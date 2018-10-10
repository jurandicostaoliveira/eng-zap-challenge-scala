package br.com.btg360.run

import br.com.btg360.entities.ConsolidatedEntity
import br.com.btg360.repositories.{ConsolidatedRepository, ProductRepository}
import br.com.btg360.traits.ConsolidatedKeyByTrait
import org.apache.spark.rdd.RDD

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
//    .findAllKeyBy(new ConsolidatedKeyByTrait {
//      /**
//        * Create a pair dynamically
//        *
//        * @param ConsolidatedEntity entity
//        * @return (?, ConsolidatedEntity)
//        */
//      override def call(entity: ConsolidatedEntity): (String, ConsolidatedEntity) = {
//        (entity.productId, entity)
//      }
//    })
//
//  rdd.foreach(row => {
//    println(row._1 + " -> " + row._2)
//  })


  //Cassandra
  val productRepository = new ProductRepository()

  productRepository.table("product_9176").findAll


}
