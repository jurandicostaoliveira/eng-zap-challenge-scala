package br.com.btg360.application

import java.sql.{Connection, ResultSet}

import br.com.btg360.jdbc.MySqlBtg360

import scala.collection.immutable.List

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
        columns ::= metaData.getColumnName(i)
      }

      var entity = entityClass.newInstance()
      while (resultSet.next()) {
        for (attribute <- attributes) {
          if (columns.contains(attribute)) {
            val field = entity.getClass.getDeclaredField(attribute)
            field.setAccessible(true)
            field.set(entity, resultSet.getObject(attribute))
          }
        }
        list ::= entity
      }

      list
    } catch {
      case e: Exception => println(e.printStackTrace())
        list
    }
  }

}
