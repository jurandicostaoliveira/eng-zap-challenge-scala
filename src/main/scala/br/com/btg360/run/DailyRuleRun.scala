package br.com.btg360.run

import br.com.btg360.worker.rule.Daily

import scala.collection.mutable.HashMap

object DailyRuleRun extends App {

  new Daily().dispatch(17)


  //  val rdd: RDD[(String, Int)] = SparkCoreSingleton.getContext.parallelize(HashMap("email1" -> 1, "email2" -> 2).toSeq)
  //  Log4jPrinter.configure("storage/lala.log", maxBackupIndex = 1)
  //
  //
  //  rdd.map(row => {
  //    Log4jPrinter.get.warn("LALA " + row._1)
  //  }).foreach(println(_))
  //
  //  Log4jPrinter.configure("storage/lele.log", maxBackupIndex = 1)
  //
  //  rdd.map(row => {
  //    Log4jPrinter.get.warn("LELE " + row._1)
  //  }).foreach(println(_))


}


