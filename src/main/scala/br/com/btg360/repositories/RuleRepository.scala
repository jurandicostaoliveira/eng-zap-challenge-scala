package br.com.btg360.repositories

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, Table}
import br.com.btg360.entities.RuleEntity

class RuleRepository extends Repository {

  private val ruleTable = "%s.%s".format(Database.PANEL, Table.RULES)

  private val userRuleTable = "%s.%s".format(Database.PANEL, Table.USERS_RULES)

  private val consolidatedRuleTable = "%s.%s".format(Database.PANEL, Table.CONSOLIDATED_RULES)

  private val configsTable = "%s.%s".format(Database.PANEL, Table.CONFIGS)

  def findActiveByUserId(userId: Int, isPeal: Boolean = false): List[RuleEntity] = {
    val strToPeal: String = if (isPeal == true) ">=1" else "=0"
    val query: String =
      s"""
        SELECT
          userRuleTable.id,
          userRuleTable.priority,
          ruleTable.ruleTypeId AS typeId,
          ruleTable.periodId,
          ruleTable.groupId,
          consolidatedRuleTable.data AS dataStringJson
        FROM ${this.userRuleTable} AS userRuleTable
        JOIN ${this.configsTable} AS configs
         	ON configs.userId = userRuleTable.userId
        JOIN ${this.ruleTable} AS ruleTable
          ON ruleTable.id = userRuleTable.ruleId
        JOIN ${this.consolidatedRuleTable} AS consolidatedRuleTable
          ON consolidatedRuleTable.userRuleId = userRuleTable.id
        WHERE
          userRuleTable.userId = $userId
          AND configs.btg = 1
          AND userRuleTable.isDeleted = 0
          AND userRuleTable.status = 1
          AND userRuleTable.pealFrom $strToPeal
          AND ruleTable.status = 1
          AND consolidatedRuleTable.status = 1
        ORDER BY userRuleTable.priority ASC;
      """
    this.fetch(query, classOf[RuleEntity])
  }


}




