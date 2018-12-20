package br.com.btg360.scheduling

import br.com.btg360.services.QueueManagerService
import br.com.btg360.traits.SchedulerContextTrait

import scala.concurrent.duration._


object Kernel extends App {

  new Scheduler("daily-rules", 0.second, 30.seconds, new SchedulerContextTrait {
    override def run(userId: Int): Unit = {
      new QueueManagerService().create(userId).create(userId, true)
    }
  })

}
