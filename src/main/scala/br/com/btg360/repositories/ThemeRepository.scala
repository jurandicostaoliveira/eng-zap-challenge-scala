package br.com.btg360.repositories

import java.sql.SQLException

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, Table}
import br.com.btg360.entities.ThemeEntity

class ThemeRepository extends Repository {

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
      this.fetch(query, classOf[ThemeEntity]).head
    } catch {
      case e: SQLException => println(e.printStackTrace())
        new ThemeEntity()
    }
  }

}
