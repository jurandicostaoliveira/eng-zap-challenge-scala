package br.com.btg360.spark

import java.util.Properties

import br.com.btg360.constants.Environment
import br.com.btg360.services.PropService
import com.datastax.driver.core.Session
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.{SparkConf, SparkContext}

object SparkContextSingleton {

  private var CONTEXT: SparkContext = _

  private val fileName = "cassandra-%s.properties".format(Environment.getAppEnv)

  def getSparkConf(): SparkConf = {
    val p: Properties = new PropService().get(fileName)
    val sparkConf = new SparkConf().setMaster(p.getProperty("master"))
      .setAppName(p.getProperty("appName"))
      .set("spark.cassandra.connection.host", p.getProperty("host"))
      .set("spark.cassandra.auth.username", p.getProperty("username"))
      .set("spark.cassandra.auth.password", p.getProperty("password"))
      .set("spark.driver.memory", p.getProperty("sparkDriverMemory"))
      .set("spark.executor.memory", p.getProperty("sparkExecutorMemory"))
      .set("spark.driver.allowMultipleContexts", p.getProperty("sparkDriverAllowMultipleContexts"))
      .set("spark.cassandra.connection.compression", p.getProperty("sparkCassandraConnectionCompression"))
      .set("spark.ui.port", p.getProperty("sparkUiPort"))
      .set("spark.cassandra.connection.timeout_ms", p.getProperty("sparkCassandraConnectionTimeoutMs"))
      .set("spark.network.timeout", p.getProperty("sparkNetworkTimeout"))
      .set("spark.cassandra.input.consistency.level", p.getProperty("sparkCassandraInputConsistencyLevel"))

    if (Environment.isDevelopment) {
      sparkConf.set("spark.local.dir", p.getProperty("sparkLocalDir"))
    }

    sparkConf
  }

  /**
    * this.synchronized is to resolve CONTEXT => null in concurrence
    *
    * @return
    */
  def getSparkContext(): SparkContext = this.synchronized {
    if (CONTEXT == null) {
      CONTEXT = new SparkContext(getSparkConf)
    }
    CONTEXT
  }

  def destroyContext(): Unit = {
    if (CONTEXT != null) {
      CONTEXT.clearJobGroup()
      CONTEXT = null
    }
  }

  def getSession(): Session = {
    try {
      val connector = CassandraConnector.apply(getSparkConf())
      connector.openSession()
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
        null
    }
  }

}
