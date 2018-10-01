package br.com.btg360.services

class DailySendLimitService {

  val redis = new RedisClientService().connect

  private var _userId: Int = _

  private var _limit: Int = 1

  def userId: Int = _userId

  def userId_=(value: Int): Unit = {
    _userId = value
  }

  def limit: Int = _limit

  def limit_=(value: Int): Unit = {
    _limit = value
  }

  private def keyName: String = {
    "send_limit_%d_%s".format(this._userId, "YYYY_MM_DD")
  }

  private def pluck: List[String] = {
    var keys: List[String] = List()
    redis.hgetall(this.keyName).foreach(row => {
      for ((key, value) <- row) {
        if (value.toInt <= limit) {
          keys = key :: keys
        }
      }
    })

    keys
  }

  def filter(users: List[String] = List()): List[String] = {
    redis.pipeline(row => {
      for (key <- users) {
        row.hincrby(this.keyName, key.toLowerCase(), 1)
      }
    })

    this.pluck
  }

}


