package br.com.btg360.scheduling

import br.com.btg360.services.QueueManagerService
import br.com.btg360.traits.RunnableScheduleTrait
import br.com.btg360.worker.rule.{Daily, Hourly}

import scala.concurrent.duration._

object Kernel extends App {

  /**
    * Queue generation
    */
  new Scheduler("queue-generation", 0.second, 15.minutes, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      new QueueManagerService().create(userId).create(userId, true)
    }
  })

  /**
    * Daily rules
    */
  new Scheduler("daily-rules", 1.minute, 1.hour, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      new Daily().dispatch(userId)
    }
  })

  /**
    * Hourly rules
    */
  new Scheduler("hourly-rules", 30.minutes, 1.hour, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      new Hourly().dispatch(userId)
    }
  })

}
