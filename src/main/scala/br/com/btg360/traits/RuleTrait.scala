package br.com.btg360.traits

import br.com.btg360.constants.{Message, Path, QueueStatus, Rule}
import br.com.btg360.entities.{ItemEntity, QueueEntity}
import br.com.btg360.logger.Printer
import br.com.btg360.repositories.{QueueRepository, ReferenceListRepository}
import br.com.btg360.services.{JsonService, PeriodService}
import org.apache.spark.rdd.RDD
import br.com.btg360.services.Port25Service

import scala.collection.immutable.Map

import scala.util.control.Breaks._

trait RuleTrait {

  protected var queue: QueueEntity = _

  private var userId: Int = 0

  private val queueRepository = new QueueRepository()

  private val periodService = new PeriodService()

  private val jsonService = new JsonService()

  private val port25Service = new Port25Service()

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
  def getData: RDD[(String, ItemEntity)]

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
    *
    */
  private def all: Unit = {
    //UPDATE STATUS
    var data = this.getData

    if (data.count() <= 0) {
      //UPDATE STATUS
      println(Message.ITEMS_NOT_FOUND)
      return
    }

    val referenceList: RDD[(String, Map[String, String])] = new ReferenceListRepository().allinId(1410).listId(2251344).getAll.map(row => {
      (row("nm_email"), row.toMap)
    })

    val a = data.leftOuterJoin(referenceList)

    val b = a.map(row => {
      val entity = row._2._1
      if (row._2._2.isDefined) {
        entity.addReferences(row._2._2.get)
      }
      (row._1, entity)
    })


    //data = this.port25Service.add(data)
    b.foreach(row => {
      println(row._1 + " : "+ row._2.products.size +" -> REF " + row._2.references)
    })


    //    data.foreach(row => {
    //      println(row._1 + " -> " + row._2.products.size)
    //    })
  }

}
