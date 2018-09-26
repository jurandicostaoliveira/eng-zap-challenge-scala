package br.com.btg360.run

import br.com.btg360.services.RedisClientService

object RedisExample extends App {
  val redis = new RedisClientService().connect
  redis.set("btgId", "768HU@")
  val btgId = redis.get("btgId")
  println(btgId)
}
