package br.com.btg360.application

import java.util.Properties

import br.com.btg360.jdbc.JDBCConnection
import br.com.btg360.services.PropService

class Application {

  def propService : PropService = { new PropService }

  def jdbcConnection : JDBCConnection = { new JDBCConnection }

  def getJdbcProperties : Properties = { jdbcConnection.getProperties }
}
