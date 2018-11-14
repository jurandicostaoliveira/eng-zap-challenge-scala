package br.com.btg360.services

import br.com.btg360.constants.{Period, QueueStatus, Rule}
import br.com.btg360.entities.{QueueEntity, RuleEntity}
import br.com.btg360.repositories.{QueueRepository, RuleRepository}
import br.com.btg360.constants.{TypeConverter => TC}


class QueueManagerService {

  val ruleRepository = new RuleRepository()

  val queueRepository = new QueueRepository()

  val jsonService = new JsonService()

  val periodService = new PeriodService()

  val today = periodService.format("yyyy-MM-dd").now

  val now = periodService.format("yyyy-MM-dd HH:mm:ss").now

  /**
    * @param Int     userId
    * @param Boolean isPeal
    * @return this
    */
  def create(userId: Int, isPeal: Boolean = false): QueueManagerService = {
    var priority = this.queueRepository.lastPriority(userId)
    val rows = this.ruleRepository.findActiveByUserId(userId, isPeal)

    for (row <- rows) {
      val rule = row.parse
      if (this.isAllowed(rule)) {
        priority += 1
        val settings = this.generateSettings(rule, isPeal)
        val entity = new QueueEntity()
        entity.userRuleId = rule.id
        entity.today = this.today
        entity.userId = userId
        entity.groupId = rule.groupId
        entity.ruleTypeId = TC.toInt(settings("ruleTypeId"))
        entity.ruleName = TC.toString(settings("ruleName"))
        entity.isPeal = TC.toInt(settings("isPeal"))
        entity.priority = priority
        entity.status = TC.toInt(settings("status"))
        entity.consolidatedTableName = TC.toString(settings("consolidatedTableName"))
        entity.channels = this.jsonService.encode(rule.data.channelMap.keys)
        entity.recommendationModule = TC.toString(settings("recommendationModule"))
        entity.createdIn = this.now
        entity.startedIn = settings("startedIn")
        entity.preparedIn = settings("preparedIn")
        entity.recommendationStartedIn = settings("recommendationStartedIn")
        entity.recommendationPreparedIn = settings("recommendationPreparedIn")
        this.queueRepository.create(entity)
      }
    }

    this
  }

  /**
    * Validation if it is allowed to create the period rule
    *
    * @param RuleEntity rule
    * @return Boolean
    */
  private def isAllowed(rule: RuleEntity): Boolean = {
    if (rule.periodId == Period.DAILY_ID) {
      return true
    }

    var dayWeek = this.periodService.dayOfWeek
    dayWeek = if (dayWeek > 1) (dayWeek - 1) else 7
    if (rule.periodId == Period.WEEKLY_ID && rule.data.dayWeek == dayWeek) {
      return true
    }

    if (rule.periodId == Period.MONTHLY_ID && rule.data.dayMonth == this.periodService.dayOfMonth) {
      return true
    }

    false
  }

  /**
    * @param RuleEntity rule
    * @param Boolean    isPeal
    * @return Map
    */
  private def generateSettings(rule: RuleEntity, isPeal: Boolean): Map[String, Any] = {
    var values: Map[String, Any] = Map()
    values = this.hasPeal(rule, isPeal) ++ this.hasAutomatic(rule, isPeal)
    values
  }

  /**
    * @param RuleEntity rule
    * @param Boolean    isPeal
    * @return String
    */
  private def generateConsolidatedTableName(rule: RuleEntity, isPeal: Boolean): String = {
    "%s_%d_%d_%s".format(
      if (isPeal) "peal" else TC.toString(rule.data.name).replace("-", "_"),
      rule.data.btgId,
      rule.id,
      this.periodService.format("yyyy_MM_dd").now
    )
  }

  /**
    * @param RuleEntity rule
    * @return Map
    */
  private def generateRecommendationModule(rule: RuleEntity): String = {
    this.jsonService.encode(Map(
      "name" -> rule.data.moduleName,
      "limit" -> rule.data.moduleLimit,
      "percentMin" -> rule.data.modulePercentMin,
      "percentMax" -> rule.data.modulePercentMax
    ))
  }

  /**
    * @param RuleEntity rule
    * @param Boolean    isPeal
    * @return Map
    */
  private def hasPeal(rule: RuleEntity, isPeal: Boolean): Map[String, Any] = {
    var ruleTypeId: Int = rule.typeId
    var ruleName: String = rule.data.name
    var intIsPeal: Int = 0

    if (isPeal) {
      ruleTypeId = Rule.DAILY_ID
      ruleName = "%s-peal".format(ruleName)
      intIsPeal = 1
    }

    Map(
      "ruleTypeId" -> ruleTypeId,
      "ruleName" -> ruleName,
      "isPeal" -> intIsPeal
    )
  }

  /**
    * @param RuleEntity rule
    * @param Boolean    isPeal
    * @return Map
    */
  private def hasAutomatic(rule: RuleEntity, isPeal: Boolean): Map[String, Any] = {
    var status: Int = QueueStatus.RECOMMENDATION_PREPARED
    var consolidatedTableName: String = null
    var recommendationModule: String = null
    var now: String = this.now

    if (rule.typeId != Rule.AUTOMATIC_ID) {
      status = QueueStatus.CREATED
      consolidatedTableName = this.generateConsolidatedTableName(rule, isPeal)
      recommendationModule = this.generateRecommendationModule(rule)
      now = null
    }

    Map(
      "status" -> status,
      "consolidatedTableName" -> consolidatedTableName,
      "recommendationModule" -> recommendationModule,
      "startedIn" -> now,
      "preparedIn" -> now,
      "recommendationStartedIn" -> now,
      "recommendationPreparedIn" -> now
    )
  }

}
