package br.com.btg360.entities

import br.com.btg360.application.Entity
import br.com.btg360.constants.{Channel, Database}
import br.com.btg360.services.JsonService

class QueueEntity extends Entity {

  private val jsonService = new JsonService()

  //Queue
  var userRuleId: Long = 0
  var today: Any = _ //Date or String
  var userId: Long = 0
  var groupId: Long = 0
  var ruleTypeId: Int = 0
  var ruleName: String = _
  var isPeal: Int = 0
  var priority: Long = 0
  var status: Int = 0
  var consolidatedTableName: String = _
  var channels: String = _
  var recommendationModule: String = _
  var createdIn: Any = _ //Date or String
  var startedIn: Any = _ //Date or String
  var preparedIn: Any = _ //Date or String
  var recommendationStartedIn: Any = _ //Date or String
  var recommendationPreparedIn: Any = _ //Date or String
  var processedIn: Any = _ //Date or String
  var finalizedIn: Any = _ //Date or String
  //Queue Configs
  var sendLimit: Int = 1
  var vmta: String = _
  var dataStringJson: String = _
  var rule: RuleDataEntity = _
  //Channel
  var channelName: String = _

  def parse: QueueEntity = {
    if (this.rule == null) {
      this.rule = this.jsonService.decode[RuleDataEntity](this.dataStringJson)
    }

    this
  }

  /**
    * Returns the name of the consolidated table of products
    *
    * @return String
    */
  def getConsolidatedTable: String = {
    val channelName = if (this.channelName.isEmpty) Channel.EMAIL else this.channelName
    "%s.%s_%s".format(Database.CONSOLIDATED, this.consolidatedTableName, channelName)
  }

  /**
    * Returns the name of the table of products
    *
    * @return
    */
  def getProductTable: String = {
    "product_%d".format(this.rule.allinId)
  }

}
