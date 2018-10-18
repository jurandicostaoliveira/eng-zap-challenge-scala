package br.com.btg360.run

import br.com.btg360.application.Entity
import br.com.btg360.entities.{ConsolidatedEntity, QueueEntity}
import br.com.btg360.repositories.{ConsolidatedRepository, ProductRepository}
import br.com.btg360.rules.Daily
import org.apache.spark.rdd.RDD
import br.com.btg360.services.DataStockService
import br.com.btg360.spark.SparkCoreSingleton


object ConsolidatedRun extends App {

  val consolidatedRepository = new ConsolidatedRepository()

  //  val rdd = consolidatedRepository.table("btg_consolidated.navigation_daily_8232_2975_2018_09_04_email").findAll
  //  val pairRdd : RDD[(String, Iterable[ConsolidatedEntity])] = rdd.keyBy(_.userSent).groupByKey()
  //  pairRdd.foreach(row => {
  //    println(">>>>>> " + row._1)
  //    row._2.foreach(p => {
  //      println(p.productId)
  //    })
  //  })


  //Editora Harlequin Books = [ id = 17,  btg = 8232, allin = 9176 ]

  //  val rdd = consolidatedRepository.table("btg_consolidated.navigation_daily_8232_2975_2018_09_04_email")
  //    .findAllKeyBy(f => (f.productId, f))
  //
  //
  //  rdd.foreach(row => {
  //    println(row._1 + " -> "+ row._2.userSent)
  //  })


  //Cassandra
  //  val productRepository = new ProductRepository()

  //Glamour [50, 36, 52]
  //  val rdd = productRepository.table("product_52").findAllKeyBy(entity => (entity.productId, entity))

  //  rdd.foreach(row => {
  //    println(row._1 + " -> "+ row._2.subCategory)
  //  })


  val queue = new QueueEntity()
  queue.userId = 17
  queue.allinId = 9176
  queue.consolidatedTable = "navigation_daily_8232_2975_2018_09_04"
  queue.channelName = "email"

  val rdd = new DataStockService(queue).get
  rdd.foreach(row => {
    println(row._1 + " -> Products: " + row._2.products.size + " Recomendations: " + row._2.recommendations.size)
  })

}
