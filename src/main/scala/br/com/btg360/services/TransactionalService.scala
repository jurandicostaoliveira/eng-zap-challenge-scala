package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.constants.Message
import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.repositories.TransactionalRepository
import org.apache.spark.rdd.RDD

class TransactionalService() extends Service {

  private val repository = new TransactionalRepository()

  /**
    * @param QueueEntity queue
    * @param RDD data
    */
  def persist(queue: QueueEntity, data: RDD[(String, StockEntity)]): Unit = {
    try {
      queue.rule.transactionalId = 1111
      this.repository
        .queue(queue)
        .data(data)
        .templateId(0)
        .createSendTable
        .saveSend

      println(Message.TRANSACTIONAL_SUCCESS)
    } catch {
      case e: Exception => println(e.printStackTrace())
        println(Message.TRANSACTIONAL_ERROR)
    }
  }

}
