package br.com.btg360.services

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import br.com.btg360.application.Service

class PeriodService extends Service {

  private val calendar = Calendar.getInstance()

  private var _format: String = "yyyy-MM-dd HH:mm:ss"

  def this(format: String) {
    this()
    this._format = format
  }

  /**
    * @return String
    */
  def format: String = this._format

  /**
    * @param String value
    * @return PeriodService
    */
  def format_=(value: String): PeriodService = {
    this._format = value
    this
  }

  private def simpleDateFormat = new SimpleDateFormat(this._format)

  /**
    * Returns the current date
    *
    * @return String
    */
  def now: String = this.simpleDateFormat.format(new Date)

  /**
    * Returns a date based on hour
    *
    * @param Int hour
    * @return String
    */
  def timeByHour(hour: Int): String = {
    this.calendar.setTime(new Date)
    this.calendar.add(Calendar.HOUR_OF_DAY, hour)
    this.simpleDateFormat.format(this.calendar.getTime)
  }

  /**
    * Returns a date based on day
    *
    * @param Int day
    * @return String
    */
  def timeByDay(day: Int): String = {
    this.calendar.setTime(new Date)
    this.calendar.add(Calendar.DAY_OF_MONTH, day)
    this.simpleDateFormat.format(this.calendar.getTime)
  }

  /**
    * Returns a date based on month
    *
    * @param Int month
    * @return String
    */
  def timeByMonth(month: Int): String = {
    this.calendar.setTime(new Date)
    this.calendar.add(Calendar.MONTH, month)
    this.simpleDateFormat.format(this.calendar.getTime)
  }

  /**
    * Returns the daylight saving confirmation
    *
    * @param Int fromHour
    * @param Int toHour
    * @return Boolean
    */
  def isChangeDay(fromHour: Int, toHour: Int): Boolean = {
    val format = this._format
    this._format = "HH"
    val compare: Boolean = this.timeByHour(fromHour).toInt > this.timeByHour(toHour).toInt
    this._format = format
    compare
  }

}