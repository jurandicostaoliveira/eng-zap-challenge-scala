package br.com.zap.spark

import org.apache.spark.SparkContext
import org.apache.spark.sql.{SQLContext, SparkSession}

object SparkConfig {

  def getSession: SparkSession = {
    SparkSession.builder()
      .master("local[*]")
      .appName("Eng-Zap-Challenge-Scala")
      .getOrCreate()
  }

  def getContext: SparkContext = {
    getSession.sparkContext
  }

  def getSql: SQLContext = {
    getSession.sqlContext
  }

}
