package br.com.btg360.traits

import br.com.btg360.constants.{QueueStatus, Rule}
import br.com.btg360.entities.QueueEntity
import br.com.btg360.logger.PrintLogger
import br.com.btg360.repositories.QueueRepository
import br.com.btg360.services.PeriodService
import org.apache.log4j.{Level, Logger, RollingFileAppender, SimpleLayout}

import scala.util.control.Breaks._


trait RuleTrait {

  private var userId: Int = 0

  private var queue: QueueEntity = _

  private val queueRepository = new QueueRepository()

  private val periodService = new PeriodService()

  /**
    * @return List[Int]
    */
  def getTypes: List[Int]

  /**
    * @return Int
    */
  def getCompletedStatus: Int

  //def getData: String

  /**
    * Run application
    *
    * @param userId
    */
  def dispatch(userId: Int): Unit = {
    try {
      this.userId = userId
      val rows = this.queueRepository.findAll(userId, this.getTypes)

      rows.foreach(row => {
        this.queue = row.parse
        breakable {
          if (this.isDailyAndNotReady) {
            return
          }
          if (this.isHourlyAndNotReady) {
            break
          }

          this.createLog.channels
        }
      })
    } catch {
      case e: Exception => println(e.printStackTrace())
    }
  }

  /**
    * @return Boolean
    */
  private def isDailyAndNotReady: Boolean = {
    this.queue.ruleTypeId != Rule.HOURLY_ID && this.queue.status != QueueStatus.RECOMMENDATION_PREPARED
  }

  /**
    * @return Boolean
    */
  private def isHourlyAndNotReady: Boolean = {
    this.queue.ruleTypeId == Rule.HOURLY_ID && this.queue.status != QueueStatus.RECOMMENDATION_PREPARED
  }

  /**
    * @return this
    */
  private def createLog: RuleTrait = {
    val path = "/home/Btg-Scala-Sending-Generator/storage/logs/rules/%s/%d/%s/%d/%s.log".format(
      this.periodService.format("yyyy/MM").now,
      this.queue.userId,
      this.queue.ruleName,
      this.queue.userRuleId,
      this.periodService.format("dd").now
    )

    PrintLogger.create(path)

//    val layout: SimpleLayout = new SimpleLayout()
//    val appender: RollingFileAppender = new RollingFileAppender(layout, path, true)
//    this.logger.addAppender(appender)
//
//    this.logger.setLevel(Level.INFO)
//
//    this.logger.info("OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK")


    //    println(path)
    //    System.setProperty("LOG_CUSTOM_NAME", path)
    //    val LOG = Logger.getRootLogger()
    //    PrintLogger.create(LOG)
    this
  }

  private def channels: Unit = {
    println(this.queue.ruleName)
  }

  private def build: Unit = {

  }

}
