package br.com.btg360.application

import br.com.btg360.entities.AccountEntity
import br.com.btg360.model.ModelFactory
import br.com.btg360.traits.ModelFactoryTrait

class Worker(_accountEntity: AccountEntity) extends Application {

  protected var accountEntity: AccountEntity = _accountEntity
  private var context: Context = _

  def process[T <: Model with ModelFactoryTrait](clazz: Class[T]) : Unit = {
    context = new Context(accountEntity)
    val model : T = clazz.getConstructor(classOf[Context]).newInstance(context)
    new ModelFactory(model, accountEntity)
  }

}
