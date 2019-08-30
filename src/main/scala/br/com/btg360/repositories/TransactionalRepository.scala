package br.com.btg360.repositories

import java.net.URLEncoder

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Base64Converter, Channel, Database, Url, TypeConverter => TC}
import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.jdbc.MySqlAllin
import br.com.btg360.services.{JsonService, PeriodService}
import org.apache.spark.rdd.RDD

import scala.collection.mutable.HashMap
import scala.util.Random

class TransactionalRepository extends Repository {

  private val db = new MySqlAllin().open

  private var _batchLimit: Int = 10000

  private var _queue: QueueEntity = _

  private var _data: RDD[(String, StockEntity)] = _

  private var _templateId: Int = 0

  private var _themeConfigs: Map[String, Any] = Map()

  /**
    * Getter
    *
    * @return
    */
  def batchLimit: Int = this._batchLimit

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def batchLimit(value: Int): TransactionalRepository = {
    this._batchLimit = value
    this
  }

  /**
    * Getter
    *
    * @return QueueEntity
    */
  def queue: QueueEntity = this._queue

  /**
    * Setter
    *
    * @param QueueEntity value
    * @return this
    */
  def queue(value: QueueEntity): TransactionalRepository = {
    this._queue = value
    this
  }

  /**
    * Getter
    *
    * @return RDD
    */
  def data: RDD[(String, StockEntity)] = this._data

  /**
    * Setter
    *
    * @param RDD value
    * @return this
    */
  def data(value: RDD[(String, StockEntity)]): TransactionalRepository = {
    this._data = value
    this
  }

  /**
    * Getter
    *
    * @return Int
    */
  def templateId: Int = this._templateId

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def templateId(value: Int): TransactionalRepository = {
    this._templateId = value
    this
  }

  /**
    * Getter
    *
    * @return Map
    */
  def themeConfigs: Map[String, Any] = this._themeConfigs

  /**
    * Setter
    *
    * @param Map value
    * @return this
    */
  def themeConfigs(value: Map[String, Any]): TransactionalRepository = {
    this._themeConfigs = value
    this
  }

  /**
    * @param String format
    * @return String
    */
  private def now(format: String = "yyyy-MM-dd HH:mm:ss") = new PeriodService(format).now

  /**
    * @return String
    */
  private def currentMonth: String = this.now("MM")

  /**
    * @return String
    */
  private def generateSendTable: String = {
    "%s.cor_envio_trans_%d_%s".format(Database.POSTMATIC, this.queue.rule.transactionalId, this.currentMonth)
  }

  /**
    * @return String
    */
  private def generateClickTable: String = {
    "%s.clique_%s_%s".format(Database.POSTMATIC, this.queue.rule.transactionalId, this.currentMonth)
  }

  /**
    * @return String
    */
  private def generateTemplateTable: String = {
    "%s.cor_template_%s".format(Database.POSTMATIC, this.queue.rule.transactionalId)
  }

  /**
    * @return String
    */
  private def generateLoginTable: String = {
    "%s.cor_login_transacional".format(Database.POSTMATIC)
  }

  /**
    * @return this
    */
  def createTemplateTable: TransactionalRepository = {
    val query: String =
      s"""CREATE TABLE IF NOT EXISTS ${this.generateTemplateTable} (
        `id_template` int(11) NOT NULL AUTO_INCREMENT,
        `nm_template` varchar(200) DEFAULT NULL,
        `nm_html` longtext,
        `fl_descartar` int(11) DEFAULT NULL,
        `dt_cadastro` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `fl_twig` int(11) DEFAULT '0',
        PRIMARY KEY (`id_template`),
        UNIQUE KEY `template` (`nm_template`) USING BTREE,
        KEY `nmtemplate` (`nm_template`),
        KEY `idtemplate` (`id_template`),
        KEY `fldescartar` (`fl_descartar`),
        KEY `fltwig` (`fl_twig`)
    ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;"""

    this.connection(this.db).queryExecutor(query, true)
    this
  }

  /**
    * @return this
    */
  def createClickTable: TransactionalRepository = {
    val query: String =
      s"""CREATE TABLE IF NOT EXISTS ${this.generateClickTable} (
              `id_clique` int(11) NOT NULL AUTO_INCREMENT,
              `id_envio` int(11) DEFAULT NULL,
              `dt_clique` datetime DEFAULT NULL,
              `nm_url` varchar(350) DEFAULT NULL,
              PRIMARY KEY (`id_clique`),
              UNIQUE KEY `clique_unico` (`id_envio`,`dt_clique`,`nm_url`),
              KEY `id_envio` (`id_envio`),
              KEY `dt_clique` (`dt_clique`)
            ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;"""

    this.connection(this.db).queryExecutor(query, true)
    this
  }

