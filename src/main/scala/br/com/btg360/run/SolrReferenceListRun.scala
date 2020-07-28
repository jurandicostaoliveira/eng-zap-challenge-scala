package br.com.btg360.run

import br.com.btg360.entities.StockEntity
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

      val rf = new SolrReferenceListService().allinId(164).listId(5415607)
      val result = rf.add(data, true)
      result.foreach(row => println("RESULT >>>>>>> " + row._1 + " ==> " + row._2))

    } catch {
      case e: Exception => println(e)
    }


    sc.stop()
  }


}
