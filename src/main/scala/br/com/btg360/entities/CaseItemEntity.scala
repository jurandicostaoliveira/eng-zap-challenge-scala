package br.com.btg360.entities

import br.com.btg360.application.Entity

import scala.collection.Map

case class CaseItemEntity(
                           products: List[Map[String, Any]] = List(),
                           recommendations: List[Map[String, Any]] = List(),
                           references: Map[String, Any] = Map()
                         ) extends Entity
