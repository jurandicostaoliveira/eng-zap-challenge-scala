package br.com.btg360.entities

import br.com.btg360.application.Entity
import br.com.btg360.constants.Database


class QueueEntity extends Entity {

  //Queue
  var userRuleId: Int = _
  var today: String = _
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
  var createdIn: String = _
  var startedIn: String = _
  var preparedIn: String = _
  var recommendationStartedIn: String = _
  var recommendationPreparedIn: String = _
  var processedIn: String = _
  var finalizedIn: String = _

  //Queue  2
  @transient var ruleActionName: String = _
  var productTable: String = _
  var sendLimit: Int = 1
  var vmta: String = _

  //Account
  var btgId: Int = 0
  var allinId: Int = 0
  var transactionalId: Int = 0
  var token: String = _

  //Rule
  var subject: String = _
  var hour: String = _
  var senderEmail: String = _
  var senderName: String = _
  var replyEmail: String = _
  var referenceListId: Int = 0
  var Interval: Int = 0
  var frequency: Int = 0
  var dayWeek: Int = 0
  var dayMonth: Int = 0
  var interval: Int = 0

  //Automatics
  var listId: Int = 0
  var listExclusionId: Int = 0
  var field: String = _
  var formatField: String = _
  var filterId: Int = 0

  //HTML
  var templateId: Int = 0
  var themeId: Int = 0
  var layoutId: Int = 0
  var content: String = _

  //Channels
  var channelId: Int = 0
  var channelName: String = _
  var channelReference: String = _
  var channelTitle: String = _
  var channelMessage: String = _
  var channelUrlScheme: String = _
  var utmChannels: String = _

  //Internal
  var ruleLabel: String = _
  var deliveryAt: String = _
  var deliveryTimestamp: Int = 0

  def setRule(data: RawRuleEntity): QueueEntity = {
    //Queue
    this.sendLimit = 1
    this.vmta = _

    //Rule
    this.ruleName = this.anyToString(data.configs.ruleName)
    this.subject = this.anyToString(data.configs.subject)
    this.hour = this.anyToString(data.configs.hour)
    this.senderEmail = this.anyToString(data.configs.senderEmail)
    this.senderName = this.anyToString(data.configs.senderName)
    this.replyEmail = this.anyToString(data.configs.replyEmail)
    if (this.replyEmail.isEmpty) {
      this.replyEmail = this.senderEmail
    }
    this.referenceListId = this.anyToInt(data.configs.list)
    this.interval = this.anyToInt(data.configs.interval)
    this.frequency = this.anyToInt(data.configs.frequency)
    this.dayWeek = this.anyToInt(data.configs.dayWeek)
    this.dayMonth = this.anyToInt(data.configs.dayMonth)

    //Automatics
    this.listId = this.anyToInt(data.automatics.list)
    this.listExclusionId = this.anyToInt(data.automatics.exclusion)
    this.field = this.anyToString(data.automatics.field)
    this.formatField = this.anyToString(data.automatics.format)
    this.filterId = this.anyToInt(data.automatics.filter)

    //HTML
    this.templateId = this.anyToInt(data.html.template)
    this.themeId = this.anyToInt(data.html.theme)
    this.layoutId = this.anyToInt(data.html.layout)
    this.content = this.anyToString(data.html.content)

    //Account
    this.btgId = this.anyToInt(data.account.btgId)
    this.allinId = this.anyToInt(data.account.allinId)
    this.transactionalId = this.anyToInt(data.account.transId)
    this.token = this.anyToString(data.account.token)

    // : Internal
    this.ruleLabel = _
    this.deliveryAt = _
    this.deliveryTimestamp = 0

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
