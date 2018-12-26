package br.com.btg360.repositories

import br.com.btg360.application.Repository
import br.com.btg360.entities.ConsolidatedEntity
import br.com.btg360.jdbc.MySqlBtg360
import br.com.btg360.services.PeriodService
import org.apache.spark.rdd.RDD

import scala.collection.mutable.HashMap

class ConsolidatedRepository extends Repository {

  private val db = new MySqlBtg360()

  private val periodService = new PeriodService()

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
    this.db.sparkRead(this.table, "isSent = 0").rdd.map(row => {
      new ConsolidatedEntity().setRow(row)
    })
  }

  /**
    * Get all registers in pair
    *
    * @param entity
    * @return RDD
    */
  def findAllKeyBy(entity: ConsolidatedEntity => (Any, ConsolidatedEntity)): RDD[(Any, ConsolidatedEntity)] = {
    this.findAll.map(row => entity(row))
  }

  /**
    * Update whether it was sent or not
    *
    * @param Int isSent
    */
  def updateSubmitted(isSent: Int = 1, platformId: Int = 0): Unit = {
    val whereIsSent = if (isSent == 1) 0 else 1

    if (!platformId.equals(0)) {
      this.whereAnd("platformId", "=", platformId)
    }

    this.whereAnd("isSent", "=", whereIsSent)
      .update(this.table, HashMap("isSent" -> isSent))
  }

  /**
    * Remove tables from that specified period
    *
    * @param Int days
    */
  def deleteOnDays(days: Int = 90): Unit = {
    val date = this.periodService.format("yyyy_MM_dd").timeByDay(-days)
    val query = s"SHOW TABLES FROM btg_consolidated WHERE Tables_in_btg_consolidated LIKE '%$date%';"
    val tables = this.connection(this.db.open).queryExecutor(query)
    while (tables.next()) {
      this.queryExecutor(s"DROP TABLE ${tables.getString("Tables_in_btg_consolidated")}", true)
    }
  }

}
