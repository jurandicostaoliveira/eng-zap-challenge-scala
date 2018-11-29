package br.com.btg360.entities

import org.apache.spark.sql.Row

class FilterEntity extends Serializable {

  var id: Int = _
  var field: String = _
  var comparator: String = _
  var value: String = _
  var format: String = _
  var groupId: Int = _
  var operator: String = _
  var groupOperator: String = _

  def setRow(row: Row): FilterEntity = {
    this.id = row.getAs("id")
    this.field = row.getAs("field")
    this.comparator = row.getAs("comparator")
    this.value = row.getAs("value")
    this.format = row.getAs("format")
    this.groupId = row.getAs("groupId")
    this.operator = row.getAs("operator")
    this.groupOperator = row.getAs("groupOperator")
    this
  }
}
