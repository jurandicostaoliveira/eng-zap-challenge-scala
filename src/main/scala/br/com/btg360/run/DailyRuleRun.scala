package br.com.btg360.run

import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD

import scala.collection.mutable.HashMap
import br.com.btg360.logger.{Printer => P}

import br.com.btg360.worker.rule.Daily

object DailyRuleRun extends App {

  new Daily().dispatch(17)

//  val rdd: RDD[(String, Int)] = SparkCoreSingleton.getContext.parallelize(HashMap("email1" -> 1, "email2" -> 2).toSeq)
//  var log = new P().inFile("storage/lala.log", maxBackupIndex = 1)
//
//
//  rdd.map(row => {
//    log.warn("LALA " + row._1)
//  }).foreach(println(_))
//
//  log = new P().inFile("storage/lele.log", maxBackupIndex = 1)
//
//  rdd.map(row => {
//    log.warn("LELE " + row._1)
//  }).foreach(println(_))


}


