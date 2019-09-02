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

    return builder.master(p.getProperty("master"))
      .appName(p.getProperty("appName"))
      .config("spark.cassandra.connection.host", p.getProperty("host"))
      .config("spark.cassandra.auth.username", p.getProperty("username"))
      .config("spark.cassandra.auth.password", p.getProperty("password"))
      .config("spark.cassandra.connection.compression", p.getProperty("sparkCassandraConnectionCompression"))
      .config("spark.cassandra.connection.timeout_ms", p.getProperty("sparkCassandraConnectionTimeoutMs"))
      .config("spark.cassandra.connection.keep_alive_ms", p.getProperty("sparkCassandraConnectionKeepAliveMs"))
      .config("spark.driver.memory", p.getProperty("sparkDriverMemory"))
      .config("spark.executor.memory", p.getProperty("sparkExecutorMemory"))
      .config("spark.driver.allowMultipleContexts", p.getProperty("sparkDriverAllowMultipleContexts"))
      .config("spark.ui.port", p.getProperty("sparkUiPort"))
      .config("spark.network.timeout", p.getProperty("sparkNetworkTimeout"))
      .config("spark.cassandra.input.consistency.level", p.getProperty("sparkCassandraInputConsistencyLevel"))
      .config("spark.local.dir", p.getProperty("sparkLocalDir"))
      .config("spark.scheduler.mode", p.getProperty("sparkSchedulerMode"))
      .config("spark.scheduler.allocation.file", p.getProperty("sparkSchedulerAllocationFile"))
      .config("spark.rdd.compress", p.getProperty("sparkRddCompress"))
      .config("spark.shuffle.compress", p.getProperty("sparkShuffleCompress"))
      .config("spark.shuffle.spill.compress", p.getProperty("sparkShuffleSpillCompress"))
      .config("spark.default.parallelism", p.getProperty("sparkDefaultParallelism"))
      .config("spark.sql.shuffle.partitions", p.getProperty("sparkSqlShufflePartitions"))
      .getOrCreate()
  }

  /**
    * this.synchronized is to resolve CONTEXT => null in concurrence
    *
    * @return
    */
  def getContext: SparkContext = {
    //this.sparkContext.setLocalProperty("spark.scheduler.pool", "default")
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
