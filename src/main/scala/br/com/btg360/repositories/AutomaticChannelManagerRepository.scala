package br.com.btg360.repositories

import java.sql.{Connection, ResultSet}
import java.text.{SimpleDateFormat}
import java.util.{Calendar, Locale}

import br.com.btg360.constants.{Automatic => AT}
import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, Table}
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

class AutomaticChannelManagerRepository extends Repository {

  private val db = new MySqlAllin()

  val sc = SparkCoreSingleton.getContext

  val channelManagerRepository: ChannelManagerRepository = new ChannelManagerRepository(this.sc.getConf, null)

  val periodService: PeriodService = new PeriodService()

  val apiChannelManager: String = "http://api-channelmanager.allin.com.br/channel/filter/"

  val apiChannelManagerToken: String = "10785ea213668ef0bb3d11f8f0e1a9cbbaff67b7d25fe74c12e4d9f8015763ca1bb39eadd740a2049fa1daa85c2fc28cbbd1676a9df5cf636acd66728f00ecd5"

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

      val data = this.getDataChannelManager(this.listId, this.getFilter(AT.BIRTHDAY))

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

      val data = this.getDataChannelManager(this.listId, this.getFilter(AT.SENDING_DATE))

      this.execExclusion(data)

    } catch {
      case e: Exception => {
        println("ERROR EXEC SENDING-DATE: " + e)
        sc.emptyRDD[(String, StockEntity)]
      }
    }
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
      s""" ${this.apiChannelManager}${this.filterId}?customerId=${this.allinId}&listId=${this.listId} """
    )
      .setHeader("Authorization", this.apiChannelManagerToken)
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

    "("+this.field+":"+this.periodService.format("yyyy-MM-dd").timeByDay(-this.interval)+"T00\\:00\\:00Z AND config.isEmailAuthorized:true)"
  }

  //Erro de No Task Serializable se usar função auxiliar pro map
  def getDataChannelManager(list: Int, filter: String): RDD[(String, StockEntity)] = {

    try {

      this.channelManagerRepository.get(list.toString, this.allinId, false, filter).rdd.map(json => {
        implicit val formats = DefaultFormats

        val data = parse(json).asInstanceOf[JObject]

        val map = data.extract[Map[String, Any]]

        val stockEntity = new StockEntity(references = map)

        ((data \ "email").extract[String], stockEntity)
      })

    } catch {
      case _: Exception =>
        this.channelManagerRepository.get(list.toString, this.allinId, false, filter).rdd.map(json => {
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

    data.subtractByKey(this.getDataChannelManager(this.listExclusionId,""))
  }
}