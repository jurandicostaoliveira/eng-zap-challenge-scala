package br.com.btg360.run

import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.services.SolrReferenceListService
import br.com.btg360.spark.SparkCoreSingleton

object SolrReferenceListRun {

  def main(args: Array[String]): Unit = {

    val sc = SparkCoreSingleton.getContext

    val data = SparkCoreSingleton.getContext.parallelize(Seq(
      "saudadedobaleal@yahoo.com" -> new StockEntity(),
      "julianaribeiro_21@outlook.com" -> new StockEntity(),
      "camilapaulagomes145@gmail.com" -> new StockEntity()
    ))

    try {

      val rf: SolrReferenceListService = new SolrReferenceListService()
      val result = rf.add(new QueueEntity, data)
      result.foreach(row => println("RESULT >>>>>>> " + row._1 + " ==> " + row._2))

    } catch {
      case e: Exception => println(e)
    }


    sc.stop()
  }


}
