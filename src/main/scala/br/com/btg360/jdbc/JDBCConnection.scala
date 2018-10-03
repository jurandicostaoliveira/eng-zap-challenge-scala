package br.com.btg360.jdbc

import java.sql.{Connection, DriverManager}
import java.util.Properties

import br.com.btg360.services.PropService
import br.com.btg360.constants.Environment

abstract class JDBCConnection {

  protected var dbDriver: String = _

  protected var driver: String = _

  protected var host: String = _

  protected var user: String = _

  protected var password: String = _

  private var _connection: Connection = _

  def connection: Connection = this._connection

  private def properties: Properties = new PropService().get("%s-%s.properties".format(this.dbDriver, Environment.getAppEnv))

  def open: Connection = {
    try {
      val p = this.properties
      Class.forName(p.getProperty(this.driver))
      this._connection = DriverManager.getConnection(
        p.getProperty(this.host),
        p.getProperty(this.user),
        p.getProperty(this.password)
      )
      this._connection
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
        this._connection
    }
  }

  def close = {
    try {
      if (this._connection != null) {
        this._connection.close()
      }
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
    }
  }

}
