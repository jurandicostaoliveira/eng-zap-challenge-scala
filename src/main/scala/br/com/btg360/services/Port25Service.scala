package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.entities.StockEntity
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD

class Port25Service extends Service {

  private val sc = SparkCoreSingleton.getContext

  private val email: String = "porta25@allin.com.br"

  /**
    * Apply the email to send the account port 25
    *
    * @param RDD data
    * @return RDD
    */
  def add(data: RDD[(String, StockEntity)]): RDD[(String, StockEntity)] = {
    if (!data.isEmpty) {
      return data.union(this.sc.parallelize(Seq((this.email, data.first()._2))))
    }

    data
  }

}
