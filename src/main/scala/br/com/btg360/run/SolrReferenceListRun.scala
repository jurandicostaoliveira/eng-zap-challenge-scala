package br.com.btg360.run

import br.com.btg360.entities.StockEntity
import br.com.btg360.services.SolrReferenceListService
import br.com.btg360.spark.SparkCoreSingleton

object SolrReferenceListRun {

  def main(args: Array[String]): Unit = {

    val sc = SparkCoreSingleton.getContext

    val data = SparkCoreSingleton.getContext.parallelize(Seq(
      "valmiraf4@gmail.com" -> new StockEntity(),
      "tatianaremonti@terra.com.br" -> new StockEntity(),
      "teste@teste.com" -> new StockEntity(),
      "aah.juju@gmail.com" -> new StockEntity()
    ))

    try {

      val rf = new SolrReferenceListService().allinId(240).listId(3128475)
      val result = rf.add(data)
      result.foreach(row => println("RESULT >>>>>>> " + row._1 + " ==> " + row._2))

    } catch {
      case e: Exception => println(e)
    }


    sc.stop()
  }


}
