package br.com.btg360.repositories

import java.sql.{Connection, ResultSet}

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, Table}
import br.com.btg360.jdbc.MySqlAllin
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

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
  def findSettings: Map[String, Int] = {
    var map: Map[String, Int] = Map("isFiled" -> 1, "isExcluded" -> 1)
    try {
      val conn: Connection = this.db.open
      val query: String = s"SELECT * FROM ${this.generateListTable} WHERE id_lista = ${this._listId};"
      val rs: ResultSet = this.connection(conn).queryExecutor(query)
      while (rs.next()) {
        map = Map(
          "isFiled" -> rs.getInt("fl_arquivado"),
          "isExcluded" -> rs.getInt("fl_excluir")
        )
      }
      conn.close()
      map
    } catch {
      case e: Exception => println(e.printStackTrace())
        map
    }
  }

  /**
    * @return ResultSet
    */
  def describe: List[String] = {
    try {
      val conn: Connection = this.db.open
      val query: String = s"DESCRIBE ${this.generateReferenceListTable};"
      val rs: ResultSet = this.connection(conn).queryExecutor(query)
      var columns: List[String] = List()
      while (rs.next()) {
        columns ::= rs.getString("Field")
      }
      conn.close()
      columns
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
  def findAll: RDD[(String, Row)] = {
    try {
      this.db.sparkRead(this.generateReferenceListTable)
        .rdd.map(row => (row.getAs("nm_email").toString, row))
    } catch {
      case e: Exception => println(e.printStackTrace())
        this.sc.emptyRDD
    }
  }

}
