package br.com.btg360.run

import br.com.btg360.spark.SparkCoreSingleton

object OptoutExample extends App {

  val sc = SparkCoreSingleton.getContext

//  val optoutService: OptoutService = new OptoutService()
//  val usersList = List("mariana@test.com", "1956claudia@globo.com", "paula@test.com", "3terni@gmail.com", "carla@test.com", "solange@test.com")
//  val users = sc.parallelize(usersList)
//  val rdd = optoutService.filter(users)
//  rdd.foreach(row => println(row))

}
