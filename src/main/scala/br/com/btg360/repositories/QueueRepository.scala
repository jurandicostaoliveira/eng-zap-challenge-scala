package br.com.btg360.repositories

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, QueueStatus, Table, TypeConverter => TC}
import br.com.btg360.entities.QueueEntity
import br.com.btg360.jdbc.MySqlBtg360
import br.com.btg360.services.PeriodService

import scala.collection.mutable.HashMap


class QueueRepository extends Repository {

  private val periodService = new PeriodService()

  private val today = this.periodService.format("yyyy-MM-dd").now

  private val db = new MySqlBtg360().open

  //TABLES
  private val rulesQueueTable = "%s.%s".format(Database.JOBS, Table.RULES_QUEUE)

  private val configsTable = "%s.%s".format(Database.PANEL, Table.CONFIGS)

  private val usersRulesTable = "%s.%s".format(Database.PANEL, Table.USERS_RULES)

  private val rulesTable = "%s.%s".format(Database.PANEL, Table.RULES)

  private val consolidatedRulesTable = "%s.%s".format(Database.PANEL, Table.CONSOLIDATED_RULES)

  /**
    * @return List
    */
  def findCurrentByStatus(statusList: List[Int]): List[QueueEntity] = {
    val query =
      s"""
          SELECT * FROM ${this.rulesQueueTable}
          WHERE today = '${this.today}' AND status IN (${statusList.mkString(",")});
      """
    this.connection(this.db).fetch(query, classOf[QueueEntity])
  }

  /**
    * @param Int  userId
    * @param List ruleTypes
    * @return List
    */
  def findAll(userId: Int, ruleTypes: List[Int]): List[QueueEntity] = {
    val query =
      s"""
        SELECT
            rules_queue.*,
            configs.limit AS sendLimit,
            configs.vmta,
            configs.referenceListToApp,
            consolidated_rules.data AS dataStringJson
        FROM
            ${this.rulesQueueTable} AS rules_queue
                INNER JOIN
            ${this.configsTable} AS configs ON configs.userId = rules_queue.userId
                INNER JOIN
            ${this.usersRulesTable} AS users_rules ON users_rules.id = rules_queue.userRuleId
                INNER JOIN
            ${this.rulesTable} AS rules ON rules.id = users_rules.ruleId
                INNER JOIN
            ${this.consolidatedRulesTable} AS consolidated_rules ON consolidated_rules.userRuleId = users_rules.id
        WHERE rules_queue.userId = $userId
            AND rules_queue.today = '${this.today}'
            AND rules_queue.ruleTypeId IN (${ruleTypes.mkString(",")})
            AND rules_queue.status < ${QueueStatus.PROCESSED}
            AND configs.btg = 1
            AND users_rules.isDeleted = 0
            AND users_rules.status = 1
            AND rules.status = 1
            AND consolidated_rules.status = 1
        ORDER BY rules_queue.priority ASC;
      """
    this.connection(this.db).fetch(query, classOf[QueueEntity])
  }

  /**
    * Create queue to client
    *
    * @param QueueEntity entity
    */
  def create(entity: QueueEntity): Unit = {
    this.insertOrUpdate(this.rulesQueueTable, entity, List(
      "userRuleId",
      "today",
      "userId",
      "groupId",
      "ruleTypeId",
      "ruleName",
      "isPeal",
      "priority",
      "status",
      "consolidatedTableName",
      "channels",
      "recommendationModule",
      "createdIn",
      "startedIn",
      "preparedIn",
      "recommendationStartedIn",
      "recommendationPreparedIn"), List("channels"))
  }

  /**
    * @param Int userId
    * @return Int
    */
  def lastPriority(userId: Int): Int = {
    val query =
      s"""
        SELECT COUNT(userRuleId) AS total
        FROM ${this.rulesQueueTable}
        WHERE userId = $userId AND today = '${this.today}';
      """
    this.countByColumnName(this.connection(this.db).queryExecutor(query), "total")
  }

  /**
    * @param Int userRuleId
    * @param Int status
    */
  def updateStatus(userRuleId: Int, status: Int): Unit = {
    val now = this.periodService.format("yyyy-MM-dd HH:mm:ss").now
    val data: HashMap[String, Any] = status match {
      case QueueStatus.CREATED => HashMap("createdIn" -> now, "status" -> status)
      case QueueStatus.STARTED => HashMap("startedIn" -> now, "status" -> status)
      case QueueStatus.PREPARED => HashMap("preparedIn" -> now, "status" -> status)
      case QueueStatus.RECOMMENDATION_STARTED => HashMap("recommendationStartedIn" -> now, "status" -> status)
      case QueueStatus.RECOMMENDATION_PREPARED => HashMap("recommendationPreparedIn" -> now, "status" -> status)
      case QueueStatus.PROCESSED => HashMap("processedIn" -> now, "status" -> status)
      case QueueStatus.FINALIZED => HashMap("finalizedIn" -> now, "status" -> status)
      case _ => null
    }

    if (data != null) {
      this.connection(this.db)
        .whereAnd("userRuleId", "=", userRuleId)
        .whereAnd("today", "=", this.today)
        .update(this.rulesQueueTable, data)
    }
  }

  /**
    * Delete queue data with period of days
    *
    * @param Int days
    */
  def deleteOnDays(days: Int = 30): Unit = {
    val day = this.periodService.format("yyyy-MM-dd").timeByDay(-days)
    val query = s"""DELETE FROM ${this.rulesQueueTable} WHERE today <= '$day';"""
    this.connection(this.db).queryExecutor(query, true)
  }

  /**
    * Get data from queue by id
    *
    * @param Int userRuleId
    * @return QueueEntity
    */
  def findByUserRuleId(userRuleId: Int): QueueEntity = {
    val query =
      s"""
         SELECT * FROM
            ${this.rulesQueueTable}
         WHERE
            today = '${this.today}'
            AND userRuleId = ${userRuleId};
       """
    val list = this.connection(this.db).fetch(query, classOf[QueueEntity])
    if (list.size > 0) {
      return list.head
    }

    new QueueEntity()
  }

}
