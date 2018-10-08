package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.repositories.OptoutRepository
import br.com.btg360.spark.SparkContextSingleton
import org.apache.spark.rdd.RDD

class OptoutService extends Service {

  val sc = SparkContextSingleton.getSparkContext()

  val repository = new OptoutRepository()

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