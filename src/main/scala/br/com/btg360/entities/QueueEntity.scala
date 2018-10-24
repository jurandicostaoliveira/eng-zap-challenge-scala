package br.com.btg360.entities

import java.util.Date

import br.com.btg360.application.Entity
import br.com.btg360.constants.Database
import br.com.btg360.services.TypeConverterService


class QueueEntity extends Entity {

  val tcs = this.invoke(classOf[TypeConverterService])

  //Queue
  var userRuleId: Int = _
  var today: Date = _
  var userId: Int = 0
  var groupId: Int = 0
  var ruleTypeId: Int = 0
  var ruleName: String = _
  var isPeal: Int = 0
  var priority: Int = 0
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

  def setRule(data: RuleDataEntity): QueueEntity = {
    //Queue
    //    this.sendLimit = 1
    //    this.vmta = _

    //Rule
    this.ruleName = this.tcs.toString(data.configs.ruleName)
    this.subject = this.tcs.toString(data.configs.subject)
    this.hour = this.tcs.toString(data.configs.hour)
    this.senderEmail = this.tcs.toString(data.configs.senderEmail)
    this.senderName = this.tcs.toString(data.configs.senderName)
    this.replyEmail = this.tcs.toString(data.configs.replyEmail)
    if (this.replyEmail.isEmpty) {
      this.replyEmail = this.senderEmail
    }
    this.referenceListId = this.tcs.toInt(data.configs.list)
    this.interval = this.tcs.toInt(data.configs.interval)
    this.frequency = this.tcs.toInt(data.configs.frequency)
    this.dayWeek = this.tcs.toInt(data.configs.dayWeek)
    this.dayMonth = this.tcs.toInt(data.configs.dayMonth)

    //Automatics
    this.listId = this.tcs.toInt(data.automatics.list)
    this.listExclusionId = this.tcs.toInt(data.automatics.exclusion)
    this.field = this.tcs.toString(data.automatics.field)
    this.formatField = this.tcs.toString(data.automatics.format)
    this.filterId = this.tcs.toInt(data.automatics.filter)

    //HTML
    this.templateId = this.tcs.toInt(data.html.template)
    this.themeId = this.tcs.toInt(data.html.theme)
    this.layoutId = this.tcs.toInt(data.html.layout)
    this.content = this.tcs.toString(data.html.content)

    //Account
    this.btgId = this.tcs.toInt(data.account.btgId)
    this.allinId = this.tcs.toInt(data.account.allinId)
    this.transactionalId = this.tcs.toInt(data.account.transId)
    this.token = this.tcs.toString(data.account.token)

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
