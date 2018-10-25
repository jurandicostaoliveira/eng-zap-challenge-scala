package br.com.btg360.services

import br.com.btg360.entities.QueueEntity
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD


class DailySendLimitService(queue: QueueEntity) {

  private val sc = SparkCoreSingleton.getContext

  private val redis = new RedisClientService().connect

  private val today = new PeriodService("yyyy_MM_dd").now

  /**
    * @return String
    */
  private def createEntityName: String = "send_limit_%d_%s".format(this.queue.userId, this.today)

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
        if (value.toInt <= this.queue.sendLimit) {
          keys = keys :+ key
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


