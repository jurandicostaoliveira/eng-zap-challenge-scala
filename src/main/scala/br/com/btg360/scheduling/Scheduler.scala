package br.com.btg360.scheduling

import akka.actor.{Actor, ActorSystem, Props}
import br.com.btg360.repositories.UserRepository
import br.com.btg360.traits.RunnableScheduleTrait

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._

class Scheduler {

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
    * Executable for all active users
    *
    * @param String                name
    * @param FiniteDuration        wait
    * @param FiniteDuration        interval
    * @param RunnableScheduleTrait runnable
    */
  def all(
           name: String,
           wait: FiniteDuration = 0.second,
           interval: FiniteDuration = 60.seconds,
           runnable: RunnableScheduleTrait
         ): Unit = {
    val system = ActorSystem("RunnableScheduleAll")
    val actor = system.actorOf(Props(new ActorContext(runnable)), name)
    implicit val executionContext = system.dispatcher

    system.scheduler.schedule(wait, interval, new Runnable {
      override def run(): Unit = {
        new UserRepository().findAllActive().foreach(userId => actor ! userId)
      }
    })
  }

  /**
    * Executable for user groups
    *
    * @param String                name
    * @param FiniteDuration        wait
    * @param FiniteDuration        interval
    * @param RunnableScheduleTrait runnable
    */
  def once(
            name: String,
            wait: FiniteDuration = 0.second,
            interval: FiniteDuration = 60.seconds,
            runnable: RunnableScheduleTrait
          ): Unit = {
    val system = ActorSystem("RunnableScheduleOnce")
    implicit val executionContext = system.dispatcher

    system.scheduler.schedule(wait, interval, new Runnable {
      override def run(): Unit = {
        runnable.run()
      }
    })
  }

}
