package br.com.btg360.run

import br.com.btg360.services.RedisClientService

object RedisExample extends App {
  val redis = new RedisClientService().connect

  val keyName = "scala_768"
  var users: List[String] = List("test1@test.com", "TEST2@test.com", "test3@test.com", "test4@test.com", "test5@test.com", "rh10@test.com")
  var limit: Int = 2

  redis.pipeline(row => {
    for (key <- users) {
      row.hincrby(keyName, key.toLowerCase(), 1)
    }
  })

  var keys: Option[Map[String, String]] = redis.hgetall(keyName)

  var refreshKeys: List[String] = List()

  keys.foreach(row => {
    for ((k, v) <- row) {
      if (v.toInt <= limit) {
        refreshKeys = k :: refreshKeys
      }
    }
  })

  println(refreshKeys)

}
