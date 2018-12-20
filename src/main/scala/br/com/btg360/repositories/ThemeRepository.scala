package br.com.btg360.repositories

import java.sql.SQLException

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, Table}
import br.com.btg360.entities.ThemeEntity
import br.com.btg360.jdbc.MySqlBtg360

class ThemeRepository extends Repository {

  private val db = new MySqlBtg360().open

  val table: String = "%s.%s".format(Database.PANEL, Table.THEMES)

  /**
    * Return a theme by id
    *
    * @param Int id
    * @return ThemeEntity
    */
  def findById(id: Int): ThemeEntity = {
    try {
      val query = s"""SELECT * FROM ${this.table} WHERE id = $id LIMIT 1;"""
      this.connection(this.db).fetch(query, classOf[ThemeEntity]).head
    } catch {
      case e: SQLException => println(e.printStackTrace())
        new ThemeEntity()
    }
  }

}
