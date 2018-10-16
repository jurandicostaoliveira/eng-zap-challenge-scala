package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.constants.Database
import br.com.btg360.entities.{ConsolidatedEntity, ProductConsolidatedEntity, ProductEntity}
import br.com.btg360.repositories.{ConsolidatedRepository, ProductRepository}
import org.apache.spark.rdd.RDD

class DataStockService extends Service {

  private var _consolidatedTable: String = _

  private var _productTable: String = _

  /**
    * Getter
    *
    * @return String
    */
  def consolidatedTable: String = "%s.%s".format(Database.CONSOLIDATED, this._consolidatedTable)

  /**
    * Setter
    *
    * @param String value
    * @return this
    */
  def consolidatedTable(value: String): DataStockService = {
    this._consolidatedTable = value
    this
  }

  /**
    * Getter
    *
    * @return String
    */
  def productTable: String = this._productTable

  /**
    * Setter
    *
    * @param String value
    * @return this
    */
  def productTable(value: String): DataStockService = {
    this._productTable = value
    this
  }


  private def consolidatedData: RDD[(Any, ConsolidatedEntity)] = {
    new ConsolidatedRepository().table(this.consolidatedTable).findAllKeyBy(entity => (entity.productId, entity))
  }

  private def productData: RDD[(Any, ProductEntity)] = {
    new ProductRepository().table(this.productTable).findAllKeyBy(entity => (entity.productId, entity))
  }


  def get: RDD[(String, ProductConsolidatedEntity)] = {
    val join: RDD[(String, ProductConsolidatedEntity)] = this.consolidatedData.join(this.productData).map(row => {
      (row._2._1.productId, new ProductConsolidatedEntity().setRow(row._2._2, row._2._1))
    })

    join
  }


}
