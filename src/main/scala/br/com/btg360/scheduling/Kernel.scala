package br.com.btg360.scheduling

import br.com.btg360.services.PeriodService
import br.com.btg360.traits.ActorTrait

import scala.concurrent.duration._

class Day extends ActorTrait {

  def dispatch(userId: Int): Unit = {
    val a = List(1, 2, 3, 4, 5)
    a.foreach(row => {
      println(new PeriodService().now + " : " + userId + " -> " + row)
    })
    println("===================================================")
  }

}

object Kernel extends App {

  new ActorSchedulerRunnable().setup(0.second, 30.seconds, classOf[Day])

}
