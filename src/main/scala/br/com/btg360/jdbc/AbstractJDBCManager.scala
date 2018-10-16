package br.com.btg360.jdbc

import java.sql.{Connection, DriverManager}
import java.util.Properties

import br.com.btg360.services.PropService
import br.com.btg360.constants.Environment
import br.com.btg360.spark.SparkSqlSingleton
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.rdd.RDD


abstract class AbstractJDBCManager {

  protected var dbDriver: String = _

  protected var driver: String = _

  protected var host: String = _

  protected var user: String = _

  protected var password: String = _

  private var _connection: Connection = _

  private val sqlContext = SparkSqlSingleton.get

  def connection: Connection = this._connection

  /**
    * Get properties the file
    *
    * @return
    */
  private def properties: Properties = {
    val p = new PropService().get("%s-%s.properties".format(this.dbDriver, Environment.getAppEnv))
    p.setProperty("driver", p.getProperty(this.driver))
    p.setProperty("host", p.getProperty(this.host))
    p.setProperty("user", p.getProperty(this.user))
    p.setProperty("password", p.getProperty(this.password))
    p
  }

  /**
    * Open JDBC Connection
    *
    * @return
    */
  def open: Connection = {
    try {
      val p = this.properties
      Class.forName(p.getProperty("driver"))
      this._connection = DriverManager.getConnection(
        p.getProperty("host"),
        p.getProperty("user"),
        p.getProperty("password")
      )
      this._connection
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
        this._connection
    }
  }

  /**
    * Close JDBC Connection
    */
  def close = {
    try {
      if (this._connection != null) {
        this._connection.close()
        this._connection = null
      }
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
    }
  }

  /**
    * Write by overwrite mode
    *
    * @param table
    * @param rdd
    * @param beanClass
    * @tparam T
    */
  def sparkWriteOverwrite[T](table: String, rdd: RDD[T], beanClass: Class[T]): Unit = {
    this.sparkWrite(table, rdd, beanClass, SaveMode.Overwrite)
  }

  /**
    * Write by append mode
    *
    * @param table
    * @param rdd
    * @param beanClass
    * @tparam T
    */
  def sparkWriteAppend[T](table: String, rdd: RDD[T], beanClass: Class[T]): Unit = {
    this.sparkWrite(table, rdd, beanClass, SaveMode.Append)
  }

  /**
    * Write by ignore mode
    *
    * @param table
    * @param rdd
    * @param beanClass
    * @tparam T
    */
  def sparkWriteIgnore[T](table: String, rdd: RDD[T], beanClass: Class[T]): Unit = {
    this.sparkWrite(table, rdd, beanClass, SaveMode.Ignore)
  }

  /**
    * Write by error if exists
    *
    * @param table
    * @param rdd
    * @param beanClass
    * @tparam T
    */
  def sparkWriteErrorIfExists[T](table: String, rdd: RDD[T], beanClass: Class[T]): Unit = {
    this.sparkWrite(table, rdd, beanClass, SaveMode.ErrorIfExists)
  }

  /**
    * Write final
    *
    * @param table
    * @param rdd
    * @param beanClass
    * @param mode
    * @tparam T
    */
  private def sparkWrite[T](table: String, rdd: RDD[T], beanClass: Class[T], mode: SaveMode): Unit = {
    val p = this.properties
    val df: DataFrame = this.sqlContext.createDataFrame(rdd, beanClass)
    df.write.mode(mode).jdbc(p.getProperty("host"), table, p)
  }

  /**
    * Read data by JDBC
    *
    * @param String table
    * @param String where
    * @return
    */
  def sparkRead(table: String, where: String = null): DataFrame = {
    try {
      val p = this.properties

      if (where != null) {
        return this.sqlContext.read.jdbc(p.getProperty("host"), table, p).where(where)
      }

      this.sqlContext.read.jdbc(p.getProperty("host"), table, p)
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
        null
    }
  }

}