  /**
    * @return this
    */
  def createSendTable: TransactionalRepository = {
    val query: String =
      s"""CREATE TABLE IF NOT EXISTS ${this.generateSendTable} (
           `id_envio` int(11) NOT NULL AUTO_INCREMENT,
           `id_tipo_envio` int(11) DEFAULT NULL,
           `id_plataforma` int(11) DEFAULT NULL,
           `nm_envio` varchar(250) DEFAULT NULL,
           `nm_email` varchar(250) DEFAULT NULL,
           `nm_celular` varchar(250) DEFAULT NULL,
           `nm_push` text,
           `nm_push_md5` varchar(50) DEFAULT NULL,
           `nm_facebook` varchar(100) DEFAULT NULL,
           `id_template` int(11) DEFAULT NULL,
           `nm_subject` varchar(350) DEFAULT NULL,
           `nm_remetente` varchar(150) DEFAULT NULL,
           `email_remetente` varchar(150) DEFAULT NULL,
           `nm_reply` varchar(150) DEFAULT NULL,
           `fl_envio` int(11) DEFAULT NULL,
           `dt_envio` date DEFAULT NULL,
           `hr_envio` time DEFAULT NULL,
           `campos` text,
           `valor` text,
           `nm_titulo` varchar(200) DEFAULT NULL,
           `nm_mensagem` text,
           `url_scheme` varchar(250) DEFAULT NULL,
           `valor_json` longtext,
           `valor_serialize` text,
           `dt_enviado` datetime DEFAULT NULL,
           `dt_abertura` datetime DEFAULT NULL,
           `status_msg` text,
           `id_cv_envio` int(11) DEFAULT NULL,
           `id_cv_acao` int(11) DEFAULT NULL,
           `fl_cv_my` char(7) DEFAULT NULL,
           `fl_cv_type` int(11) DEFAULT NULL,
           `id_workflow` int(11) DEFAULT NULL,
           `id_seletor` int(11) DEFAULT NULL,
           `id_envio_anterior` int(11) unsigned DEFAULT NULL,
           `fl_processado` int(11) DEFAULT NULL,
           `btg_user_rule_id` int(11) DEFAULT NULL,
           PRIMARY KEY (`id_envio`),
           UNIQUE KEY `id_cv_envio_2` (`id_cv_envio`,`id_cv_acao`,`fl_cv_my`),
           UNIQUE KEY `nm_email` (`nm_email`,`nm_celular`,`nm_push_md5`,`nm_facebook`,`id_workflow`,`id_seletor`,`dt_envio`,`hr_envio`),
           UNIQUE KEY `nm_email_2` (`nm_email`,`nm_celular`,`nm_push_md5`,`nm_facebook`,`id_workflow`,`id_seletor`,`id_envio_anterior`),
           UNIQUE KEY `evita_repeticoes` (`id_template`,`nm_email`,`nm_celular`,`nm_push_md5`,`dt_envio`,`hr_envio`),
           KEY `id_tipo_envio` (`id_tipo_envio`),
           KEY `id_plataforma` (`id_plataforma`),
           KEY `data_enviado` (`dt_enviado`),
           KEY `data_envio` (`dt_envio`),
           KEY `hora_envio` (`hr_envio`),
           KEY `template` (`id_template`),
           KEY `email` (`nm_email`),
           KEY `celular` (`nm_celular`),
           KEY `push` (`nm_push_md5`),
           KEY `facebook` (`nm_facebook`),
           KEY `fl_cv_my` (`fl_cv_my`),
           KEY `fl_cv_type` (`fl_cv_type`),
           KEY `id_cv_envio` (`id_cv_envio`),
           KEY `id_cv_acao` (`id_cv_acao`),
           KEY `id_workflow` (`id_workflow`),
           KEY `id_seletor` (`id_seletor`),
           KEY `id_envio_anterior` (`id_envio_anterior`),
           KEY `btg_user_rule_id` (`btg_user_rule_id`),
           KEY `dthr_envio` (`dt_envio`,`hr_envio`),
           KEY `dt_abertura` (`dt_abertura`)
         ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;"""

    this.connection(this.db).queryExecutor(query, true)
    this
  }

