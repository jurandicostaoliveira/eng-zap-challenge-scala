package br.com.btg360.services

import br.com.btg360.constants.{Channel, Message}
import br.com.btg360.entities._
import br.com.btg360.repositories.{ConsolidatedRepository, ProductRepository}
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD

class DataStockService(queue: QueueEntity) {

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
  private def groupData: RDD[(String, ItemEntity)] = {
    this.joinData.groupByKey().map(rows => {
      val entity: ItemEntity = new ItemEntity()

      rows._2.foreach(row => {
        if (row.isRecommendation.equals(1)) {
          entity.addRecommendations(row)
        } else {
          entity.addProducts(row)
        }
      })

      (rows._1, entity)
    })
  }

  /**
    * Return filtered data
    *
    * @return RDD
    */
  def get: RDD[(String, ItemEntity)] = {
    try {
      val data = this.groupData
      println(Message.TOTAL_ITEMS_FOUND.format(data.count()))

      var filters = new DailySendLimitService(this.queue).filter(groupData.keys)
      println(Message.TOTAL_DAILY_LIMIT_REMOVED.format(filters.count()))

      if (Channel.isEmailChannel(this.queue.channelName)) {
        filters = new OptoutService(this.queue).filter(filters)
        println(Message.TOTAL_OPTOUT_REMOVED.format(filters.count()))
      }

      filters.map(key => (key, 0)).join(data).map(row => {
        (row._1, row._2._2)
      })
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
        SparkCoreSingleton.getContext.emptyRDD
    }
  }

}
