package br.com.btg360.services

import br.com.btg360.constants.Rule
import br.com.btg360.entities.{QueueEntity, RuleEntity}
import br.com.btg360.repositories.{QueueRepository, RuleRepository}


class QueueManagerService {

  val ruleRepository = new RuleRepository()

  val queueRepository = new QueueRepository()

  val jsonService = new JsonService()

  val periodService = new PeriodService()

  val today = periodService.format("yyyy-MM-dd").now

  val now = periodService.format("yyyy-MM-dd HH:mm:ss").now

  def create(userId: Int, isPeal: Boolean = false): QueueManagerService = {
    var priority = this.queueRepository.lastPriority(userId)
    val rules = this.ruleRepository.findActiveByUserId(userId, isPeal)

    for (row <- rules) {
      priority += 1
      val rule = row.parse
      val entity = new QueueEntity()
      entity.userRuleId = rule.id
      entity.today = this.today
      entity.userId = userId
      entity.groupId = rule.groupId
      entity.ruleTypeId = rule.typeId
      entity.ruleName = rule.data.name
      entity.isPeal = if (isPeal) 1 else 0
      entity.priority = priority
      entity.status = 0
      entity.consolidatedTableName = this.generateConsolidatedTableName(rule, isPeal)
      entity.channels = this.jsonService.encode(rule.data.channelMap.keys)
      entity.recommendationModule = ""
      entity.createdIn = this.now
      entity.startedIn = null
      entity.preparedIn = null
      entity.recommendationStartedIn = null
      entity.recommendationPreparedIn = null
      entity.processedIn = null
      entity.finalizedIn = null
      this.queueRepository.create(entity)
    }

    this
  }

  /**
    * @param RuleEntity rule
    * @param Boolean    isPeal
    * @return String
    */
  private def generateConsolidatedTableName(rule: RuleEntity, isPeal: Boolean): String = {
    if (rule.typeId == Rule.AUTOMATIC_ID) {
      return null
    }

    val table = if (isPeal) "peal" else rule.data.name.replace("-", "_")
    "%s_%d_%d_%s".format(table, rule.data.btgId, rule.id, this.periodService.format("yyyy_MM_dd").now)
  }

}
