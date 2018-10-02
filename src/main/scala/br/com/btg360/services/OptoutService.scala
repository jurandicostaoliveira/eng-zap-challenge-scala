package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.spark.SparkContextSingleton
import org.apache.spark.rdd.RDD

class OptoutService extends Service {

  val sc = SparkContextSingleton.getSparkContext()

  def filter(users: List[String] = List()): List[String] = {
    val opts = List("ana@test.com", "paula@test.com", "sandra@test.com", "carla@test.com")
    val optsRDD : RDD[String] = sc.parallelize(opts)
    val usersRDD : RDD[String] = sc.parallelize(users)
    val rdd : RDD[String] = usersRDD.subtract(optsRDD)

    rdd.foreach(row => {
      println(row)
    })

    users
  }

}
