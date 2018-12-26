package br.com.btg360.scheduling

import br.com.btg360.entities.QueueEntity
import br.com.btg360.repositories.{ConsolidatedRepository, QueueRepository}
import br.com.btg360.services.{DailySendLimitService, PeriodService, QueueManagerService}
import br.com.btg360.traits.RunnableScheduleTrait
import br.com.btg360.worker.rule.{Daily, Hourly}

import scala.concurrent.duration._

object Kernel extends App {

  val scheduler = new Scheduler()

  /**
    * Queue generation
    */
  scheduler.all("queue-generation", 0.second, 15.minutes, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      new QueueManagerService().create(userId).create(userId, true)
    }
  })

  /**
    * Daily rules
    */
  scheduler.all("daily-rules", 1.minute, 1.hour, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      new Daily().dispatch(userId)
    }
  })

  /**
    * Hourly rules
    */
  scheduler.all("hourly-rules", 30.minutes, 1.hour, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      new Hourly().dispatch(userId)
    }
  })

  /**
    * Clear send limit
    */
  scheduler.once("clear-send-limit", 0.second, 30.minutes, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      if (new PeriodService("HH").now == "00") {
        new DailySendLimitService(new QueueEntity()).destroyNotCurrent
      }
    }
  })

  /**
    * Clear queue
    */
  scheduler.once("clear-queue", 0.second, 30.minutes, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      if (new PeriodService("HH").now == "00") {
        new QueueRepository().deleteOnDays(30)
      }
    }
  })

  /**
    * Clear consolidated tables
    */
  scheduler.once("clear-consolidated-tables", 0.second, 30.minutes, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      if (new PeriodService("HH").now == "00") {
        new ConsolidatedRepository().deleteOnDays(90)
      }
    }
  })

}
