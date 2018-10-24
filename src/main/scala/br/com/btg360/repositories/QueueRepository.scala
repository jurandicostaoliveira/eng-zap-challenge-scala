package br.com.btg360.repositories

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, Table}
import br.com.btg360.entities.QueueEntity

class QueueRepository extends Repository {

  val rulesQueueTable = "%s.%s".format(Database.JOBS, Table.RULES_QUEUE)
  val configsTable = "%s.%s".format(Database.PANEL, Table.CONFIGS)
  val usersRulesTable = "%s.%s".format(Database.PANEL, Table.USERS_RULES)
  val rulesTable = "%s.%s".format(Database.PANEL, Table.RULES)
  val consolidatedRulesTable = "%s.%s".format(Database.PANEL, Table.CONSOLIDATED_RULES)

  def findAll(userId: Int, ruleTypes: List[Int]): QueueEntity = {


    val strQuery =
      s"""
        SELECT
            rules_queue.*,
            configs.limit AS sendLimit,
            configs.vmta,
            consolidated_rules.data AS json
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
            AND rules_queue.today = CURRENT_DATE()
            AND rules_queue.ruleTypeId IN (${ruleTypes.mkString(",")})
            AND rules_queue.status < 5
            AND configs.btg = 1
            AND users_rules.isDeleted = 0
            AND users_rules.status = 1
            AND rules.status = 1
            AND consolidated_rules.status = 1
        ORDER BY rules_queue.priority ASC;
      """

    println(strQuery)


    null

  }


}
