package br.com.btg360.repositories

import br.com.btg360.entities.ConsolidatedEntity
import br.com.btg360.jdbc.MySqlBtg360
import org.apache.spark.rdd.RDD

class ConsolidatedRepository {

  private val db = new MySqlBtg360()

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
    * @param value
    * @return
    */
  def table(value: String): ConsolidatedRepository = {
    this._table = value
    this
  }

  /**
    * Get all registers
    *
    * @return RDD
    */
  def findAll: RDD[ConsolidatedEntity] = {
    this.db.sparkRead(this.table).rdd.map(row => {
      new ConsolidatedEntity().setRow(row)
    })
  }

}
