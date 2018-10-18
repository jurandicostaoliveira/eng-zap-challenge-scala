package br.com.btg360.services

import com.redis._
import br.com.btg360.constants.Environment

class RedisClientService {

  private val fileName = "redis-%s.properties".format(Environment.getAppEnv)

  def connect: RedisClient = {
    try {
      val p = new PropService().get(fileName)
      new RedisClient(p.getProperty("host"), p.getProperty("port").toInt, p.getProperty("database").toInt)
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
        new RedisClient()
    }
  }

}
