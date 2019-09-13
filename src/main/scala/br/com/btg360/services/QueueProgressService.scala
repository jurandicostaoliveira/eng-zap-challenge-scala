package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.constants.{QueueStatus, TypeConverter => TC}
import br.com.btg360.entities.QueueEntity
import br.com.btg360.repositories.QueueRepository

class QueueProgressService extends Service {

  val repository = new QueueRepository()

  var _statusFrom: List[Int] = List()

  var _statusTo: Int = 0

  var _tolerance: Int = 2

  /**
    * Getter
    *
    * @return Int
    */
  def statusFrom: List[Int] = this._statusFrom

  /**
    * Setter
    *
    * @param List list
    * @return this
    */
  def statusFrom(list: List[Int]): QueueProgressService = {
    this._statusFrom = list
    this
  }

  /**
    * Getter
    *
    * @return Int
    */
  def statusTo: Int = this._statusTo

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def statusTo(value: Int): QueueProgressService = {
    this._statusTo = value
    this
  }

  /**
    * Getter
    *
    * @return Int
    */
  def tolerance: Int = this._tolerance

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def tolerance(value: Int): QueueProgressService = {
    this._tolerance = value
    this
  }

  /**
    * @param QueueEntity queue
    * @return Int
    */
  private def findSendHour(queue: QueueEntity): Int = {
    queue.status match {
      case QueueStatus.CREATED => queue.createdIn.toString.substring(11, 13).toInt
      case QueueStatus.STARTED => queue.startedIn.toString.substring(11, 13).toInt
      case QueueStatus.PREPARED => queue.preparedIn.toString.substring(11, 13).toInt
      case QueueStatus.RECOMMENDATION_STARTED => queue.recommendationStartedIn.toString.substring(11, 13).toInt
      case QueueStatus.RECOMMENDATION_PREPARED => queue.recommendationPreparedIn.toString.substring(11, 13).toInt
      case QueueStatus.PROCESSED => queue.processedIn.toString.substring(11, 13).toInt
    }
  }

  /**
    * Runnable
    */
  def run(): Unit = {
    try {
      val currentHour = TC.toInt(new PeriodService("HH").now)
      val rows = this.repository.findCurrentByStatus(this.statusFrom)

      rows.foreach(row => {
        if ((currentHour - this.findSendHour(row)) >= this.tolerance) {
          this.repository.updateStatus(row.userRuleId.toInt, this.statusTo)
        }
      })
    } catch {
      case e: Exception => println(e.printStackTrace())
    }
  }

}
