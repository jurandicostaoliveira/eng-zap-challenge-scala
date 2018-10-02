package br.com.btg360.application

import java.util.Properties
import br.com.btg360.jdbc.JDBCConnection

abstract class Application {

  def jdbcConnection: JDBCConnection = new JDBCConnection

  def getJdbcProperties: Properties = this.jdbcConnection.getProperties

}
