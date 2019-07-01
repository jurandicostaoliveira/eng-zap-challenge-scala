package br.com.btg360.spark

import java.util.Properties

import br.com.btg360.constants.Environment
import br.com.btg360.services.PropService
import com.datastax.driver.core.Session
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext

object SparkCoreSingleton extends Serializable {

  private var sparkContext: SparkContext = _

  private val fileName = "cassandra-%s.properties".format(Environment.getAppEnv)

  //  /**
  //    * Return the settings
  //    *
  //    * @return SparkConf
  //    */
  //  private def getConf: SparkConf = {
  //    val p: Properties = new PropService().get(fileName)
  //    val sparkConf = new SparkConf().setMaster(p.getProperty("master"))
  //      .setAppName(p.getProperty("appName"))
  //      .set("spark.cassandra.connection.host", p.getProperty("host"))
  //      .set("spark.cassandra.auth.username", p.getProperty("username"))
  //      .set("spark.cassandra.auth.password", p.getProperty("password"))
  //      .set("spark.driver.memory", p.getProperty("sparkDriverMemory"))
  //      .set("spark.executor.memory", p.getProperty("sparkExecutorMemory"))
  //      .set("spark.driver.allowMultipleContexts", p.getProperty("sparkDriverAllowMultipleContexts"))
  //      .set("spark.cassandra.connection.compression", p.getProperty("sparkCassandraConnectionCompression"))
  //      .set("spark.ui.port", p.getProperty("sparkUiPort"))
  //      .set("spark.cassandra.connection.timeout_ms", p.getProperty("sparkCassandraConnectionTimeoutMs"))
  //      .set("spark.network.timeout", p.getProperty("sparkNetworkTimeout"))
  //      .set("spark.cassandra.input.consistency.level", p.getProperty("sparkCassandraInputConsistencyLevel"))
  //
  //    if (Environment.isDevelopment) {
  //      sparkConf.set("spark.local.dir", p.getProperty("sparkLocalDir"))
  //    }
  //
  //    sparkConf
  //  }

  /**
    *
    * @return SparkSession
    */
  private def getSparkSession: SparkSession = {
    val p: Properties = new PropService().get(fileName)
    val builder: SparkSession.Builder = SparkSession.builder()

    if (Environment.isDevelopment) {
      builder.config("spark.local.dir", p.getProperty("sparkLocalDir"))
    }

    return builder.master(p.getProperty("master"))
      .appName(p.getProperty("appName"))
      .config("spark.cassandra.connection.host", p.getProperty("host"))
      .config("spark.cassandra.auth.username", p.getProperty("username"))
      .config("spark.cassandra.auth.password", p.getProperty("password"))
      .config("spark.driver.memory", p.getProperty("sparkDriverMemory"))
      .config("spark.executor.memory", p.getProperty("sparkExecutorMemory"))
      .config("spark.driver.allowMultipleContexts", p.getProperty("sparkDriverAllowMultipleContexts"))
      .config("spark.cassandra.connection.compression", p.getProperty("sparkCassandraConnectionCompression"))
      //.config("spark.ui.port", p.getProperty("sparkUiPort"))
      .config("spark.cassandra.connection.timeout_ms", p.getProperty("sparkCassandraConnectionTimeoutMs"))
      .config("spark.network.timeout", p.getProperty("sparkNetworkTimeout"))
      .config("spark.cassandra.input.consistency.level", p.getProperty("sparkCassandraInputConsistencyLevel"))
      .getOrCreate()
  }

  /**
    * this.synchronized is to resolve CONTEXT => null in concurrence
    *
    * @return
    */
  def getContext: SparkContext = this.synchronized {
    if (sparkContext == null) {
      sparkContext = this.getSparkSession.sparkContext
    }
    sparkContext
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
    if (sparkContext != null) {
      sparkContext.clearJobGroup()
      sparkContext = null
    }
  }

}
