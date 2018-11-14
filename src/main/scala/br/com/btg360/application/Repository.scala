package br.com.btg360.application

import java.sql.{Connection, ResultSet}

import br.com.btg360.jdbc.MySqlBtg360

import scala.collection.immutable.List
import br.com.btg360.constants.{TypeConverter => TC}



abstract class Repository extends Model {

  private var dbConnection: Connection = this.invoke(classOf[MySqlBtg360]).open

  /**
    *
    * @param dbConnection
    * @return
    */
  def connection(dbConnection: Connection): Repository = {
    this.dbConnection = dbConnection
    this
  }

  /**
    *
    * @param query
    * @return
    */
  def queryExecutor(query: String): ResultSet = {
    this.dbConnection.createStatement().executeQuery(query)
  }

  /**
    * Seek the data and reflect it with the entity
    *
    * @param String query
    * @param Class  entityClass
    * @tparam T
    * @return List[T]
    */
  def fetch[T](query: String, entityClass: Class[T]): List[T] = {
    var list: List[T] = List()
    try {
      val attributes: Array[String] = entityClass.getDeclaredFields.map(_.getName)
      val resultSet = this.queryExecutor(query)
      val metaData = resultSet.getMetaData
      val total = metaData.getColumnCount
      var columns: List[String] = List()
      for (i <- 1 to total) {
        columns ::= metaData.getColumnLabel(i)
      }

      while (resultSet.next()) {
        val entity = entityClass.newInstance()
        for (attribute <- attributes) {
          if (columns.contains(attribute)) {
            val field = entity.getClass.getDeclaredField(attribute)
            field.setAccessible(true)
            field.set(entity, resultSet.getObject(attribute))
          }
        }
        list = list :+ entity
      }

      list
    } catch {
      case e: Exception => println(e.printStackTrace())
        list
    }
  }

  /**
    * @param ResultSet rs
    * @param String    columnName
    * @return Int
    */
  def countByColumnName(rs: ResultSet, columnName: String): Int = {
    try {
      rs.next()
      val total: Int = TC.toInt(rs.getObject(columnName))
      rs.close()
      total
    } catch {
      case e: Exception => println(e.printStackTrace())
        0
    }
  }

  /**
    * @param String table
    * @param T      entity
    * @param List   columns
    * @return void
    */
  def insert(table: String, entity: AnyRef, columns: List[String]) = {
    this.onInsert(table, entity, columns)
  }

  /**
    * @param String table
    * @param T      entity
    * @param List   columns
    * @param List   columnsUpdate
    * @return void
    */
  def insertOrUpdate(table: String, entity: AnyRef, columns: List[String], columnsUpdate: List[String]) = {
    this.onInsert(table, entity, columns, columnsUpdate)
  }

  /**
    * @param String table
    * @param T      entity
    * @param List   columns
    * @return void
    */
  def insertIgnore(table: String, entity: AnyRef, columns: List[String]) = {
    this.onInsert(table, entity, columns, List(), "IGNORE")
  }

  /**
    * Prepare and execute inserts
    *
    * @param String table
    * @param T      entity
    * @param List   columns
    * @param List   columnsUpdate
    * @param String ignore
    * @return void
    */
  private def onInsert(table: String, entity: AnyRef, columns: List[String], columnsUpdate: List[String] = List(), ignore: String = "") = {
    try {
      val strFields: String = columns.mkString(",")
      val strValues: String = List.fill(columns.size)("?").mkString(",")
      var fields: List[String] = columns
      var strOnDuplicate: String = ""

      if (columnsUpdate.size > 0) {
        strOnDuplicate = "ON DUPLICATE KEY UPDATE"
        var fieldsValues: List[String] = List()
        for (column <- columnsUpdate) {
          fieldsValues = fieldsValues :+ "%s = ?".format(column)
        }
        strOnDuplicate = "%s %s".format(strOnDuplicate, fieldsValues.mkString(", "))
        fields = List.concat(columns, columnsUpdate)
      }

      val query = s"INSERT $ignore INTO $table ($strFields) VALUES ($strValues) $strOnDuplicate;"
      val stmt = this.dbConnection.prepareStatement(query)
      var index: Int = 0
      for (row <- fields) {
        index += 1
        val field = entity.getClass.getDeclaredField(row)
        field.setAccessible(true)
        val value = field.get(entity)
        if (value == null) stmt.setNull(index, 0) else stmt.setObject(index, value)
      }

      stmt.executeUpdate()
    } catch {
      case e: Exception => println(e.printStackTrace())
    }
  }

}
