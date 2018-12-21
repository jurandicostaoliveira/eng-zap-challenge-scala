package br.com.btg360.redis

import br.com.btg360.constants.Environment
import br.com.btg360.services.PropService
import com.redis._

class Connection {

  private val fileName = "redis-%s.properties".format(Environment.getAppEnv)

  def get: RedisClient = {
    try {
      val p = new PropService().get(fileName)
      new RedisClient(p.getProperty("host"), p.getProperty("port").toInt, p.getProperty("database").toInt)
    } catch {
      case e: Exception => println("REDIS CONNECTION ERROR: " + e.printStackTrace())
        new RedisClient()
    }
  }

}
