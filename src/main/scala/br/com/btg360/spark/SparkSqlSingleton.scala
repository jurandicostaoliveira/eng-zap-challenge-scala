package br.com.btg360.spark

import org.apache.spark.sql.SQLContext

object SparkSqlSingleton {

  private var sqlContext: SQLContext = _

  /**
    * Get SQLContext session
    *
    * @return SQLContext
    */
  def get: SQLContext = {
    if (this.sqlContext == null) {
      this.sqlContext = new SQLContext(SparkCoreSingleton.getContext)
    }
    this.sqlContext
  }

}
