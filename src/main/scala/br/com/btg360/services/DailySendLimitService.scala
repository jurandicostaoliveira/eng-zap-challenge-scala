package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD


class DailySendLimitService extends Service {

  private val sc = SparkCoreSingleton.getContext

  private val redis = this.redisClientService.connect

  private val today = this.periodService("yyyy_MM_dd").now

  private var _userId: Int = _

  private var _limit: Int = 1

  /**
    * Getter
    *
    * @return Int
    */
  def userId: Int = _userId

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def userId_=(value: Int): DailySendLimitService = {
    this._userId = value
    this
  }

  /**
    * Getter
    *
    * @return Int
    */
  def limit: Int = this._limit

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def limit_=(value: Int): DailySendLimitService = {
    this._limit = value
    this
  }

  /**
    * @return String
    */
  private def createEntityName: String = "send_limit_%d_%s".format(this._userId, this.today)

  /**
    * Filtering to start the defined limit
    *
    * @param String entityName
    * @return List
    */
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

  /**
    * Filtration
    *
    * @param RDD users
    * @return RDD
    */
  def filter(users: RDD[String]): RDD[String] = {
    val entityName: String = this.createEntityName
    val usersList = users.collect().toList
    this.redis.pipeline(row => {
      for (key <- usersList) {
        row.hincrby(entityName, key, 1)
      }
    })

    this.sc.parallelize(this.pluck(entityName))
  }

  /**
    * Process to delete keys that are not from the current day
    */
  def destroyNotCurrent: Unit = {
    this.redis.keys("*").toList.flatMap(_.filter(key => !key.get.contains(this.today))).foreach(row => {
      this.redis.del(row.get)
    })
  }

}


