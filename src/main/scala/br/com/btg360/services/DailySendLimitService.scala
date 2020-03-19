package br.com.btg360.services

import br.com.btg360.entities.QueueEntity
import br.com.btg360.redis.Connection
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD
import com.redislabs.provider.redis._


class DailySendLimitService(queue: QueueEntity) extends Serializable {

  private val redis = new Connection().get

  private val today = new PeriodService("yyyy_MM_dd").now

  /**
    * @return String
    */
  private def createKeyName: String = "send_limit_%d_%s".format(this.queue.userId, this.today)

  /**
    * Filtering to start the defined limit
    *
    * @param String keyName
    * @return RDD
    */
  private def diff(keyName: String, users: RDD[String]): RDD[String] = {
    val inputData: RDD[(String, String)] = users.keyBy(row => row)

    val sendLimit: Int = this.queue.sendLimit

    val currentData: RDD[(String, String)] = SparkCoreSingleton.getContext
      .fromRedisHash(keyName)
      .filter(row => row._2.toInt <= sendLimit)

    inputData.join(currentData).keys
  }

  /**
    * Filtration
    *
    * @param RDD users
    * @return RDD
    */
  def filter(users: RDD[String]): RDD[String] = {
    try {
      val keyName: String = this.createKeyName
      val usersList = users.collect().toList

      this.redis.pipeline(pipe => {
        usersList.foreach(user => {
          pipe.hincrby(keyName, user, 1)
        })
      })

      this.diff(keyName, users)
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


