package br.com.btg360.repositories

import br.com.btg360.application.Repository
import javax.validation.OverridesAttribute

import scala.collection.immutable.List
import scala.collection.mutable.HashMap

class TransactionalRepository extends Repository {

  private var typeSend: Int = 1

  private var batchLimit: Int = 10000


  def save(): Unit = {
    val table = "btg_jobs.batch"
    val values = List(HashMap("id" -> 1, "name" -> "lala"), HashMap("id" -> 2, "name" -> "lele"))

    var limiter: Int = 0
    var totalizator: Int = 0
    var queries: List[String] = List()
    val total: Int = values.size

    for (item <- values) {
      val strValues = "'%s'".format(item.values.mkString("','"))
      val query = s"INSERT IGNORE INTO ${table} (${item.keys.mkString(",")}) VALUES (${strValues});"
      queries = queries :+ query
      limiter += 1
      totalizator += 1

      if (limiter >= this.batchLimit || totalizator >= total) {
        this.insertQueryBatch(queries)
        queries = List()
        limiter = 0
      }
    }
  }


  //  def insertQueryBatch2(table: String, values: List[HashMap[String, Any]]): Unit = {
  //
  //    if (values.size <= 0) {
  //      return
  //    }
  //
  //    val strFields: String = "(" + values.head.keys.mkString(",") + ")"
  //    val stmt = this.dbConnection.createStatement()
  //
  //    for (item <- values) {
  //      val strValues = "('" + item.values.mkString("','") + "')"
  //      val query = s"INSERT INTO ${table} ${strFields} VALUES ${strValues};"
  //      println(query)
  //      stmt.addBatch(query)
  //      stmt.executeBatch()
  //    }
  //
  //
  //  }

}
