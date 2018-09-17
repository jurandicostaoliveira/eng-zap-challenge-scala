package br.com.btg360.model

import br.com.btg360.application.{Context, Model}
import br.com.btg360.traits.ModelFactoryTrait

class QueueCreateModel(context: Context) extends Model with Serializable with ModelFactoryTrait{
  override def build: Unit = {
    val accountId = context.accountEntity.accountId
    println("CHEGUEI NA MODEL: accountId => " + accountId)
  }
}
