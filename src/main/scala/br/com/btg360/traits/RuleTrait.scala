package br.com.btg360.traits

import br.com.btg360.constants._
import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.logger.Printer
import br.com.btg360.repositories.QueueRepository
import br.com.btg360.services._
import org.apache.spark.rdd.RDD

import scala.util.control.Breaks._

trait RuleTrait extends Serializable {

  protected var queue: QueueEntity = _

  private var userId: Int = 0

  private val queueRepository = new QueueRepository()

  private val periodService = new PeriodService()

  private val jsonService = new JsonService()

  private val port25Service = new Port25Service()

  private val referenceListService = new ReferenceListService()

  /**
    * @return List[Int]
    */
  def getTypes: List[Int]

  /**
    * @return Int
    */
  def getCompletedStatus: Int

  /**
    * @return RDD
    */
  def getData: RDD[(String, StockEntity)]

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

          this.startLog.flush.endLog
        }
      })
    } catch {
      case e: Exception => println(e.printStackTrace())
        this.queueRepository.updateStatus(this.queue.userRuleId.toInt, QueueStatus.CREATED)
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
  private def startLog: RuleTrait = {
    this.queue.logger = new Printer().inFile("%s/rules/%s/%d/%s/%d/%s.log".format(
      Path.LOGS,
      this.periodService.format("yyyy/MM").now,
      this.queue.userId,
      this.queue.ruleName,
      this.queue.userRuleId,
      this.periodService.format("dd").now
    ))
    println(Message.START_LOG)
    this
  }

  /**
    * @return this
    */
  private def endLog: RuleTrait = {
    println(Message.END_LOG)
    this
  }

  /**
    * @return this
    */
  private def flush: RuleTrait = {
    val channels = this.jsonService.decode[List[String]](this.queue.channels)
    println(this.queue.dataStringJson)
    channels.foreach(channel => {
      println(Message.CHANNEL_RUNNING.format(channel.toUpperCase))
      this.queue.channelName = channel
      this.all
    })
    this
  }

  /**
    * Processing of rules
    */
  private def all: Unit = {
    this.queueRepository.updateStatus(this.queue.userRuleId.toInt, QueueStatus.PROCESSED)
    var data = this.getData

    if (data == null || data.count() <= 0) {
      this.queueRepository.updateStatus(this.queue.userRuleId.toInt, this.getCompletedStatus)
      println(Message.ITEMS_NOT_FOUND)
      return
    }

    if (Channel.isEmail(this.queue.channelName)) {
      if (this.queue.ruleTypeId != Rule.AUTOMATIC_ID) {
        //data = this.referenceListService.add(this.queue, data)
      }

      data = this.port25Service.add(data)
    }

    data.foreach(row => {
      println(row._1 + " -> " + new JsonService().encode(row._2))
      println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
    })

    this.queueRepository.updateStatus(this.queue.userRuleId.toInt, this.getCompletedStatus)
  }

}