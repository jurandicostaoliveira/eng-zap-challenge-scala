package repositories

import java.sql.{Connection, ResultSet, Statement}

import application.Repository

class SchemaRepository extends Repository {

  val dbDriver : String = "mysql"

  def findTablesByLikeExpression(dataBase : String, tableNameLikeExpr : String)
  : ResultSet = {
    try {
      val query : String = "SELECT table_name FROM information_schema.tables " +
        "WHERE " +
        s"table_schema = '$dataBase' AND " +
        s"TABLE_NAME LIKE '%$tableNameLikeExpr%' AND " +
        s"TABLE_NAME NOT LIKE 'peal_%' AND " +
        s"TABLE_NAME NOT LIKE '%_email' AND " +
        s"TABLE_NAME NOT LIKE '%_facebook' AND " +
        s"TABLE_NAME NOT LIKE '%_webpush' AND " +
        s"TABLE_NAME NOT LIKE '%_mobile';"
      jdbcConnection.setDbDriver(dbDriver)
      val connection: Connection = jdbcConnection.open
      val stmt: Statement = connection.createStatement()
      val resultSet: ResultSet = stmt.executeQuery(query)
      resultSet
    } catch {
      case e : Exception => e.printStackTrace()
        null
    }
  }

  def alterTableToMultichannel(tableName : String) : Boolean = {
    try {
      val query : String = "ALTER TABLE " +
                            s"btg_consolidated.$tableName " +
                            "CHANGE cookieBid userId text, " +
                            "CHANGE email userSent text," +
                            "ADD platformId int(11) NOT NULL;"

      jdbcConnection.setDbDriver(dbDriver)
      val connection: Connection = jdbcConnection.open
      val stmt: Statement = connection.createStatement()
      stmt.executeUpdate(query)
      true
    } catch {
      case e : Exception => e.printStackTrace()
        false
    }
  }

  def renameTableWithSuffix(tableName : String, suffix : String) : Boolean = {
    try {
      val query : String = s"RENAME TABLE btg_consolidated.$tableName TO btg_consolidated.$tableName" + s"_$suffix;"

      jdbcConnection.setDbDriver(dbDriver)
      val connection: Connection = jdbcConnection.open
      val stmt: Statement = connection.createStatement()
      stmt.executeUpdate(query)
      true
    } catch {
      case e : Exception => e.printStackTrace()
        false
    }
  }

}
