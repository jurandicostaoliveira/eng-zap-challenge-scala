package br.com.btg360.run

import br.com.btg360.services.OptoutService
import br.com.btg360.spark.SparkContextSingleton

object OptoutExample extends App {

  val sc = SparkContextSingleton.getSparkContext()

  val optoutService: OptoutService = new OptoutService()
  val usersList = List("mariana@test.com", "1956claudia@globo.com", "paula@test.com", "3terni@gmail.com", "carla@test.com", "solange@test.com")
  val users = sc.parallelize(usersList)
  val rdd = optoutService.filter(6878, users)
  rdd.foreach(row => println(row))

}
