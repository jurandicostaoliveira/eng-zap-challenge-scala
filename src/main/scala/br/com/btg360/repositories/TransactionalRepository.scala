package br.com.btg360.repositories

import br.com.btg360.application.Repository
import br.com.btg360.constants.Database
import br.com.btg360.entities.QueueEntity
import br.com.btg360.jdbc.MySqlBtg360
import scala.util.Random
import br.com.btg360.services.PeriodService

import scala.collection.mutable.HashMap

class TransactionalRepository extends Repository {

  private val db = new MySqlBtg360().open

  private var _batchLimit: Int = 10000

  private var _typeSend: Int = 1

  private var _allinId: Int = 0

  private var _transactionalId: Int = 0

  private var _data: List[HashMap[String, Any]] = List(HashMap("id" -> 1, "name" -> "lala"), HashMap("id" -> 2, "name" -> "lele"))

  private var queue: QueueEntity = _

  //
  private var userRuleId: Long = 0
  private var name: String = _
  private var template: String = _
  private var configs: HashMap[String, Any] = HashMap()
  private var subject: String = _
  private var date: String = _
  private var senderEmail: String = _
  private var replyEmail: String = _
  private var senderName: String = _
  private var title: String = _
  private var message: String = _
  private var urlScheme: String = _

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
    * @return
    */
  def typeSend: Int = this._typeSend

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def typeSend(value: Int): TransactionalRepository = {
    this._typeSend = value
    this
  }

  /**
    * Getter
    *
    * @return
    */
  def allinId: Int = this._allinId

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def allinId(value: Int): TransactionalRepository = {
    this._allinId = value
    this
  }

  /**
    * Getter
    *
    * @return
    */
  def transactionalId: Int = this._transactionalId

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def transactionalId(value: Int): TransactionalRepository = {
    this._transactionalId = value
    this
  }

  /**
    * Getter
    *
    * @return
    */
  def data: List[HashMap[String, Any]] = this._data

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def data(value: List[HashMap[String, Any]]): TransactionalRepository = {
    this._data = value
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
    "%s.cor_envio_trans_%d_%s".format(Database.POSTMATIC, this.transactionalId, this.currentMonth)
  }

  /**
    * @return String
    */
  private def generateClickTable: String = {
    "%s.clique_%s_%s".format(Database.POSTMATIC, this.transactionalId, this.currentMonth)
  }

  /**
    * @return String
    */
  private def generateTemplateTable: String = {
    "%s.cor_template_%s".format(Database.POSTMATIC, this.transactionalId)
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

    this.connection(this.db).ddlExecutor(query)
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
              KEY `id_envio` (`id_envio`),
              KEY `dt_clique` (`dt_clique`)
            ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;"""

    this.connection(this.db).ddlExecutor(query)
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
                `valor_json` text,
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
                KEY `id_envio_anterior` (`id_envio_anterior`)
            ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;"""

    this.connection(this.db).ddlExecutor(query)
    this
  }

  /**
    *
    */
  def alterSendTable: Unit = {
    val table: String = this.generateSendTable
    val column = "btg_user_rule_id"
    if (!this.columnExists(table, column)) {
      this.connection(this.db)
        .ddlExecutor(s"ALTER TABLE ${table} ADD COLUMN ${column} int(11) DEFAULT NULL;")
    }
  }

  /**
    *
    */
  def updateLastSend: Unit = {
    this.connection(this.db)
      .whereAnd("id_allinmail", "=", this.allinId)
      .update(this.generateLoginTable, HashMap("dt_ult_envio" -> this.now()))
  }

  /**
    * Persist template
    *
    * @return Int
    */
  def saveTemplate: Int = {
    val rand: Long = new Random().nextInt((50000 - 1000) * 38)
    this.connection(this.db).insertGetId(this.generateTemplateTable, HashMap(
      "nm_template" -> "BTG_template_%s_%d".format(this.now("yyyy_MM_dd_HH_mm"), rand),
      "nm_html" -> "", //template
      "fl_descartar" -> 0,
      "dt_cadastro" -> this.now(),
      "fl_twig" -> 1
    ))
  }

  /**
    * Persist send data
    */
  def saveSend: Unit = {
    val table = this.generateSendTable
    var limiter: Int = 0
    var totalizator: Int = 0
    val total: Int = data.size
    var queries: List[String] = List()

    data.foreach(row => {
      val strValues = "'%s'".format(row.values.mkString("','"))
      val query = s"INSERT IGNORE INTO ${table} (${row.keys.mkString(",")}) VALUES (${strValues});"
      queries = queries :+ query
      limiter += 1
      totalizator += 1

      if (limiter >= this.batchLimit || totalizator >= total) {
        this.connection(this.db).insertQueryBatch(queries)
        queries = List()
        limiter = 0
      }
    })
  }

}
