package br.com.btg360.repositories

import java.sql.{Connection, ResultSet}
import java.text.SimpleDateFormat
import java.util.{Calendar, Locale}

import br.com.btg360.constants.{Database, Table, Token, Url, Automatic => AT}
import br.com.btg360.application.Repository
import br.com.btg360.entities.StockEntity
import br.com.btg360.jdbc.MySqlAllin
import br.com.btg360.services.PeriodService
import br.com.btg360.spark.SparkCoreSingleton
import com.m3.curly.{HTTP, Request}
import org.apache.spark.rdd.RDD
import org.json4s.DefaultFormats
import org.json4s.JsonAST.{JArray, JObject}
import org.json4s.jackson.JsonMethods._
import br.com.allin.ChannelManagerRepository.ChannelManagerRepository

import scala.collection.mutable.{ArrayBuffer}
import collection.JavaConverters._

class AutomaticChannelManagerRepository extends Repository {

  private val db = new MySqlAllin()

  val sc = SparkCoreSingleton.getContext

  val channelManagerRepository: ChannelManagerRepository = new ChannelManagerRepository(this.sc.getConf, null)

  val periodService: PeriodService = new PeriodService()

  var allinId: Int = _

  var listId: Int = _

  var listExclusionId: Int = _

  var field: String = _

  var formatField: String = _

  var filterId: Int = _

  var interval: Int = _

  def setAllinId(allinId: Int): AutomaticChannelManagerRepository = {
    this.allinId = allinId
    this
  }

  def setListId(listId: Int): AutomaticChannelManagerRepository = {
    this.listId = listId
    this
  }

  def setListExclusionId(listExclusionId: Int): AutomaticChannelManagerRepository = {
    this.listExclusionId = listExclusionId
    this
  }

  def setField(field: String): AutomaticChannelManagerRepository = {
    this.field = field
    this
  }

  def setFormatField(formatField: String): AutomaticChannelManagerRepository = {
    this.formatField = formatField
    this
  }

  def setFilterId(filterId: Int): AutomaticChannelManagerRepository = {
    this.filterId = filterId
    this
  }

  def setInterval(interval: Int): AutomaticChannelManagerRepository = {
    this.interval = interval
    this
  }

  def isMigratedChannelManager: Boolean = {
    var total: Int = 0
    try {
      val conn: Connection = this.db.open
      val query: String = s"""
          SELECT COUNT(*) AS total FROM ${Database.MAIL_SENDER}.${Table.CHANNEL_MANAGER_MIGRATION} WHERE
          is_migrated_channel_mysql   = 1 AND
          is_migrated_channel_mongodb = 1 AND
          date_migrated_channel_mongodb < '${periodService.now}' AND
          is_migrated_list_mysql      = 1 AND
          is_migrated_list_mongodb    = 1 AND
          date_migrated_list_mongodb  < '${periodService.now}' AND
          id_login = ${this.allinId};
        """

      val rs: ResultSet = this.connection(conn).queryExecutor(query)
      while (rs.next()) {
        total = rs.getInt("total")
      }
      conn.close()
      total > 0
    } catch {
      case e: Exception => println(e.printStackTrace())
        false
    }
  }

  def findBirthday: RDD[(String, StockEntity)] = {

    try {

      val dataList = this.isListActive(this.listId)

      this.fieldExists(dataList)

      val data = this.getDataChannelManager(this.listId, this.getFilter(AT.BIRTHDAY), this.getFields(dataList))

      this.execExclusion(data)

    } catch {
      case e: Exception => {
        println("ERROR EXEC BIRTHDAY: " + e)
        sc.emptyRDD[(String, StockEntity)]
      }
    }
  }

  def findSendingDate: RDD[(String, StockEntity)] = {
    try {

      val dataList = this.isListActive(this.listId)

      this.fieldExists(dataList)

      val data = this.getDataChannelManager(this.listId, this.getFilter(AT.SENDING_DATE), this.getFields(dataList))

      this.execExclusion(data)

    } catch {
      case e: Exception => {
        println("ERROR EXEC SENDING-DATE: " + e)
        sc.emptyRDD[(String, StockEntity)]
      }
    }
  }

  def findInactive(activity: RDD[(String, StockEntity)]): RDD[(String, StockEntity)] = {
    try {

      val dataList = this.isListActive(this.listId)

      val data = this.getDataChannelManager(this.listId, this.getFilter(AT.INACTIVE), this.getFields(dataList))

      this.execExclusion(data.join(activity).map(row => (row._1, row._2._1)))

    } catch {
      case e: Exception => {
        println("ERROR EXEC INACTIVE: " + e)
        sc.emptyRDD[(String, StockEntity)]
      }
    }
  }

  def isListActive(listId: Int): String = {

    val response = HTTP.get(this.getRequestListIsActive(listId))

    if( response.getStatus != 200 ) {
      throw new Exception()
    }

    this.isListActiveJson(response.getTextBody)

    response.getTextBody
  }

  def getRequestListIsActive(listId: Int): Request = {
    new Request(
      s""" ${Url.API_CHANNEL_MANAGER}/list/${listId}?customerId=${this.allinId} """
    )
      .setHeader("Authorization", Token.API_CHANNEL_MANAGER)
      .setHeader("cache-control", "no-cache")
  }

