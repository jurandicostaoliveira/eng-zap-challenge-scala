package br.com.btg360.traits

import br.com.btg360.application.Application
import br.com.btg360.constants._
import br.com.btg360.entities.{QueueEntity, StockEntity, UserEntity}
import br.com.btg360.logger.Log4jPrinter
import br.com.btg360.repositories.{ConsolidatedRepository, QueueRepository, UserRepository}
import br.com.btg360.services._
import org.apache.spark.rdd.RDD

import scala.util.control.Breaks._

trait RuleTrait extends Application {

  protected var user: UserEntity = _

  protected var queue: QueueEntity = _

  private val userRepository = new UserRepository()

  private val queueRepository = new QueueRepository()

  private val consolidatedRepository = new ConsolidatedRepository()

  private val periodService = new PeriodService()

  private val jsonService = new JsonService()

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
      this.user = this.userRepository.findById(userId)
      val rows = this.queueRepository.findAll(userId, this.getTypes)

      /**
        * Update queue hourly
        */
      rows.foreach(row => {
        if (row.ruleTypeId == Rule.HOURLY_ID && row.status == QueueStatus.RECOMMENDATION_PREPARED) {
          this.queueRepository.updateStatus(row.userRuleId.toInt, QueueStatus.PROCESSED)
        }
      })

      /**
        * Execute all
        */
      rows.foreach(row => {
        this.queue = row.parse
        this.queue.rule.transactionalId = this.user.transId
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
        this.toException
    }
  }

  /**
    * When there is an exception in the middle of the routine
    */
  private def toException: Unit = {
    if (this.queue.ruleTypeId == Rule.HOURLY_ID) {
      this.queueRepository.updateStatus(this.queue.userRuleId.toInt, QueueStatus.CREATED)
    } else {
      this.queueRepository.updateStatus(this.queue.userRuleId.toInt, QueueStatus.RECOMMENDATION_PREPARED)
      this.consolidatedRepository.table(this.queue.getConsolidatedTable).updateSubmitted(
        0, this.queue.platformId
      )
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
    Log4jPrinter.configure("%s/rules/%s/%d/%s/%d/%s.log".format(
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
    /**
      * Validate current status only daily rules
      */
    val currentStatus: Int = this.queueRepository.findByUserRuleId(this.queue.userRuleId.toInt).status
    if (this.queue.ruleTypeId != Rule.HOURLY_ID && currentStatus != QueueStatus.RECOMMENDATION_PREPARED) {
      println(Message.STATUS_IS_UNAVAILABLE_TO_PROCESS.format(currentStatus))
      return this
    }

    val channels = this.jsonService.decode[List[String]](this.queue.channels)
    println(this.queue.dataStringJson)
    channels.foreach(channel => {
      println(Message.CHANNEL_RUNNING.format(channel.toUpperCase))
      this.queue.channelName = channel
      this.queue.platformId = Channel.getPlatformId(channel)
      this.all
    })

    this
  }

  /**
    * Return mapping all filters
    *
    * @param RDD data
    * @return RDD
    */
  private def filter(data: RDD[(String, StockEntity)]): RDD[(String, StockEntity)] = {
    var keys: RDD[String] = data.keys

    if (Rule.isRemoveDailyLimit(this.queue.groupId.toInt)) {
      keys = new DailySendLimitService(this.queue).filter(keys)
      println(Message.TOTAL_DAILY_LIMIT_REMOVED.format(keys.count()))
    }

    if (Channel.isEmail(this.queue.channelName)) {
      keys = new OptoutService(this.queue).filter(keys)
      println(Message.TOTAL_OPTOUT_REMOVED.format(keys.count()))
    }

    keys.map(key => (key, 0)).join(data).map(row => {
      (row._1, row._2._2)
    })
  }

  /**
    * Apply extra structures
    *
    * @param RDD data
    * @return RDD
    */
  private def apply(data: RDD[(String, StockEntity)]): RDD[(String, StockEntity)] = {
    var rdd = data
    if (Channel.isEmail(this.queue.channelName)) {
      if (this.queue.ruleTypeId != Rule.AUTOMATIC_ID) {

        /**
          * Hack para o Hotel Urbano
          */
        if (List(2).contains(this.queue.userId)) {
          rdd = new ReferenceListService().add(this.queue, rdd)
          println(Message.APPLIED_REFERENCE_LIST_SUCCESSFULLY + " TO HU")
        } else {

          try {
            rdd = new SolrReferenceListService()
              .allinId(this.queue.rule.allinId)
              .listId(this.queue.rule.referenceListId)
              .add(rdd, false)

            println(Message.APPLIED_REFERENCE_LIST_SUCCESSFULLY)
          } catch {
            case e: Exception => println(e.printStackTrace())
              rdd = data
          }
        }

      }

      rdd = new Port25Service().add(rdd)
      println(Message.APPLIED_PORT_25_SUCCESSFULLY)
    }
    rdd
  }

  /**
    * Processing of rules
    */
  private def all: Unit = {
    if (List(Rule.DAILY_ID, Rule.AUTOMATIC_ID).contains(this.queue.ruleTypeId)) {
      this.queueRepository.updateStatus(this.queue.userRuleId.toInt, QueueStatus.PROCESSED)
    }

    val data = this.getData
    if (data == null || data.count() <= 0) {
      this.queueRepository.updateStatus(this.queue.userRuleId.toInt, this.getCompletedStatus)
      println(Message.ITEMS_NOT_FOUND)
      return
    }

    new TransactionalService().persist(this.queue, this.apply(this.filter(data)))

    this.queueRepository.updateStatus(this.queue.userRuleId.toInt, this.getCompletedStatus)
    println(Message.UPDATE_RULE_STATUS.format(this.queue.userRuleId.toInt, this.getCompletedStatus))

    if (this.queue.ruleTypeId != Rule.AUTOMATIC_ID) {
      this.consolidatedRepository.table(this.queue.getConsolidatedTable).updateSubmitted(
        1, this.queue.platformId
      )
    }

    println(Message.UPDATE_CONSOLIDATED_STATUS.format(1, this.queue.platformId))
  }

}