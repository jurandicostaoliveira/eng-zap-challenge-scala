package br.com.btg360.redis

import br.com.btg360.constants.Environment
import br.com.btg360.services.PropService
import com.redis._

import scala.util.Try

class Connection {

  private val fileName = "redis-%s.properties".format(Environment.getAppEnv)

  def get: RedisClient = {
    try {
      val p = new PropService().get(fileName)
      new RedisClient(
        p.getProperty("host"),
        p.getProperty("port").toInt,
        p.getProperty("database").toInt,
        Try(p.getProperty("secret")).toOption,
        p.getProperty("timeout").toInt
      )
    } catch {
      case e: Exception => println("REDIS CONNECTION ERROR: " + e.printStackTrace())
        new RedisClient()
    }
  }

}
