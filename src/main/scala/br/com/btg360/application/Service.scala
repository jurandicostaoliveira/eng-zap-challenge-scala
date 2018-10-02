package br.com.btg360.application

import br.com.btg360.services._

abstract class Service extends Application {

  def propService: PropService = new PropService

  def periodService(format: String = "yyyy-MM-dd HH:mm:ss"): PeriodService = new PeriodService(format)

  def redisClientService: RedisClientService = new RedisClientService()

  def dailySendLimitService: DailySendLimitService = new DailySendLimitService()

  def optoutService: OptoutService = new OptoutService()

}
