package br.com.btg360.run

import com.redis._

object RedisExample extends App {
  val redis = new RedisClient(database = 0, port=32768, host="0.0.0.0")
  redis.set("btgId", "768")
  val btgId = redis.get("btgId")
  println(btgId)
}
