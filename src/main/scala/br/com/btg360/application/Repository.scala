package br.com.btg360.application

import java.sql.{Connection, ResultSet, Statement}

abstract class Repository extends Model {

  val mySqlBtg360Connection: Connection = mySqlBtg360.open

  def queryExecutor(query: String): ResultSet = {
    val stmt: Statement = mySqlBtg360Connection.createStatement()
    stmt.executeQuery(query)
  }
}
