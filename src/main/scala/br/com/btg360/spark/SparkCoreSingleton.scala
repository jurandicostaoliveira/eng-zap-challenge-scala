package br.com.btg360.spark

import java.util.Properties

import br.com.btg360.constants.Environment
import br.com.btg360.services.PropService
import com.datastax.driver.core.Session
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext

object SparkCoreSingleton extends Serializable {

  private val fileName = "cassandra-%s.properties".format(Environment.getAppEnv)

  private val sparkContext: SparkContext = this.getSparkSession.sparkContext

  /**
    *
    * @return SparkSession
    */
  private def getSparkSession: SparkSession = {
    val p: Properties = new PropService().get(this.fileName)
    val builder: SparkSession.Builder = SparkSession.builder()

    return builder
      .appName(p.getProperty("appName"))
      .config("spark.cassandra.connection.host", p.getProperty("host"))
      .config("spark.cassandra.auth.username", p.getProperty("username"))
      .config("spark.cassandra.auth.password", p.getProperty("password"))
      .config("spark.cassandra.connection.compression", p.getProperty("sparkCassandraConnectionCompression"))
      .getOrCreate()
  }

  /**
    * this.synchronized is to resolve CONTEXT => null in concurrence
    *
    * @return
    */
  def getContext: SparkContext = {
    this.sparkContext.setLocalProperty("spark.scheduler.pool", "production")
    this.sparkContext
  }

  /**
    * Get cassandra session
    *
    * @return
    */
  def getSession: Session = {
    try {
      val connector = CassandraConnector.apply(this.getSparkSession.sparkContext.getConf)
      connector.openSession()
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
        null
    }
  }

  /**
    * Destroy and clean the context
    */
  def destroyContext: Unit = {
    this.sparkContext.clearJobGroup()
    this.sparkContext.clearCallSite()
  }

}
