package br.com.btg360.entities

import br.com.btg360.application.Entity
import br.com.btg360.constants.{Channel, Database, Rule, TypeConverter => TC}
import br.com.btg360.services.{JsonService, PeriodService}

class QueueEntity extends Entity {

  //Queue
  var userRuleId: Long = 0
  var today: Any = _ //Date or String
  var userId: Long = 0
  var groupId: Long = 0
  var ruleTypeId: Int = 0
  var ruleName: String = ""
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
  var vmta: String = ""
  var referenceListToApp: Int = 0
  var byAvailability: Boolean = false
  var dataStringJson: String = ""
  var rule: RuleDataEntity = _
  //Channel
  var channelName: String = Channel.EMAIL
  var platformId: Int = 0
  var deliveryHourAt: String = ""
  var deliveryAt: String = ""
  var deliveryTimestamp: Long = 0
  var utmLink: String = ""

  /**
    * Parsed the rule configuration data
    *
    * @return this
    */
  def parse: QueueEntity = {
    if (this.rule == null) {
      this.rule = new JsonService().decode[RuleDataEntity](this.dataStringJson)
      this.deliveryHourAt = this.generateDeliveryHourAt
      this.deliveryAt = this.generateDeliveryAt
      //this.deliveryTimestamp = this.generateDeliveryTimestamp
      this.utmLink = this.generateUtmLink
    }

    this
  }

  /**
    * Returns the name of the consolidated table of products
    *
    * @return String
    */
  def getConsolidatedTable: String = {
    "%s.%s_%s".format(Database.CONSOLIDATED, this.consolidatedTableName, Channel.getLabel(this.channelName))
  }

  /**
    * Returns the name of the table of products
    *
    * @return
    */
  def getProductTable: String = {
    "product_%d".format(this.rule.allinId)
  }

  /**
    * Returns the hour to exit sending
    *
    * @return String
    */
  private def generateDeliveryHourAt: String = {
    val timeNow = "%d:00:00".format(TC.toInt(new PeriodService("HH").now) + 1)
    var hour = this.rule.hour

    if (hour.isEmpty) {
      hour = timeNow
    }

    if (this.ruleTypeId == Rule.HOURLY_ID) {
      hour = timeNow
    }

    hour
  }

  /**
    * Returns the time to exit sending
    *
    * @return String
    */
  private def generateDeliveryAt: String = {
    "%s %s".format(this.today, this.generateDeliveryHourAt)
  }

  /**
    * Returns timestamp to exit sending
    *
    * @return Long
    */
  private def generateDeliveryTimestamp: Long = {
    new PeriodService().toDate(this.deliveryAt).getTime / 1000 //Hack to PHP
  }

  /**
    * Return the query string umts from google analytics
    *
    * @return String
    */
  private def generateUtmLink: String = {
    var list: List[String] = List()
    this.rule.channelMap(this.channelName).utms.foreach(row => {
      list = list :+ "%s=%s".format(row.key, row.value)
    })
    list.mkString("&")
  }

}
