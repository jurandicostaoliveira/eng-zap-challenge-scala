package br.com.btg360.repositories

import java.sql.SQLException

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, MultiChannel, Table}
import br.com.btg360.entities.UserEntity
import br.com.btg360.jdbc.MySqlBtg360

class UserRepository extends Repository {

  private val db = new MySqlBtg360().open

  val userTable: String = "%s.%s".format(Database.MASTER, Table.USERS)

  val userRulesTable: String = "%s.%s".format(Database.PANEL, Table.USERS_RULES)

  /**
    * Return a user by id
    *
    * @param Int id
    * @return ThemeEntity
    */
  def findById(id: Int): UserEntity = {
    try {
      val query = s"""SELECT * FROM ${this.userTable} WHERE id = $id LIMIT 1;"""
      this.connection(this.db).fetch(query, classOf[UserEntity]).head
    } catch {
      case e: SQLException => println(e.printStackTrace())
        new UserEntity()
    }
  }

  /**
    * Return all active users
    *
    * @return ResultSet
    */
  def findAllActive(): List[Int] = {
    try {
      val query =
        s""" SELECT DISTINCT ${this.userTable}.id
          FROM ${this.userTable}
          INNER JOIN ${this.userRulesTable}
            ON ${this.userRulesTable}.userId = ${this.userTable}.id
          WHERE
              ${this.userRulesTable}.status = 1
              AND ${this.userTable}.isMultiChannel = ${MultiChannel.STATUS};
       """
      var list: List[Int] = List()
      val rows = this.connection(this.db).queryExecutor(query)
      while (rows.next()) {
        list = list :+ rows.getInt(1)
      }
      list
    } catch {
      case e: SQLException => println(e.printStackTrace())
        List()
    }
  }

}
