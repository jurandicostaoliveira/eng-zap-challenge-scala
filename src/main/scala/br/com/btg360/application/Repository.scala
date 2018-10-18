package br.com.btg360.application

import java.sql.{Connection, ResultSet, Statement}

abstract class Repository extends Model {

  val dbBtg360: Connection = mySqlBtg360.open

  def queryExecutor(query: String): ResultSet = {
    val stmt: Statement = this.dbBtg360.createStatement()
    stmt.executeQuery(query)
  }

}
