package br.com.btg360.run

import java.sql.ResultSet

import br.com.btg360.repositories.{SchemaRepository, UserRuleRepository}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import br.com.btg360.run.AlterTableToPeal.schemaRepository


object Greeter {
  //#greeter-messages
  def props(message: String, printerActor: ActorRef): Props = Props(new Greeter(message, printerActor))
  //#greeter-messages
  //  final case class WhoToGreet(who: String)
  final case class WhoToGreet(who: String)
  case object Greet
}

class Greeter(message: String, printerActor: ActorRef) extends Actor with ActorLogging {
  import Greeter._
  import Printer._

  var greeting: String = ""

  def receive = {
    case WhoToGreet(who) =>
            greeting = who

    case Greet =>
      //#greeter-send-message
      printerActor ! Greeting(greeting)
    //#greeter-send-message
  }
}

object Printer {
  //#printer-messages
  def props: Props = Props[Printer]
  //#printer-messages
  final case class Greeting(greeting: String)
}

class Printer extends Actor with ActorLogging {
  import Printer._

  def receive = {
    case Greeting(greeting) =>
      log.info("Greeting received (from " + sender() + "): " + greeting)
      log.info(s"Tabela $greeting renomeada!")
      applyChanges(greeting)
  }

  def applyChanges(tableName : String) : Unit = {
    schemaRepository.alterTableToMultichannel(tableName)
    schemaRepository.renameTableWithSuffix(tableName, "email")
  }

}

object AlterTableToPeal {

  var schemaRepository : SchemaRepository = null

  var userRuleRepository : UserRuleRepository = null

  var answerConsole : String = "y"

  def main(args : Array[String]) : Unit = {
    schemaRepository = new SchemaRepository
    userRuleRepository = new UserRuleRepository

    dispatch
  }

  def dispatch : Unit = {
    val activeUsers = userRuleRepository.findActiveUsers
    var btgId : Int = 0
    while(activeUsers.next()) {
      btgId = activeUsers.getInt("btgId")
      println("BTG_ID: " + btgId)
      getAllTablesById(btgId)
    }
  }

  def getAllTablesById(btgId : Int) : Unit = {

    import Greeter._

    val system : ActorSystem = ActorSystem("helloAkka")

    val printer : ActorRef = system.actorOf(Printer.props, "printerActor")

    val howdyGreeter : ActorRef =
      system.actorOf(Greeter.props("Howdy", printer), "howdyGreeter")

    val tablesName : ResultSet = schemaRepository.findTablesByLikeExpression("btg_consolidated",
                                    s"_$btgId"+"_%_2018_")

    while(tablesName.next()) {
      val tableName : String = tablesName.getString("table_name")

      if(tableName.contains("2018_08_")) {
        if(response(tableName)){
          //applyChanges(tableName)
          howdyGreeter ! WhoToGreet(tableName)
          howdyGreeter ! Greet

        } else {
          //applyChanges(tableName)
          howdyGreeter ! WhoToGreet(tableName)
          howdyGreeter ! Greet
          println(s"Tabela $tableName renomeada!")
        }

      }
    }
  }

  def response(tableName : String) : Boolean = {

    answerConsole match {
      case s if s matches "(?i)y" => {
        answerConsole = readLine(s"Do you want rename this table? $tableName? [y=Yes[one register]|a=All[all registers]]")
        if(answerConsole.equalsIgnoreCase("y")){
          return true
        }
        if(answerConsole.equalsIgnoreCase("all")){
          return false
        }
        true
      }
      case s if s matches "(?i)all" => {
        false
      }
    }

  }

  /*def applyChanges(tableName : String) : Unit = {
    schemaRepository.alterTableToMultichannel(tableName)
    schemaRepository.renameTableWithSuffix(tableName, "email")
  }*/
}
