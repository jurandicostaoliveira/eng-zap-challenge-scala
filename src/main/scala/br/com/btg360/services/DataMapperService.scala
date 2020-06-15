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

    if (!repository.tableExists(table)) {
      println(Message.CONSOLIDATED_TABLE_NOT_FOUND.format(table))
      return null
    }

    println(Message.CONSOLIDATED_TABLE_NAME.format(table))
    repository.table(table)
      .findAll(this.queue.platformId)
      .keyBy(row => row.userSent + "-" + row.productId)
      .reduceByKey((entity1, entity2) => entity1)
      .map(row => (row._2.productId, row._2))
  }

  /**
    * Return all product data
    *
    * @return RDD
    */
  private def productData: RDD[(Any, ProductEntity)] = {
    val repository = new ProductRepository()
    val table = this.queue.getProductTable
    val keyspaceAndTable = "%s.%s".format(Keyspace.BTG360, table)

    if (!repository.cassandraTableExists(keyspaceAndTable)) {
      println(Message.PRODUCT_TABLE_NOT_FOUND.format(keyspaceAndTable))
      return null
    }

    println(Message.PRODUCT_TABLE_NAME.format(keyspaceAndTable))
    repository.table(table).findAllKeyBy(
      entity => (entity.productId.getOrElse(""), entity)
    )
  }

  /**
    * Return the merged data
    *
    * @return
    */
  private def join: RDD[(String, JsonProductEntity)] = {
    val consolidatedData = this.consolidatedData
    val productData = this.productData
    if (consolidatedData == null || productData == null) {
      return null
    }

    consolidatedData.join(productData).map(row => {
      (row._2._1.userSent, new StockEntity().toJsonProduct(row._2._1, row._2._2))
    })
  }

  /**
    * Returns the grouped data
    *
    * @return
    */
  def get: RDD[(String, StockEntity)] = {
    try {
      val join = this.join
      if (join == null) {
        return null
      }

      val group = join.groupByKey().map(rows => {
        var products: List[JsonProductEntity] = List()
        var recommendations: List[JsonProductEntity] = List()

        rows._2.foreach(row => {
          val product = row.copy(productLink = new UrlService().redirect(
            this.queue,
            TC.toString(row.productLink),
            HtmlPosition.CONTENT,
            TC.toString(row.productId),
            TC.toInt(row.isRecommendation),
            rows._1
          ))

          if (product.isRecommendation.equals(1)) {
            recommendations = recommendations :+ product
          } else {
            products = products :+ product
          }
        })

        (rows._1, new StockEntity(products, recommendations))
      })

      val data = group.filter(row => row._2.products.size > 0)
      println(Message.TOTAL_ITEMS_FOUND.format(data.count()))
      data
    } catch {
      case e: Exception => println(e.printStackTrace())
        SparkCoreSingleton.getContext.emptyRDD[(String, StockEntity)]
    }
  }

}
