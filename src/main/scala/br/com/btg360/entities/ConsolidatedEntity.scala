package br.com.btg360.entities

import org.apache.spark.sql.Row
import br.com.btg360.application.Entity

class ConsolidatedEntity extends Entity {

  var userId: String = _
  var userSent: String = _
  var productId: String = _
  var isSent: Int = 0
  var isRecommendation: Int = 0
  var percentage: Double = 0
  var platformId: Int = 0
  var createdAt: String = _

  /**
    * Setters attributes
    *
    * @param Row row
    * @return this
    */
  def setRow(row: Row): ConsolidatedEntity = {
    this.userId = row.getAs("userId")
    this.userSent = row.getAs("userSent")
    this.productId = row.getAs("productId")
    this.isSent = row.getAs("isSent")
    this.isRecommendation = row.getAs("isRecommendation")
    this.percentage = row.getAs("percentage")
    this.platformId = row.getAs("platformId")
    this.createdAt = row.getAs("createdAt")
    this
  }

}
