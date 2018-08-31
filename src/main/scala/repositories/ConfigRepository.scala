package repositories

import java.sql.{Connection, ResultSet, Statement}

import application.Repository
import constants.{Database, Table}

class ConfigRepository extends Repository {

  val dbDriver : String = "mysql"

  def findByUserId(userId : Int) : ResultSet = {
    try {
      val dataBase = Database.PANEL
      val table = Table.CONFIGS
      val query: String = f"SELECT * FROM $dataBase%s.$table%s WHERE userId = $userId%d;"
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
}
