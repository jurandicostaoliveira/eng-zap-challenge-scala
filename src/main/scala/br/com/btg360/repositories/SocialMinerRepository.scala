package br.com.btg360.repositories

import java.sql.SQLException

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, Table}
import br.com.btg360.jdbc.MySqlAllin

class SocialMinerRepository extends Repository {

  private val dbAllin = new MySqlAllin().open

  private val corLoginSmTable: String = "%s.%s".format(Database.MAIL_SENDER, Table.COR_LOGIN_SM)

  /**
    * @param Int allinId
    * @return Boolean
    */
  def allinCrossExists(allinId: Int): Boolean = {
    try {
      val rs = this.connection(this.dbAllin).queryExecutor(
        s"""SELECT COUNT(id_login) AS total FROM ${this.corLoginSmTable} WHERE id_login = ${allinId} LIMIT 1;"""
      )

      rs.first()
      val total = rs.getInt("total")
      rs.close()

      if (total > 0) {
        return true
      }

      false
    } catch {
      case e: SQLException => e.printStackTrace()
        false
    }
  }

}
