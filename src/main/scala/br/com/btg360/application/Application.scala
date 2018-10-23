package br.com.btg360.application

import br.com.btg360.jdbc.{MySqlAllin, MySqlBtg360}

abstract class Application extends Serializable {

  def mySqlAllin: MySqlAllin = new MySqlAllin()

  def mySqlBtg360: MySqlBtg360 = new MySqlBtg360()

  def anyToString(value: Any): String = value.toString

  def anyToInt(value: Any): Int = value.toString.toInt

  def anyToBoolean(value: Any): Boolean = value.toString.toBoolean

  def anyToFloat(value: Any): Float = value.toString.toFloat

}
