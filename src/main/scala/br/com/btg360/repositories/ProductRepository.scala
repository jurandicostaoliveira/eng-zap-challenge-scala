package br.com.btg360.repositories

import br.com.btg360.constants.Keyspace
import br.com.btg360.entities.ProductEntity
import br.com.btg360.spark.SparkContextSingleton
import com.datastax.spark.connector.toSparkContextFunctions
//import com.datastax.spark.connector.rdd.CassandraTableScanRDD
import org.apache.spark.rdd.RDD

class ProductRepository {

  val sc = SparkContextSingleton.getSparkContext()

  private var _table: String = _

  /**
    * Getter
    *
    * @return String
    */
  def table: String = this._table

  /**
    * Setter
    *
    * @param String value
    * @return
    */
  def table(value: String): ProductRepository = {
    this._table = value
    this
  }


  def findAll: RDD[ProductEntity] = {
    val rdd = this.sc.cassandraTable[ProductEntity](Keyspace.BTG360, this.table).limit(5)

    rdd.foreach(row => {
      println(row.productId + " -> " + row.priceBy)
    })

    null
  }


}
