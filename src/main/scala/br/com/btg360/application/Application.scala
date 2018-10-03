package br.com.btg360.application

import br.com.btg360.jdbc.{MySqlAllin, MySqlBtg360}

abstract class Application {

  def mySqlAllin: MySqlAllin = new MySqlAllin()

  def mySqlBtg360: MySqlBtg360 = new MySqlBtg360()

}
