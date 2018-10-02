package br.com.btg360.application

import java.util.Properties

import br.com.btg360.jdbc.JDBCConnection
import br.com.btg360.services.{
  PeriodService,
  PropService,
  RedisClientService,
  DailySendLimitService
}

abstract class Application {

  def propService: PropService = new PropService

  def jdbcConnection: JDBCConnection = new JDBCConnection

  def getJdbcProperties: Properties = this.jdbcConnection.getProperties

  def periodService(format: String = "yyyy-MM-dd HH:mm:ss"): PeriodService = new PeriodService(format)

  def redisClientService: RedisClientService = new RedisClientService()

  def dailySendLimitService: DailySendLimitService = new DailySendLimitService()

}
