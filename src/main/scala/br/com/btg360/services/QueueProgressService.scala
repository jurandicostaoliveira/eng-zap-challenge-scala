package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.constants.{QueueStatus, TypeConverter => TC}
import br.com.btg360.repositories.QueueRepository

class QueueProgressService extends Service {

  val repository = new QueueRepository()

  var _tolerance: Int = 2

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
    * Runnable
    */
  def run(): Unit = {
    try {
      val currentHour = TC.toInt(new PeriodService("HH").now)
      val rows = this.repository.findCurrentByStatus(
        List(QueueStatus.PREPARED, QueueStatus.RECOMMENDATION_STARTED)
      )

      rows.foreach(row => {
        val sendHour: Int = row.status match {
          case QueueStatus.PREPARED => row.preparedIn.toString.substring(11, 13).toInt
          case QueueStatus.RECOMMENDATION_STARTED => row.recommendationStartedIn.toString.substring(11, 13).toInt
        }

        if ((currentHour - sendHour) >= tolerance) {
          this.repository.updateStatus(row.userRuleId.toInt, QueueStatus.RECOMMENDATION_PREPARED)
        }
      })
    } catch {
      case e: Exception => println(e.printStackTrace())
    }
  }

}