  def isListActiveJson(json: String) = {

    implicit val formats = DefaultFormats

    try {

      val data = parse(json).asInstanceOf[JObject]

      val active = (data \ "result" \ "active").extract[Boolean]

      val archived = (data \ "result" \ "archived").extract[Boolean]

      if (!active || archived) {
        throw new Exception()
      }

    } catch {
      case _: Exception => throw new Exception()
    }
  }

  def fieldExists(json: String): Boolean = {

    implicit val formats = DefaultFormats

    try {

      val data = parse(json).asInstanceOf[JObject]

      (data \ "result" \ "fields").asInstanceOf[JArray].arr.foreach{
        field => {

          val name = (field \ "name").extract[String]

          if( this.field == name ) {

            return true;
          }
        }
      }

      throw new Exception()

    } catch {
      case _: Exception => throw new Exception()
    }
  }

  def getFields(json: String): ArrayBuffer[String] = {

    implicit val formats = DefaultFormats

    val fields = new ArrayBuffer[String]()

    try {

      val data = parse(json).asInstanceOf[JObject]

      (data \ "result" \ "fields").asInstanceOf[JArray].arr.foreach{
        field => {

          fields += (field \ "name").extract[String]
        }
      }

    } catch {
      case _: Exception => {

        return null;
      }
    }

    fields
  }

  def getFilter(automaticType: String): String = {
    try {
      this.getStringFilter + this.getPeriod(automaticType)
    } catch {
      case _: Exception => throw new Exception("Exception in getFilter ChannelManager - ALLIN: " + this.allinId)
    }
  }

  def getStringFilter: String = {

    if( this.filterId == 0 ) {
      return ""
    }

    try {
      getStringFilterExecute
    } catch {
      case _: Exception => getStringFilterExecute
    }
  }

  def getStringFilterExecute: String = {

    val response = HTTP.get(this.getRequestFilter)

    if( response.getStatus != 200 ) {
      throw new Exception()
    }

    s"""(${this.getFilterInJson(response.getTextBody)}) AND """
  }

  def getRequestFilter: Request = {
    new Request(
      s""" ${Url.API_CHANNEL_MANAGER}/channel/filter/${this.filterId}?customerId=${this.allinId}&listId=${this.listId} """
    )
      .setHeader("Authorization", Token.API_CHANNEL_MANAGER)
      .setHeader("cache-control", "no-cache")
  }

  def getFilterInJson(json: String): String = {

    implicit val formats = DefaultFormats

    var filter : String = ""

    try {

      val data = parse(json).asInstanceOf[JObject]

      (data \ "result").asInstanceOf[JArray].arr.foreach{
        f => filter = f.extract[String]
      }

    } catch {
      case _: Exception => throw new Exception()
    }

    if( filter == "" ) {
      throw new Exception()
    }

    filter
  }

  def getPeriod(automaticType: String): String = {

    if(AT.BIRTHDAY == automaticType) {

      val dayOfMonth = if ( Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10 )
        "0" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) else Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

      return "(" + this.field + "_copy:*\\ " +
        new SimpleDateFormat("MMM", Locale.ENGLISH).format(Calendar.getInstance().getTime()) +
        "\\ " + dayOfMonth +
        "\\ 00\\:00\\:00\\ UTC\\ * AND config.isEmailAuthorized:true)"
    }

    if(AT.SENDING_DATE == automaticType) {

      return "("+this.field+":"+this.periodService.format("yyyy-MM-dd").timeByDay(-this.interval)+"T00\\:00\\:00Z AND config.isEmailAuthorized:true)"
    }

    "(config.isEmailAuthorized:true)"
  }

  def getDataChannelManager(list: Int, filter: String, fields: ArrayBuffer[String]): RDD[(String, StockEntity)] = {

    try {

      this.channelManagerRepository.get(list.toString, this.allinId, false, filter, fields.asJava).rdd.map(json => {
        implicit val formats = DefaultFormats

        val data = parse(json).asInstanceOf[JObject]

        val map = data.extract[Map[String, Any]]

        val stockEntity = new StockEntity(references = map)

        ((data \ "email").extract[String], stockEntity)
      })

    } catch {
      case _: Exception =>
        this.channelManagerRepository.get(list.toString, this.allinId, false, filter, fields.asJava).rdd.map(json => {
          implicit val formats = DefaultFormats

          val data = parse(json).asInstanceOf[JObject]

          val map = data.extract[Map[String, Any]]

          val stockEntity = new StockEntity(references = map)

          ((data \ "email").extract[String], stockEntity)
        })
    }
  }

  def execExclusion(data: RDD[(String, StockEntity)]): RDD[(String, StockEntity)] = {

    if( this.listExclusionId == 0 || this.listExclusionId == this.listId ) {
      return data
    }

    try {

      this.isListActive(this.listExclusionId)

    } catch {
      case _: Exception =>
        return data
    }

    data.subtractByKey(this.getDataChannelManager(this.listExclusionId, "", ArrayBuffer("email")))
  }
}