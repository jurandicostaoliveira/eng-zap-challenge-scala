package br.com.btg360.repositories

import java.sql.ResultSet

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, Table}
import br.com.btg360.jdbc.MySqlAllin
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD

import scala.collection.Map


class ReferenceListRepository extends Repository {

  private val sc = SparkCoreSingleton.getContext

  private val db = new MySqlAllin()

  private var _allinId: Int = 0

  private var _listId: Int = 0

  /**
    * Getter
    *
    * @return
    */
  def allinId: Int = this._allinId

  /**
    * Setter
    *
    * @param value
    * @return
    */
  def allinId(value: Int): ReferenceListRepository = {
    this._allinId = value
    this
  }

  /**
    * Getter
    *
    * @return Int
    */
  def listId: Int = this._listId

  /**
    * Setter
    *
    * @param Int value
    * @return Int
    */
  def listId(value: Int): ReferenceListRepository = {
    this._listId = value
    this
  }

  /**
    * @return String
    */
  private def generateListTable: String = {
    "%s.%s".format(Database.LIST, Table.COR_LIST)
  }

  /**
    * @return String
    */
  private def generateReferenceListTable: String = {
    "%s.%s_%d_%d".format(Database.LIST, Table.BASE, this._allinId, this._listId)
  }

  /**
    * @return ResultSet
    */
  def findSettings: ResultSet = {
    try {
      val query = s"SELECT * FROM ${this.generateListTable} WHERE id_lista = ${this._listId};"
      this.connection(this.db.open).queryExecutor(query)
    } catch {
      case e: Exception => println(e.printStackTrace())
        null
    }
  }

  /**
    * @return ResultSet
    */
  private def describe: ResultSet = {
    try {
      this.connection(this.db.open).queryExecutor(s"DESCRIBE ${this.generateReferenceListTable};")
    } catch {
      case e: Exception => println(e.printStackTrace())
        null
    }
  }

  /**
    * Return all reference list
    *
    * @return RDD
    */
  def findAll: RDD[Map[String, String]] = {
    try {
      val describe = this.describe
      var columns: List[String] = List()
      while (describe.next()) {
        columns ::= describe.getString("Field")
      }

      val rows: RDD[Map[String, String]] = this.db.sparkRead(this.generateReferenceListTable).rdd.map(row => {
        row.getValuesMap(columns)
      })

      rows
    } catch {
      case e: Exception => println(e.printStackTrace())
        this.sc.emptyRDD
    }
  }

}
