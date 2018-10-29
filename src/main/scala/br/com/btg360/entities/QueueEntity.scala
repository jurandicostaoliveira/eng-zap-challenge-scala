package br.com.btg360.entities

import br.com.btg360.constants.Channel
import java.util.Date

import br.com.btg360.application.Entity
import br.com.btg360.constants.Database
import br.com.btg360.services.JsonService

class QueueEntity extends Entity {

  private val jsonService = new JsonService()

  //Queue
  var userRuleId: Long = 0
  var today: Date = _
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
  var createdIn: Date = _
  var startedIn: Date = _
  var preparedIn: Date = _
  var recommendationStartedIn: Date = _
  var recommendationPreparedIn: Date = _
  var processedIn: Date = _
  var finalizedIn: Date = _
  //Queue Configs
  var sendLimit: Int = 1
  var vmta: String = _
  var dataStringJson: String = _
  var rule: RuleDataEntity = _
  //Channel
  var channelName: String = _

  def parse: QueueEntity = {
    this.rule = this.jsonService.decode[RuleDataEntity](this.dataStringJson)
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
