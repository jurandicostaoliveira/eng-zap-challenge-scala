package br.com.btg360.scheduling

import akka.actor.{Actor, ActorSystem, Props}
import br.com.btg360.repositories.UserRepository
import br.com.btg360.traits.SchedulerContextTrait

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._

class Scheduler(
                 name: String,
                 wait: FiniteDuration = 0.second,
                 interval: FiniteDuration = 60.seconds,
                 context: SchedulerContextTrait
               ) {

  /**
    * Receiver system class of actors
    *
    * @param SchedulerContextTrait context
    */
  class ActorContext(context: SchedulerContextTrait) extends Actor {
    override def receive: Receive = {
      case userId: Int => context.run(userId)
    }
  }


  val system = ActorSystem("UserContextScheduler")
  val actor = system.actorOf(Props(new ActorContext(this.context)), this.name)
  val users = new UserRepository().findAllActive()
  implicit val executionContext = system.dispatcher

  system.scheduler.schedule(this.wait, this.interval, new Runnable {
    override def run(): Unit = {
      while(users.next()) {
        actor ! users.getInt(1)
      }
    }
  })

}
