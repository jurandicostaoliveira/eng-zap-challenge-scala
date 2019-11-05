package br.com.btg360.entities

import org.apache.spark.sql.Row

class FilterEntity extends Serializable {

  var id: Int = 0
  var field: String = ""
  var comparator: String = ""
  var value: String = ""
  var format: String = ""
  var groupId: Int = 0
  var operator: String = ""
  var groupOperator: String = ""

  def setRow(row: Row): FilterEntity = {
    try {
      this.id = row.getAs("id")
      this.field = row.getAs("field")
      this.comparator = row.getAs("comparator")
      this.value = row.getAs("value")
      this.format = row.getAs("format")
      this.groupId = row.getAs("groupId")
      this.operator = row.getAs("operator")
      this.groupOperator = row.getAs("groupOperator")
    } catch {
      case e: Exception => println("AUTOMATIC FILTER ERROR : " + e.getMessage)
    }
    this
  }
}
