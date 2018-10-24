package br.com.btg360.repositories

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, Table}
import br.com.btg360.entities.RuleEntity

class RuleRepository extends Repository {

  private val ruleTable = "%s.%s".format(Database.PANEL, Table.RULES)

  private val userRuleTable = "%s.%s".format(Database.PANEL, Table.USERS_RULES)

  private val consolidatedRuleTable = "%s.%s".format(Database.PANEL, Table.CONSOLIDATED_RULES)

  def findActiveByUserId(userId: Int, isPeal: Boolean = false): List[RuleEntity] = {
    val strIsPeal: String = if (isPeal == true) ">=1" else "=0"
    val query: String =
      """
        SELECT
          userRuleTable.id AS userRuleId,
          userRuleTable.priority,
          ruleTable.ruleTypeId,
          ruleTable.periodId,
          ruleTable.groupId,
          ruleTable.alias AS ruleName,
          consolidatedRuleTable.data
        FROM %s AS userRuleTable
        JOIN %s AS ruleTable
          ON ruleTable.id = userRuleTable.ruleId
        JOIN %s AS consolidatedRuleTable
          ON consolidatedRuleTable.userRuleId = userRuleTable.id
        WHERE
          userRuleTable.userId = %d
          AND userRuleTable.isDeleted = 0
          AND userRuleTable.status = 1
          AND userRuleTable.pealFrom %s
          AND ruleTable.status = 1
          AND consolidatedRuleTable.status = 1
        ORDER BY userRuleTable.priority ASC;
      """.format(this.userRuleTable, this.ruleTable, this.consolidatedRuleTable, userId, strIsPeal)

    println(query)

    this.fetch(query, classOf[RuleEntity])
  }


}




