package br.com.btg360.repositories

import java.sql.{Connection, ResultSet, Statement}

import br.com.btg360.application.Repository

class UserRuleRepository extends Repository {

  def findActiveUsers : ResultSet = {
    try {
      val query : String = "SELECT " +
                              "DISTINCT ur.userId, u.btgId " +
                              "FROM master.users AS u " +
                              "JOIN btg_panel.users_rules AS ur ON " +
                              "u.id = ur.userId " +
                              "WHERE " +
                              "ur.status = 1 AND " +
                              "u.isMultiChannel = 1;"
      val connection: Connection = mySqlBtg360.open
      val stmt: Statement = connection.createStatement()
      val resultSet: ResultSet = stmt.executeQuery(query)
      resultSet
    } catch {
      case e : Exception => e.printStackTrace()
        null
    }
  }

}
