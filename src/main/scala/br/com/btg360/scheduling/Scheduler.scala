package br.com.btg360.scheduling

import akka.actor.{Actor, ActorSystem, Props}
import br.com.btg360.repositories.UserRepository
import br.com.btg360.traits.RunnableScheduleTrait

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._

class Scheduler(
                 name: String,
                 wait: FiniteDuration = 0.second,
                 interval: FiniteDuration = 60.seconds,
                 runnable: RunnableScheduleTrait
               ) {

  /**
    * Receiver system class of actors
    *
    * @param SchedulerContextTrait context
    */
  class ActorContext(runnable: RunnableScheduleTrait) extends Actor {
    override def receive: Receive = {
      case userId: Int => runnable.run(userId)
    }
  }

  /**
    * Scheduling system settings
    */
  private val system = ActorSystem("RunnableSchedule")
  private val actor = system.actorOf(Props(new ActorContext(this.runnable)), this.name)
  private val users = new UserRepository().findAllActive()
  implicit val executionContext = system.dispatcher

  system.scheduler.schedule(this.wait, this.interval, new Runnable {
    override def run(): Unit = {
      users.foreach(userId => {
        actor ! userId
      })
    }
  })

}
