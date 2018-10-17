package br.com.btg360.services

import br.com.btg360.entities._
import br.com.btg360.repositories.{ConsolidatedRepository, ProductRepository}
import org.apache.spark.rdd.RDD
import scala.collection.mutable.Map

class DataStockService {

  private var _queue: QueueEntity = _

  /**
    * Getter
    *
    * @return QueueEntity
    */
  def queue: QueueEntity = this._queue

  /**
    * Setter
    *
    * @param QueueEntity value
    * @return this
    */
  def queue(value: QueueEntity): DataStockService = {
    this._queue = value
    this
  }

  /**
    * Return all consolidated data
    *
    * @return RDD
    */
  private def consolidatedData: RDD[(Any, ConsolidatedEntity)] = {
    new ConsolidatedRepository().table(this.queue.getConsolidatedTable).findAllKeyBy(
      entity => (entity.productId, entity)
    )
  }

  /**
    * Return all product data
    *
    * @return RDD
    */
  private def productData: RDD[(Any, ProductEntity)] = {
    new ProductRepository().table(this.queue.getProductTable).findAllKeyBy(
      entity => (entity.productId, entity)
    )
  }

  /**
    * Return the merged data
    *
    * @return
    */
  private def joinData: RDD[(String, ConsolidatedProductEntity)] = {
    this.consolidatedData.join(this.productData).map(row => {
      (row._2._1.userSent, new ConsolidatedProductEntity().setRow(row._2._1, row._2._2))
    })
  }

  /**
    * Returns the grouped data
    *
    * @return
    */
  def groupData: RDD[Map[String, ItemEntity]] = {
    this.joinData.groupByKey().map(rows => {
      val data: Map[String, ItemEntity] = Map()
      val entity: ItemEntity = new ItemEntity()

      rows._2.foreach(row => {
        if (row.isRecommendation.equals(1)) {
          entity.addRecommendations(row)
        } else {
          entity.addProducts(row)
        }
      })

      data(rows._1) = entity
      (data)
    })
  }


  def get: RDD[(String, ConsolidatedProductEntity)] = {
    try {
      val groupData = this.groupData
//      val rddKeys: RDD[String] = groupData.map(row => {
//        var list: List[String] = List()
//        for ((key, value) <- row) {
//          list ::= key
//        }
//        list
//      })

      //LOG TOTAL
      val dailyUnlimitedData = new DailySendLimitService().filter(RDD[String])
      //LOG TOTAL

      dailyUnlimitedData.foreach(row => println(row))

      null
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
        null
    }
  }


}
