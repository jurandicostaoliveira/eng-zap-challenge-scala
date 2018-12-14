package br.com.btg360.application

import java.sql.{Connection, ResultSet, SQLException}

import br.com.btg360.constants.{TypeConverter => TC}
import br.com.btg360.jdbc.MySqlBtg360

import scala.collection.immutable.List
import scala.collection.mutable.HashMap


abstract class Repository extends Model {

  private var dbConnection: Connection = this.invoke(classOf[MySqlBtg360]).open

  private var where: String = ""

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
    * @param query
    * @return
    */
  def queryExecutor(query: String): ResultSet = {
    try {
      this.dbConnection.createStatement().executeQuery(query)
    } catch {
      case e: SQLException => println(e.getErrorCode + ": " + e.getMessage)
        null
    }
  }

  /**
    * @param String query
    */
  def ddlExecutor(query: String): Unit = {
    try {
      this.dbConnection.createStatement().executeUpdate(query)
    } catch {
      case e: SQLException => println(e.getStackTrace)
    }
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

      resultSet.close()
      list
    } catch {
      case e: SQLException => println(e.printStackTrace())
        list
    }
  }

  /**
    * @param ResultSet rs
    * @param String    columnName
    * @return Int
    */
  def countByColumnName(resultSet: ResultSet, columnName: String): Int = {
    try {
      resultSet.next()
      val total: Int = TC.toInt(resultSet.getObject(columnName))
      resultSet.close()
      total
    } catch {
      case e: SQLException => println(e.printStackTrace())
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
      stmt.close()
    } catch {
      case e: Exception => println(e.printStackTrace())
    }
  }

  /**
    * @param table
    * @param data
    * @return
    */
  def insertGetId(table: String, data: HashMap[String, Any]): Int = {
    try {
      val strFields: String = data.keys.mkString(",")
      val strValues: String = List.fill(data.size)("?").mkString(",")
      val query = s"INSERT INTO $table ($strFields) VALUES ($strValues);"
      val stmt = this.dbConnection.prepareStatement(query)
      var index = 1

      for (value <- data.values) {
        if (value == null) stmt.setNull(index, 0) else stmt.setObject(index, value)
        index += 1
      }

      stmt.executeUpdate()
      val keys = stmt.getGeneratedKeys
      println(keys.next())
      5
//      keys.next()
//      val key = keys.getInt(1)
//      stmt.close()
//      key
    } catch {
      case e: SQLException => println(e.printStackTrace())
        0
    }
  }

  /**
    * Insert multiple data
    *
    * @param String  table
    * @param List    data
    * @param Boolean ignore
    */
  def insertBatch(table: String, data: List[HashMap[String, Any]], ignore: Boolean = false): Unit = {
    try {
      if (data.size <= 0) return

      val onIgnore = if (ignore) "IGNORE" else ""
      val strValues: String = List.fill(data.head.size)("?").mkString(",")
      val query = s"INSERT $onIgnore INTO $table (${data.head.keys.mkString(",")}) VALUES ($strValues);"
      val stmt = this.dbConnection.prepareStatement(query)

      data.foreach(row => {
        var index = 1
        for (value <- row.values) {
          if (value == null) stmt.setNull(index, 0) else stmt.setObject(index, value)
          index += 1
        }
        stmt.addBatch()
      })

      stmt.executeBatch()
      stmt.close()
    } catch {
      case e: SQLException => println(e.printStackTrace())
    }
  }


  /**
    * Insert multiple queries
    *
    * @param List queries
    */
  def insertQueryBatch(queries: List[String]): Unit = {
    try {
      val stmt = this.dbConnection.createStatement()
      for (query <- queries) {
        stmt.addBatch(query)
      }

      stmt.executeBatch()
      stmt.close()
    } catch {
      case e: Exception => println(e.printStackTrace())
    }
  }

  /**
    * @param String value
    * @return HashMap
    */
  private def databaseSplit(value: String): HashMap[String, String] = {
    if (value.contains(".")) {
      val line = value.split("\\.")
      return HashMap("database" -> line(0), "table" -> line(1))
    }

    HashMap("database" -> null, "table" -> value)
  }

  /**
    * Check if a table exists
    *
    * @param String database
    * @param String table
    * @return Boolean
    */
  def tableExists(table: String): Boolean = {
    val row = this.databaseSplit(table)
    this.dbConnection.getMetaData.getTables(row("database"), null, row("table"), null).next()
  }

  /**
    * Check if a column exists in table
    *
    * @param String database
    * @param String table
    * @param String column
    * @return Boolean
    */
  def columnExists(table: String, column: String): Boolean = {
    val row = this.databaseSplit(table)
    this.dbConnection.getMetaData.getColumns(row("database"), null, row("table"), column).next()
  }

  /**
    * @param String column
    * @param String condition
    * @param Any    value
    * @return this
    */
  def whereAnd(column: String, condition: String, value: Any): Repository = {
    this.onWhere(column, condition, value, "AND")
    this
  }

  /**
    * @param String column
    * @param String condition
    * @param Any    value
    * @return this
    */
  def whereOr(column: String, condition: String, value: Any): Repository = {
    this.onWhere(column, condition, value, "OR")
    this
  }

  /**
    * @param String column
    * @param String condition
    * @param Any    value
    * @param String clause
    * @return this
    */
  private def onWhere(column: String, condition: String, value: Any, clause: String) = {
    val onClause = if (this.where.isEmpty) "" else clause
    this.where += s""" ${onClause} ${column} ${condition} '${value}'"""
  }

  /**
    * Update from collection data
    *
    * @param String  table
    * @param HashMap collection
    */
  def update(table: String, data: HashMap[String, Any]): Unit = {
    try {
      var columns: List[String] = List()
      for (key <- data.keys) {
        columns = columns :+ "%s=?".format(key)
      }

      var query = s"""UPDATE ${table} SET ${columns.mkString(",")}"""
      if (!this.where.isEmpty) {
        query += " WHERE %s".format(this.where.trim)
        this.where = ""
      }

      val stmt = this.dbConnection.prepareStatement(query.concat(";"))
      var index = 1
      for (value <- data.values) {
        if (value == null) stmt.setNull(index, 0) else stmt.setObject(index, value)
        index += 1
      }
      stmt.executeUpdate()
      stmt.close()
    } catch {
      case e: SQLException => println(e.printStackTrace())
    }
  }

}
