package br.com.btg360.scheduling

import akka.actor.{Actor, ActorSystem, PoisonPill, Props}
import br.com.btg360.traits.ActorTrait

import scala.concurrent.duration.FiniteDuration

class ActorSchedulerRunnable {

  class ActorContext(beanClass: ActorTrait) extends Actor {
    override def receive: Receive = {
      case userId: Int => beanClass.dispatch(userId)
    }
  }

  def setup[T <: ActorTrait](initialDelay: FiniteDuration, interval: FiniteDuration, beanClass: Class[T]): Unit = {
    val system = ActorSystem("ActorContextSystem")
    val actor = system.actorOf(Props(new ActorContext(beanClass.newInstance())), "ActorContext")
    implicit val executionContext = system.dispatcher

    system.scheduler.schedule(initialDelay, interval, new Runnable {
      override def run(): Unit = {
        val users = List(2, 4, 17, 29)
        users.foreach(id => {

          actor ! id

        })
      }
    })

  }

}
