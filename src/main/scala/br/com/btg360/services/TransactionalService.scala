package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.logger.Log4jPrinter
import br.com.btg360.repositories.TransactionalRepository
import org.apache.spark.rdd.RDD

class TransactionalService(
                            queue: QueueEntity,
                            data: RDD[(String, StockEntity)]
                          ) extends Service {

  private val repository = new TransactionalRepository()

  def persist: Unit = {
    this.data.foreach(row => {
      Log4jPrinter.get.warn(row._1 + " -> " + new JsonService().encode(row._2))
      Log4jPrinter.get.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
    })
  }

}