  /**
    * @return this
    */
  def alterSendTable: TransactionalRepository = {
    val table: String = this.generateSendTable
    val column = "btg_user_rule_id"
    if (!this.columnExists(table, column)) {
      this.connection(this.db)
        .queryExecutor(s"ALTER TABLE $table ADD COLUMN $column int(11) DEFAULT NULL;", true)
    }
    this
  }

  /**
    *
    */
  def updateLastSend: Unit = {
    this.connection(this.db)
      .whereAnd("id_allinmail", "=", this.queue.rule.allinId)
      .update(this.generateLoginTable, HashMap("dt_ult_envio" -> this.now()))
  }

  /**
    * Persist template
    *
    * @return Int
    */
  def saveTemplate(template: String): Int = {
    val rand: Long = new Random().nextInt((50000 - 1000) * 38)
    this.connection(this.db).insertGetId(this.generateTemplateTable, HashMap(
      "nm_template" -> "BTG_template_%s_%d".format(this.now("yyyy_MM_dd_HH_mm"), rand),
      "nm_html" -> template,
      "fl_descartar" -> 0,
      "dt_cadastro" -> this.now(),
      "fl_twig" -> 1
    ))
  }

  /**
    * @param Int templateId
    * @return Map
    */
  private def createSendData(user: String, stock: StockEntity): Map[String, Any] = {
    val channelMap = this.queue.rule.channelMap
    val client = Base64Converter.encode(user)
    Map(
      "nm_envio" -> "BTG:%s:%s".format(this.queue.ruleName, this.queue.rule.latinName),
      "nm_subject" -> this.queue.rule.subject,
      "nm_remetente" -> this.queue.rule.senderName,
      "email_remetente" -> this.queue.rule.senderEmail,
      "nm_reply" -> this.queue.rule.replyEmail,
      "dt_envio" -> this.queue.today,
      "hr_envio" -> this.queue.deliveryHourAt,
      "nm_titulo" -> TC.toString(channelMap(this.queue.channelName).subject),
      "nm_mensagem" -> TC.toString(channelMap(this.queue.channelName).message),
      "url_scheme" -> TC.toString(channelMap(this.queue.channelName).urlScheme),
      "id_tipo_envio" -> Channel.all(this.queue.channelName),
      "id_template" -> this.templateId,
      "btg_user_rule_id" -> this.queue.userRuleId,
      "nm_email" -> this.fillUser(user, List(Channel.EMAIL)),
      "nm_celular" -> this.fillUser(user, List(Channel.SMS)),
      "nm_push" -> this.fillUser(user, List(Channel.PUSH_ANDROID, Channel.PUSH_IOS)),
      "nm_facebook" -> this.fillUser(user, List(Channel.FACEBOOK)),
      "valor_json" -> new JsonService().encode(new StockEntity(
        products = stock.products,
        recommendations = stock.recommendations,
        references = stock.references,
        configs = this.themeConfigs,
        email = user,
        client = client,
        pixel = "%s?client=%s&userId=%s&userRuleId=%s&deliveryAt=%s".format(
          Url.PIXEL_VIEW,
          client,
          this.queue.userId,
          this.queue.userRuleId,
          URLEncoder.encode(this.queue.deliveryAt, Base64Converter.UTF_8)
        ),
        virtual_mta = this.queue.vmta
      ))
    )
  }

  /**
    * @param List   channels
    * @param String user
    * @return String
    */
  private def fillUser(user: String, channels: List[String]): String = {
    if (channels.contains(this.queue.channelName)) {
      return user
    }
    TC.VOID
  }

  /**
    * Persist send data
    */
  def saveSend: Boolean = {
    try {
      val table = this.generateSendTable
      val total: Long = this.data.count
      var limiter: Int = 0
      var totalizator: Int = 0
      var query: String = null
      var data: List[Map[String, Any]] = List()

      this.data.collect().foreach(row => {
        val dataMap = this.createSendData(row._1, row._2)
        if (query == null) {
          query = String.format("INSERT IGNORE INTO %s (%s) VALUES (%s);",
            table, dataMap.keys.mkString(","), List.fill(dataMap.size)("?").mkString(",")
          )
        }
        data = data :+ dataMap
        limiter += 1
        totalizator += 1
        if (limiter >= this.batchLimit || totalizator >= total) {
          this.connection(this.db).insertStatementBatch(query, data)
          data = List()
          limiter = 0
        }
      })
      true
    } catch {
      case e: Exception => println(e.printStackTrace())
        false
    }
  }

}
