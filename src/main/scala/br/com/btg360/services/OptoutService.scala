package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.repositories.OptoutRepository
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD

class OptoutService extends Service {

  val sc = SparkCoreSingleton.getContext

  val repository = new OptoutRepository()

  /**
    * Returns users by removing anyone on the optout list
    *
    * @param Int allinId
    * @param RDD users
    * @return
    */
  def filter(allinId: Int, users: RDD[String]): RDD[String] = {
    try {
      val optouts = this.repository.allinId(allinId).getEmails
      users.subtract(optouts)
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
        this.sc.emptyRDD
    }
  }

}