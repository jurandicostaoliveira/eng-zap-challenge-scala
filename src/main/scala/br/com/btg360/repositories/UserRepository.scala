package br.com.btg360.repositories

import java.sql.SQLException

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, MultiChannel, Table}
import br.com.btg360.entities.UserEntity
import br.com.btg360.jdbc.{MySqlAllin, MySqlBtg360}

class UserRepository extends Repository {

  private val dbBtg360 = new MySqlBtg360().open

  private val dbAllin = new MySqlAllin().open

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
      this.connection(this.dbBtg360).fetch(query, classOf[UserEntity]).head
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
              AND ${this.userTable}.isMultiChannel = ${MultiChannel.STATUS}
              AND ${this.userTable}.isDedicatedEnv = ${MultiChannel.isDedicatedEnv};
       """
      var list: List[Int] = List()
      val rows = this.connection(this.dbBtg360).queryExecutor(query)
      while (rows.next()) {
        list = list :+ rows.getInt(1)
      }
      list
    } catch {
      case e: SQLException => println(e.printStackTrace())
        List()
    }
  }

  /**
    * Process to disable clients
    */
  def disableByAllin: Unit = {
    try {
      //Btg360
      var listBtg360: List[Long] = List()
      val rsBtg360 = this.connection(this.dbBtg360).queryExecutor(
        "SELECT allinId FROM master.users WHERE homologation = 1;"
      )
      while (rsBtg360.next()) {
        listBtg360 = listBtg360 :+ rsBtg360.getLong(1)
      }

      //Allin
      var listAllin: List[Long] = List()
      val rsInAllin = this.connection(this.dbAllin).queryExecutor(
        "SELECT id_login FROM emailpro_mailsender.cor_login WHERE fl_ativo = 1 AND id_login_situacao = 1;"
      )
      while (rsInAllin.next()) {
        listAllin = listAllin :+ rsInAllin.getLong(1)
      }

      val list: String = listBtg360.diff(listAllin).mkString(",")
      this.connection(this.dbBtg360).queryExecutor(
        s"UPDATE master.users SET homologation = 0, homologation_at = NOW() WHERE allinId IN (${list});", true
      )
    } catch {
      case e: SQLException => println(e.printStackTrace())
    }
  }

}
