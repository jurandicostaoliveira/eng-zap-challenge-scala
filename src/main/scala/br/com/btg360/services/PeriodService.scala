package br.com.btg360.services

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

class PeriodService {

  private val calendar = Calendar.getInstance()

  private var _format: String = "yyyy-MM-dd HH:mm:ss"

  def format: String = this._format

  def format_=(value: String): PeriodService = {
    this._format = value
    this
  }

  def this(format: String) {
    this()
    this._format = format
  }

  private def simpleDateFormat = new SimpleDateFormat(this._format)

  def now: String = this.simpleDateFormat.format(new Date)

  def timeByHour(hour: Int): String = {
    this.calendar.setTime(new Date)
    this.calendar.add(Calendar.HOUR_OF_DAY, hour)
    this.simpleDateFormat.format(this.calendar.getTime)
  }

  def timeByDay(day: Int): String = {
    this.calendar.setTime(new Date)
    this.calendar.add(Calendar.DAY_OF_MONTH, day)
    this.simpleDateFormat.format(this.calendar.getTime)
  }

  def timeByMonth(month: Int): String = {
    this.calendar.setTime(new Date)
    this.calendar.add(Calendar.MONTH, month)
    this.simpleDateFormat.format(this.calendar.getTime)
  }

  def isChangeDay(fromHour: Int, toHour: Int): Boolean = {
    val format = this._format
    this._format = "HH"
    val compare: Boolean = this.timeByHour(fromHour).toInt > this.timeByHour(toHour).toInt
    this._format = format
    compare
  }

}