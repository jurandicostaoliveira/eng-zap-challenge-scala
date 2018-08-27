package scheduling.job

import org.quartz.Job
import org.quartz.JobExecutionContext

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }

object Greeter {
  //#greeter-messages
  def props(message: String, printerActor: ActorRef): Props = Props(new Greeter(message, printerActor))
  //#greeter-messages
  //  final case class WhoToGreet(who: String)
  final case class WhoToGreet(who: Int)
  case object Greet
}

class Greeter(message: String, printerActor: ActorRef) extends Actor with ActorLogging {
  import Greeter._
  import Printer._

  var greeting: Int = 0

  def receive = {
    case WhoToGreet(who) =>
      //      greeting = message + ", " + who
      greeting = who * 10
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
  final case class Greeting(greeting: Int)
}

class Printer extends Actor with ActorLogging {
  import Printer._

  def receive = {
    case Greeting(greeting) =>
      log.info("Greeting received (from " + sender() + "): " + greeting)
//      this.greetAndSleep(greeting)
  }

  def greetAndSleep(greeting: Int) {
      log.info("Greeting received (from " + sender() + "): " + greeting)
//    val i: Int = 0
//    val target = 1000000
//    log.info("Greeting received (from " + sender() + "): " + greeting)
//    for (i <- 1 to target) {
//      if (i == target) {
//        println("chegamos em  " + target)
//      }
//    }
  }
}

class JobCall extends Job {
  override def execute(jobExecutionContext: JobExecutionContext) = {
    import Greeter._

    val system: ActorSystem = ActorSystem("helloAkka")

    val printer: ActorRef = system.actorOf(Printer.props, "printerActor")

    val howdyGreeter: ActorRef =
      system.actorOf(Greeter.props("Howdy", printer), "howdyGreeter")

//    val helloGreeter: ActorRef =
//      system.actorOf(Greeter.props("Hello", printer), "helloGreeter")
//
//    val goodDayGreeter: ActorRef =
//      system.actorOf(Greeter.props("Good day", printer), "goodDayGreeter")

        val list: List[Int] = List(1, 2, 3, 4, 5)
    
        list.foreach(item => {
          howdyGreeter ! WhoToGreet(item)
          howdyGreeter ! Greet
        })

//    howdyGreeter ! WhoToGreet(2)
//    howdyGreeter ! Greet
//
//    howdyGreeter ! WhoToGreet(3)
//    howdyGreeter ! Greet
//
//    helloGreeter ! WhoToGreet(4)
//    helloGreeter ! Greet
//
//    goodDayGreeter ! WhoToGreet(5)
//    goodDayGreeter ! Greet

    println("AQUI NO JOB")
  }
}