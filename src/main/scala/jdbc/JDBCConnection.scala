package jdbc

import java.sql.{Connection, DriverManager}
import java.util.Properties

import services.PropService

class JDBCConnection {

  private var dbDriver : String = "mysql"

  private var connection : Connection = _

  def getConnection : Connection = connection

  def setConnection(value : Connection) : Unit = connection = value

  def setDbDriver(driver : String) : Unit = dbDriver = driver

  def getProperties : Properties = {
    val fileName = String.format("%s-%s.properties", this.dbDriver, "development")
    new PropService().get(fileName)
  }

  def open : Connection = {
    try {
      Class.forName(getProperties.getProperty("driver"))
      connection = DriverManager.getConnection(getProperties.getProperty("host"),
        getProperties.getProperty("user"), getProperties.getProperty("password"))
      connection
    } catch {
      case e : Exception => e.printStackTrace()
        null
    }
  }
}
