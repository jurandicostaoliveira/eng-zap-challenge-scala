package br.com.btg360.services

import br.com.btg360.entities.QueueEntity
import br.com.btg360.redis.Connection
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD


class DailySendLimitService(queue: QueueEntity) {

  private val sc = SparkCoreSingleton.getContext

  private val redis = new Connection().get

  private val today = new PeriodService("yyyy_MM_dd").now

  /**
    * @return String
    */
  private def createEntityName: String = "send_limit_%d_%s".format(this.queue.userId, this.today)

  /**
    * Filtering to start the defined limit
    *
    * @param String entityName
    * @return RDD
    */
  private def pluck(entityName: String, users: List[String]): RDD[String] = {
    var keys: List[String] = List()

    this.redis.hmget(entityName, users: _*).foreach(rows => {
      for ((key, value) <- rows) {
        if (value.toInt <= this.queue.sendLimit) {
          keys = keys :+ key
        }
      }
    })

    this.sc.parallelize(keys)
  }

  /**
    * Filtration
    *
    * @param RDD users
    * @return RDD
    */
  def filter(users: RDD[String]): RDD[String] = {
    try {
      val entityName: String = this.createEntityName
      val usersList = users.collect().toList

      this.redis.pipeline(pipe => {
        for (key <- usersList) {
          pipe.hincrby(entityName, key, 1)
        }
      })

      this.pluck(entityName, usersList)
    } catch {
      case e: Exception => println("DAILY SEND LIMIT ERROR: " + e.printStackTrace())
        users
    }
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


