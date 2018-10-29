package br.com.btg360.services

import br.com.btg360.entities.QueueEntity
import br.com.btg360.repositories.OptoutRepository
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD

class OptoutService(queue: QueueEntity) {

  val sc = SparkCoreSingleton.getContext

  val repository = new OptoutRepository()

  /**
    * Returns users by removing anyone on the optout list
    *
    * @param RDD users
    * @return
    */
  def filter(users: RDD[String]): RDD[String] = {
    try {
      val usersOptout = this.repository.allinId(this.queue.rule.allinId).getEmails
      users.subtract(usersOptout)
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
        this.sc.emptyRDD
    }
  }

}