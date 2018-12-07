package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.entities.{QueueEntity, StockEntity}
import org.apache.spark.rdd.RDD

class TransactionalService(
                            queue: QueueEntity,
                            data: RDD[(String, StockEntity)]
                          ) extends Service {

  //private val repository = new TransactionalRepository()

  def persist: Unit = {

  }

}
