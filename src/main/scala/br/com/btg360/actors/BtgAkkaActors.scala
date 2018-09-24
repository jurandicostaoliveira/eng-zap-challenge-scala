package br.com.btg360.actors

import akka.actor.{Actor, ActorLogging, Props}
import br.com.btg360.application.{Model, Worker}
import br.com.btg360.entities.AccountEntity
import br.com.btg360.traits.ModelFactoryTrait

object BtgAkkaActors {
  final case class callIt[T <: Model with ModelFactoryTrait](id: Int, clazz: Class[T])
}

class BtgAkkaActors extends Actor with ActorLogging {
  import BtgAkkaActors._
  override def receive: Receive = {
    case callIt(id, clazz) =>
      val secondRef = context.actorOf(Props.empty, "second-create-queue-actor" + id)
      log.info(s"Second: $secondRef")
      secondRef ! loadWork(id, clazz)
  }

  def loadWork[T <: Model with ModelFactoryTrait](id: Int, clazz: Class[T]): Unit = {
    var accountEntity: AccountEntity = new AccountEntity
    accountEntity.accountId_=(id)
    val worker: Worker = new Worker(accountEntity)
    worker.process(clazz)
  }
}
