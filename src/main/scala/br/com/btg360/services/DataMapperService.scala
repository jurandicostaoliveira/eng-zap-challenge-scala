package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.constants.{HtmlPosition, Keyspace, Message, TypeConverter => TC}
import br.com.btg360.entities._
import br.com.btg360.repositories.{ConsolidatedRepository, ProductRepository}
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD

import scala.collection.mutable.HashMap

class DataMapperService(queue: QueueEntity) extends Service with Serializable {

  /**
    * Return all consolidated data
    *
    * @return RDD
    */
  private def consolidatedData: RDD[(Any, ConsolidatedEntity)] = {
    val repository = new ConsolidatedRepository()
    val table = this.queue.getConsolidatedTable
    println(Message.CONSOLIDATED_TABLE_NAME.format(table))

    if (!repository.tableExists(table)) {
      println(Message.CONSOLIDATED_TABLE_NOT_FOUND.format(table))
      return SparkCoreSingleton.getContext.emptyRDD[(Any, ConsolidatedEntity)]
    }

    repository.table(table).findAllKeyBy(
      entity => (entity.productId, entity), this.queue.platformId
    )
  }

  /**
    * Return all product data
    *
    * @return RDD
    */
  private def productData: RDD[(Any, ProductEntity)] = {
    val table = this.queue.getProductTable
    println(Message.PRODUCT_TABLE_NAME.format(Keyspace.BTG360 + "." + table))
    new ProductRepository().table(table).findAllKeyBy(
      entity => (entity.productId, entity)
    )
  }

  /**
    * Return the merged data
    *
    * @return
    */
  private def join: RDD[(String, HashMap[String, Any])] = {
    this.consolidatedData.join(this.productData).map(row => {
      (row._2._1.userSent, new StockEntity().toMap(row._2._1, row._2._2))
    })
  }

  /**
    * Returns the grouped data
    *
    * @return
    */
  def get: RDD[(String, StockEntity)] = {
    try {
      val data = this.join.groupByKey().map(rows => {
        var products: List[HashMap[String, Any]] = List()
        var recommendations: List[HashMap[String, Any]] = List()
        var position: Int = 0

        rows._2.foreach(row => {
          row("productLink") = new UrlService().redirect(
            this.queue,
            TC.toString(row("productLink")),
            HtmlPosition.CONTENT,
            TC.toString(row("productId")),
            TC.toInt(row("isRecommendation")),
            rows._1
          )

          if (row("isRecommendation").equals(1)) {
            recommendations = recommendations :+ row
          } else {
            products = products :+ row
          }
          position += 1
        })

        (rows._1, new StockEntity(products, recommendations))
      })

      println(Message.TOTAL_ITEMS_FOUND.format(data.count()))
      data
    } catch {
      case e: Exception => println(e.printStackTrace())
        SparkCoreSingleton.getContext.emptyRDD[(String, StockEntity)]
    }
  }

}
