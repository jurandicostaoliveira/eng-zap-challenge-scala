package br.com.btg360.spark

import org.apache.spark.sql.SQLContext

object SparkSqlContextSingleton {

  private var sqlContext: SQLContext = _

  def get: SQLContext = {
    if (this.sqlContext == null) {
      this.sqlContext = new SQLContext(SparkContextSingleton.getSparkContext())
    }
    this.sqlContext
  }


}
