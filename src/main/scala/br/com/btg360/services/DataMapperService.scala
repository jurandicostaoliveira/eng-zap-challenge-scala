package br.com.btg360.services

import br.com.btg360.constants.{Channel, Message}
import br.com.btg360.entities._
import br.com.btg360.repositories.{ConsolidatedRepository, ProductRepository}
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD

import scala.collection.mutable.HashMap

class DataMapperService(queue: QueueEntity) {

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
  private def join: RDD[(String, HashMap[String, Any])] = {
    val positions: HashMap[String, Int] = HashMap()
    val data = this.consolidatedData.join(this.productData).map(row => {
      val key = "%s-%d".format(row._2._1.userSent, row._2._1.isRecommendation)
      positions(key) = if (positions.contains(key)) positions(key) else 0
      val tuple = (row._2._1.userSent, new StockEntity().toMap(
        this.queue,
        row._2._1,
        row._2._2,
        positions(key)
      ))

      positions(key) += 1
      tuple
    })

    positions.clear()
    data
  }

  /**
    * Returns the grouped data
    *
    * @return
    */
  private def group: RDD[(String, StockEntity)] = {
    this.join.groupByKey().map(rows => {
      var products: List[HashMap[String, Any]] = List()
      var recommendations: List[HashMap[String, Any]] = List()

      rows._2.foreach(row => {
        if (row("isRecommendation").equals(1)) {
          recommendations = recommendations :+ row
        } else {
          products = products :+ row
        }
      })

      (rows._1, new StockEntity(products, recommendations))
    })
  }

  /**
    * Return filtered data
    *
    * @return RDD
    */
  def get: RDD[(String, StockEntity)] = {
    try {
      val data = this.group
      println(Message.TOTAL_ITEMS_FOUND.format(data.count()))

      var filters = new DailySendLimitService(this.queue).filter(data.keys)
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
