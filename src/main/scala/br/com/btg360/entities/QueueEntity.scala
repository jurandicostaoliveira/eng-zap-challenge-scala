package br.com.btg360.entities

import br.com.btg360.application.Entity
import br.com.btg360.constants.Database

class QueueEntity extends Entity {

  //Queue
  var userRuleId: Int = 0
  var userId: Int = 0
  var groupId: Int = 0
  var ruleTypeId: Int = 0
  var ruleActionName: String = _
  var isPeal: Boolean = false
  var priority: Int = 0
  var status: Int = 0
  var consolidatedTable: String = _
  var productTable: String = _
  var sendLimit: Int = 1
  var vmta: String = _

  //Account
  var btgId: Int = 0
  var allinId: Int = 0
  var transactionalId: Int = 0
  var token: String = _

  //Rule
  var ruleName: String = _
  var subject: String = _
  var hour: String = _
  var senderEmail: String = _
  var senderName: String = _
  var replyEmail: String = _
  var referenceListId: Int = 0
  var listId: Int = 0
  var listExclusionId: Int = 0
  var field: String = _
  var formatField: String = _
  var filterId: Int = 0
  var interval: Int = 0
  var frequency: Int = 0
  var dayWeek: Int = 0
  var dayMonth: Int = 0

  //HTML
  var templateId: Int = 0
  var themeId: Int = 0
  var layoutId: Int = 0
  var content: String = _

  //Channels
  var channels: String = _
  var channelId: Int = 0
  var channelName: String = _
  var channelReference: String = _
  var channelTitle: String = _
  var channelMessage: String = _
  var channelUrlScheme: String = _
  var utmChannels: String = _

  // Internal ####
  var ruleLabel: String = _
  var deliveryAt: String = _
  var deliveryTimestamp: Int = 0

  /**
    * Returns the name of the consolidated table of products
    *
    * @return String
    */
  def getConsolidatedTable: String = {
    "%s.%s_%s".format(Database.CONSOLIDATED, this.consolidatedTable, this.channelName)
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
