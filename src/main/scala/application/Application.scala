package application

import java.util.Properties

import jdbc.JDBCConnection
import services.PropService

class Application {

  def propService : PropService = { new PropService }

  def jdbcConnection : JDBCConnection = { new JDBCConnection }

  def getJdbcProperties : Properties = { jdbcConnection.getProperties }
}
