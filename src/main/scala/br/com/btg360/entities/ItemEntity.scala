package br.com.btg360.entities

import br.com.btg360.application.Entity
import scala.collection.Map

class ItemEntity extends Entity {

  var products: List[ConsolidatedProductEntity] = List()

  var recommendations: List[ConsolidatedProductEntity] = List()

  var references: Map[String, String] = Map()

  /**
    * Add products in list
    *
    * @param ConsolidatedProductEntity value
    */
  def addProducts(value: ConsolidatedProductEntity): Unit = {
    this.products ::= value
  }

  /**
    * Add recommendations in list
    *
    * @param ConsolidatedProductEntity value
    */
  def addRecommendations(value: ConsolidatedProductEntity): Unit = {
    this.recommendations ::= value
  }

  /**
    * Add references list
    *
    * @param value
    */
  def addReferences(value: Map[String, String]): Unit = {
    this.references = value
  }

}
