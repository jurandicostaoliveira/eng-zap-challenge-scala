package br.com.btg360.services

import br.com.btg360.application.Service

class DailySendLimitService extends Service {

  private val redis = this.redisClientService.connect

  private val today = this.periodService("yyyy_MM_dd").now

  private var _userId: Int = _

  private var _limit: Int = 1

  def userId: Int = _userId

  def userId_=(value: Int): DailySendLimitService = {
    this._userId = value
    this
  }

  def limit: Int = this._limit

  def limit_=(value: Int): DailySendLimitService = {
    this._limit = value
    this
  }

  private def createEntityName: String = "send_limit_%d_%s".format(this._userId, this.today)

  private def pluck(entityName: String): List[String] = {
    var keys: List[String] = List()
    this.redis.hgetall(entityName).foreach(row => {
      for ((key, value) <- row) {
        if (value.toInt <= this._limit) {
          keys ::= key
        }
      }
    })

    keys
  }

  def filter(users: List[String] = List()): List[String] = {
    val entityName: String = this.createEntityName
    this.redis.pipeline(row => {
      for (key <- users) {
        row.hincrby(entityName, key, 1)
      }
    })

    this.pluck(entityName)
  }

  def destroyNotCurrent: Unit = {
    this.redis.keys("*").toList.flatMap(_.filter(key => !key.get.contains(this.today))).foreach(row => {
      this.redis.del(row.get)
    })
  }

}


