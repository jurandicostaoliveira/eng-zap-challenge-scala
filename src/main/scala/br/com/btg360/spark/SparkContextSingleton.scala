package br.com.btg360.spark

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object SparkContextSingleton {

  private var CONTEXT: SparkContext = _

  def getSparkConf(): SparkConf = {
    return new SparkConf().setAppName("Spark Scala").setMaster("local[*]")
                 .set("spark.cassandra.connection.host", "34.226.183.71,52.44.58.51")//host
      					 .set("spark.cassandra.auth.username", "cassandra")//login
      					 .set("spark.cassandra.auth.password", "cassandra")//password
  }

  def getSparkContext(): SparkContext = {
    if (CONTEXT == null) {
      CONTEXT = new SparkContext(getSparkConf())
    }
    return CONTEXT
  }
}
