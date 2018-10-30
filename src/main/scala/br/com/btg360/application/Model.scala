package br.com.btg360.application

import java.sql.ResultSet

abstract class Model extends Application {

  /**
    * @param ResultSet rs
    * @return Int
    */
  def countRows(rs: ResultSet): Int = {
    rs.last()
    val numRows: Int = rs.getRow()
    rs.beforeFirst()
    numRows
  }

}
