package br.com.btg360.run

import akka.actor.{Actor, ActorSystem, Props}
import br.com.btg360.services.PeriodService


import scala.concurrent.duration._

class Day {

  def run(id: Int): Unit = {
    val a = List(1, 2, 3, 4, 5)
    a.foreach(row => {
      println(new PeriodService().now + " : " + id + " -> " + row)
    })
    println("===================================================")
  }

}

object SimpleActorExample extends App {

  class SimpleActor extends Actor {
    override def receive: Receive = {
      case i: Int => {
        new Day().run(i)
      }
    }
  }



  val system = ActorSystem("SimpleSystem")
  val actor = system.actorOf(Props[SimpleActor], "SimpleActor")
  implicit val ec = system.dispatcher


  system.scheduler.schedule(0.second, 60.seconds, new Runnable {
    override def run(): Unit = {
      (actor ! 12)
    }
  })
  system.scheduler.schedule(30.second, 60.seconds, new Runnable {
    override def run(): Unit = {
      (actor ! 768)
    }
  })


  //system.awaitTermination()

}
