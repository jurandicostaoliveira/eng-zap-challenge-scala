package br.com.btg360.model

import br.com.btg360.entities.AccountEntity
import br.com.btg360.traits.ModelFactoryTrait

@SerialVersionUID(1L)
class ModelFactory(factory: ModelFactoryTrait, accountEntity: AccountEntity) extends Serializable {
  factory.build
}
