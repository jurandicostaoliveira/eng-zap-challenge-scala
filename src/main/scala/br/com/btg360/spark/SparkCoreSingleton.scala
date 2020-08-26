package br.com.btg360.spark

import java.util.Properties

import br.com.btg360.constants.Environment
import br.com.btg360.services.PropService
import com.datastax.driver.core.Session
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext

object SparkCoreSingleton extends Serializable {

  private val cassandraFileName = "cassandra-%s.properties".format(Environment.getAppEnv)

  private val redisFileName = "redis-%s.properties".format(Environment.getAppEnv)

  private val sparkContext: SparkContext = this.getSparkSession.sparkContext

  /**
    *
    * @return SparkSession
    */
  private def getSparkSession: SparkSession = {
    val pCassandra: Properties = new PropService().get(this.cassandraFileName)
    val pRedis: Properties = new PropService().get(this.redisFileName)
    val builder: SparkSession.Builder = SparkSession.builder()

    if (!Environment.isCluster) {
      builder
        .master(pCassandra.getProperty("master"))
        .config("spark.driver.memory", pCassandra.getProperty("sparkDriverMemory"))
        .config("spark.executor.memory", pCassandra.getProperty("sparkExecutorMemory"))
        .config("spark.rdd.compress", pCassandra.getProperty("sparkRddCompress"))
        .config("spark.shuffle.compress", pCassandra.getProperty("sparkShuffleCompress"))
        .config("spark.shuffle.spill.compress", pCassandra.getProperty("sparkShuffleSpillCompress"))
        .config("spark.sql.shuffle.partitions", pCassandra.getProperty("sparkSqlShufflePartitions"))
        .config("spark.default.parallelism", pCassandra.getProperty("sparkDefaultParallelism"))
        .config("spark.network.timeout", pCassandra.getProperty("sparkNetworkTimeout"))
        .config("spark.executor.heartbeatInterval", pCassandra.getProperty("sparkExecutorHeartbeatInterval"))
        .config("spark.cassandra.connection.timeout_ms", pCassandra.getProperty("sparkCassandraConnectionTimeoutMs"))
        .config("spark.cassandra.connection.keep_alive_ms", pCassandra.getProperty("sparkCassandraConnectionKeepAliveMs"))
        .config("spark.scheduler.mode", pCassandra.getProperty("sparkSchedulerMode"))
        .config("spark.scheduler.allocation.file", pCassandra.getProperty("sparkSchedulerAllocationFile"))
        .config("spark.driver.maxResultSize", pCassandra.getProperty("sparkDriverMaxResultSize"))
        .config("spark.local.dir", pCassandra.getProperty("sparkLocalDir"))
    }

    return builder
      .appName(pCassandra.getProperty("appName"))
      .config("spark.cassandra.connection.host", pCassandra.getProperty("sparkCassandraConnectionHost"))
      .config("spark.cassandra-connection.port", pCassandra.getProperty("sparkCassandraConnectionPort"))
      .config("spark.cassandra.auth.username", pCassandra.getProperty("sparkCassandraAuthUsername"))
      .config("spark.cassandra.auth.password", pCassandra.getProperty("sparkCassandraAuthPassword"))
      .config("spark.cassandra.connection.compression", pCassandra.getProperty("sparkCassandraConnectionCompression"))
      .config("spark.redis.host", pRedis.getProperty("host"))
      .config("spark.redis.port", pRedis.getProperty("port"))
      .config("spark.redis.auth", pRedis.getProperty("secret"))
      .config("spark.redis.timeout", pRedis.getProperty("timeout"))
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
