package br.com.btg360.traits

import br.com.btg360.entities.ConsolidatedEntity

trait ConsolidatedKeyByTrait extends Serializable {

  /**
    * Create a pair dynamically
    *
    * @param ConsolidatedEntity entity
    * @return (?, ConsolidatedEntity)
    */
  def call(entity: ConsolidatedEntity): (Any, ConsolidatedEntity)

}
