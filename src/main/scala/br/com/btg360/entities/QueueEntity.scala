package br.com.btg360.entities

import java.util.Date

import br.com.btg360.application.Entity
import br.com.btg360.constants.Database
import br.com.btg360.services.{JsonService, TypeConverterService => TCS}

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

  //Account
  @transient var btgId: Int = 0
  @transient var allinId: Int = 0
  @transient var transactionalId: Int = 0
  @transient var token: String = _

  //Rule
  @transient var subject: String = _
  @transient var hour: String = _
  @transient var senderEmail: String = _
  @transient var senderName: String = _
  @transient var replyEmail: String = _
  @transient var referenceListId: Int = 0
  @transient var Interval: Int = 0
  @transient var frequency: Int = 0
  @transient var dayWeek: Int = 0
  @transient var dayMonth: Int = 0
  @transient var interval: Int = 0

  //Automatics
  @transient var listId: Int = 0
  @transient var listExclusionId: Int = 0
  @transient var field: String = _
  @transient var formatField: String = _
  @transient var filterId: Int = 0

  //HTML
  @transient var templateId: Int = 0
  @transient var themeId: Int = 0
  @transient var layoutId: Int = 0
  @transient var content: String = _

  //Channels
  @transient var channelsList: List[String] = List()
  @transient var channelId: Int = 0
  @transient var channelName: String = _
  @transient var channelReference: String = _
  @transient var channelTitle: String = _
  @transient var channelMessage: String = _
  @transient var channelUrlScheme: String = _
  @transient var utmChannels: String = _

  //Internal
  @transient var ruleLabel: String = _
  @transient var deliveryAt: String = _
  @transient var deliveryTimestamp: Int = 0

  def parse(): QueueEntity = {
    val dataRule = this.jsonService.decode[RuleDataEntity](this.dataStringJson)

    //Rule
    this.ruleName = TCS.toString(dataRule.configs.ruleName)
    this.subject = TCS.toString(dataRule.configs.subject)
    this.hour = TCS.toString(dataRule.configs.hour)
    this.senderEmail = TCS.toString(dataRule.configs.senderEmail)
    this.senderName = TCS.toString(dataRule.configs.senderName)
    this.replyEmail = TCS.toString(dataRule.configs.replyEmail)
    if (this.replyEmail.isEmpty) {
      this.replyEmail = this.senderEmail
    }
    this.referenceListId = TCS.toInt(dataRule.configs.list)
    this.interval = TCS.toInt(dataRule.configs.interval)
    this.frequency = TCS.toInt(dataRule.configs.frequency)
    this.dayWeek = TCS.toInt(dataRule.configs.dayWeek)
    this.dayMonth = TCS.toInt(dataRule.configs.dayMonth)

    //Automatics
    this.listId = TCS.toInt(dataRule.automatics.list)
    this.listExclusionId = TCS.toInt(dataRule.automatics.exclusion)
    this.field = TCS.toString(dataRule.automatics.field)
    this.formatField = TCS.toString(dataRule.automatics.format)
    this.filterId = TCS.toInt(dataRule.automatics.filter)

    //HTML
    this.templateId = TCS.toInt(dataRule.html.template)
    this.themeId = TCS.toInt(dataRule.html.theme)
    this.layoutId = TCS.toInt(dataRule.html.layout)
    this.content = TCS.toString(dataRule.html.content)

    //Account
    this.btgId = TCS.toInt(dataRule.account.btgId)
    this.allinId = TCS.toInt(dataRule.account.allinId)
    this.transactionalId = TCS.toInt(dataRule.account.transId)
    this.token = TCS.toString(dataRule.account.token)

    //Channels
    if (this.channels != null) {
      this.channelsList = this.jsonService.decode[List[String]](this.channels)
    }

    // : Internal
    //    this.ruleLabel = _
    //    this.deliveryAt = _
    //    this.deliveryTimestamp = 0
    this
  }

  /**
    * Returns the name of the consolidated table of products
    *
    * @return String
    */
  def getConsolidatedTable: String = {
    "%s.%s_%s".format(Database.CONSOLIDATED, this.consolidatedTableName, this.channelName)
  }

  /**
    * Returns the name of the table of products
    *
    * @return
    */
  def getProductTable: String = {
    "product_%d".format(this.allinId)
  }

}
