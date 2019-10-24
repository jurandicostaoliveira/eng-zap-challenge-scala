package br.com.btg360.scheduling

import br.com.btg360.constants.{Path, QueueStatus, Time}
import br.com.btg360.entities.QueueEntity
import br.com.btg360.repositories.{ConsolidatedRepository, QueueRepository, UserRepository}
import br.com.btg360.services._
import br.com.btg360.traits.RunnableScheduleTrait
import br.com.btg360.worker.rule.{Daily, Hourly}
import br.com.btg360.spark.SparkCoreSingleton

import scala.concurrent.duration._

object Kernel extends App {

  SparkCoreSingleton.getContext

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
  scheduler.all("daily-rules", 20.minute, 1.hour, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      new Daily().dispatch(userId)
    }
  })

  /**
    * Hourly rules
    */
  scheduler.all("hourly-rules", 1.minute, 30.minutes, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      new Hourly().dispatch(userId)
    }
  })

  /**
    * Queue progress
    */
  scheduler.once("queue-progress", 1.hour, 55.minutes, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      new QueueProgressService()
        .statusFrom(List(QueueStatus.PREPARED, QueueStatus.RECOMMENDATION_STARTED, QueueStatus.PROCESSED))
        .statusTo(QueueStatus.RECOMMENDATION_PREPARED)
        .tolerance(2)
        .run()
    }
  })

  /**
    * Clear send limit
    */
  scheduler.once("clear-send-limit", 0.second, 30.minutes, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      if (Time.isMidnight) {
        new DailySendLimitService(new QueueEntity()).destroyNotCurrent
      }
    }
  })

  /**
    * Clear queue
    */
  scheduler.once("clear-queue", 0.second, 30.minutes, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      if (Time.isMidnight) {
        new QueueRepository().deleteOnDays(30)
      }
    }
  })

  /**
    * Clear consolidated tables
    */
  scheduler.once("clear-consolidated-tables", 0.second, 30.minutes, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      if (Time.isMidnight) {
        new ConsolidatedRepository().deleteOnDays(90)
      }
    }
  })

  /**
    * Log clear
    */
  scheduler.once("log-clear", 0.second, 30.days, new RunnableScheduleTrait {
    override def run(userId: Int): Unit = {
      val pattern = "%s/rules/%s"
      new FileService().deleteRecursive(
        pattern.format(Path.LOGS, new PeriodService("yyyy/MM").timeByMonth(-2))
      )

      val year = new PeriodService("yyyy")
      val oldYear = year.timeByMonth(-2)
      if (!oldYear.equals(year.now)) {
        new FileService().deleteRecursive(pattern.format(Path.LOGS, oldYear))
      }
    }
  })

  /**
    * Process disable clients
    */
//    scheduler.once("disable-clients", 1.hour, 1.day, new RunnableScheduleTrait {
//      override def run(userId: Int): Unit = {
//        new UserRepository().disableByAllin
//      }
//    })

}
