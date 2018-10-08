package br.com.btg360.repositories

import java.sql.{Connection, ResultSet, Statement}

import br.com.btg360.application.Repository

class UserRuleRepository extends Repository {

  def findActiveUsers : ResultSet = {

    try {
      val query: String = "SELECT " +
                              "DISTINCT ur.userId, u.btgId " +
                              "FROM master.users AS u " +
                              "JOIN btg_panel.users_rules AS ur ON " +
                              "u.id = ur.userId " +
                              "WHERE " +
                              "ur.status = 1 AND " +
                              "u.isMultiChannel = 1;"
      queryExecutor(query)
    } catch {
      case e : Exception => e.printStackTrace()
        null
    }
  }

  def findActiveByUserId(userId: Int, isPeal: Boolean): ResultSet = {
    try {
      val isPealSeparator: String = if (isPeal == true) ">=" else "="
      val isPealToInt: Int = if(isPeal) 1 else 0

      val query: String = "SELECT " +
                          "users_rules.id AS userRuleId, " +
                          "rules.ruleTypeId, " +
                          "rules.periodId, " +
                          "rules.groupId, " +
                          "rules.alias as ruleName, " +
                          "consolidated_rules.data " +
                          "FROM " +
                          "btg_panel.users_rules " +
                          "INNER JOIN " +
                          "btg_panel.rules " +
                          "ON " +
                          "btg_panel.rules.id = btg_panel.users_rules.ruleId " +
                          "INNER JOIN " +
                          "btg_panel.consolidated_rules " +
                          "ON " +
                          "btg_panel.consolidated_rules.userRuleId = users_rules.id " +
                          "WHERE " +
                          s"btg_panel.users_rules.userId = $userId " +
                          "AND btg_panel.users_rules.status = 1 " +
                          "AND btg_panel.consolidated_rules.status = 1 " +
                          s"AND btg_panel.users_rules.pealFrom $isPealSeparator $isPealToInt " +
                          "AND btg_panel.users_rules.isDeleted = 0 " +
                          "AND btg_panel.rules.status = 1 " +
                          "ORDER BY btg_panel.users_rules.priority ASC;"
      queryExecutor(query)
    } catch {
      case e: Exception => e.printStackTrace()
        null
    }
  }
}
