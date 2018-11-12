package br.com.btg360.entities

import br.com.btg360.application.Entity

import scala.collection.Map
import scala.collection.mutable.ListBuffer

class ItemEntity extends Entity {

  var products: ListBuffer[ConsolidatedProductEntity] = ListBuffer()

  var recommendations: ListBuffer[ConsolidatedProductEntity] = ListBuffer()

  var references: Map[String, String] = Map()

  /**
    * Add products in list
    *
    * @param ConsolidatedProductEntity value
    */
  def addProducts(value: ConsolidatedProductEntity): Unit = {
    this.products.append(value)
  }

  /**
    * Add recommendations in list
    *
    * @param ConsolidatedProductEntity value
    */
  def addRecommendations(value: ConsolidatedProductEntity): Unit = {
    this.recommendations.append(value)
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
