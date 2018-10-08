package br.com.btg360.model

import java.sql.ResultSet

import br.com.btg360.application.{Context, Model}
import br.com.btg360.repositories.{RuleQueueRepository, UserRuleRepository}
import br.com.btg360.traits.ModelFactoryTrait

import scala.collection.mutable.HashMap

class QueueCreateModel(context: Context) extends Model with Serializable with ModelFactoryTrait {

  var accountId = context.accountEntity.accountId

  var userRuleRepository: UserRuleRepository = _
  var ruleQueueRepository: RuleQueueRepository = _

  override def build: Unit = {
    try {
      userRuleRepository = new UserRuleRepository
      ruleQueueRepository = new RuleQueueRepository
      this.add(false)
        .add(true)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def add(isPeal: Boolean = false): QueueCreateModel = {
    try {
      println("isPeal: " + isPeal)

      val rules: ResultSet = userRuleRepository.findActiveByUserId(accountId, isPeal)
      val priority: Int = ruleQueueRepository.countByUserId(accountId)

      if (rules == null || countRows(rules) <= 0) {
        println("-----Rules result is null or not have value-----")
        return this
      }

      println(s"\nuserId: $accountId <==> isPeal: $isPeal")

      var list: List[HashMap[String, String]] = List(HashMap())
      var hashMap: HashMap[String, String] = HashMap()

      while (rules.next()) {
          hashMap += ("userRuleId" -> rules.getInt("userRuleId").toString)
          hashMap += ("groupId" -> rules.getInt("groupId").toString)
          hashMap += ("ruleName" -> rules.getString("ruleName"))
          list ::= hashMap
      }

      list.flatten.foreach(l => {
          println(l._1 + " -> " + l._2)
      })

      this
    } catch {
      case e: Exception => e.printStackTrace()
        this
    }
  }
}
