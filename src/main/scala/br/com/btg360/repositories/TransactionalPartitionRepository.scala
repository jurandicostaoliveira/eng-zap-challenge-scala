package br.com.btg360.repositories

import java.net.{URLDecoder, URLEncoder}

import br.com.btg360.constants.{Base64Converter, Channel, Url, TypeConverter => TC}
import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.jdbc.MySqlAllin
import br.com.btg360.services.JsonService
import org.apache.spark.rdd.RDD

import scala.collection.mutable.HashMap

class TransactionalPartitionRepository extends Serializable {

  private var _queue: QueueEntity = new QueueEntity()

  private var _data: RDD[(String, StockEntity)] = _

  private var _table: String = ""

  private var _templateId: Int = 0

  private var _themeConfigs: Map[String, Any] = Map()

  private var _numPartitions: Int = 5

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
  def queue(value: QueueEntity): TransactionalPartitionRepository = {
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
  def data(value: RDD[(String, StockEntity)]): TransactionalPartitionRepository = {
    this._data = value
    this
  }

  /**
    * Getter
    *
    * @return String
    */
  def table: String = this._table

  /**
    * Setter
    *
    * @param String value
    * @return this
    */
  def table(value: String): TransactionalPartitionRepository = {
    this._table = value
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
  def templateId(value: Int): TransactionalPartitionRepository = {
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
  def themeConfigs(value: Map[String, Any]): TransactionalPartitionRepository = {
    this._themeConfigs = value
    this
  }

  /**
    * Getter
    *
    * @return Int
    */
  def numPartitions: Int = this._numPartitions

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def numPartitions(value: Int): TransactionalPartitionRepository = {
    this._numPartitions = value
    this
  }

  /**
    * Return fields to send table
    *
    * @return HashMap
    */
  def getFields(): HashMap[String, Any] = {
    HashMap(
      "nm_envio" -> null,
      "nm_subject" -> null,
      "nm_remetente" -> null,
      "email_remetente" -> null,
      "nm_reply" -> null,
      "dt_envio" -> null,
      "hr_envio" -> null,
      "nm_titulo" -> null,
      "nm_mensagem" -> null,
      "url_scheme" -> null,
      "id_tipo_envio" -> null,
      "id_template" -> null,
      "btg_user_rule_id" -> null,
      "nm_email" -> null,
      "nm_celular" -> null,
      "nm_push" -> null,
      "nm_facebook" -> null,
      "valor_json" -> null
    )
  }

  private def fillColumns(user: String, stock: StockEntity): HashMap[String, Any] = {
    val channelMap = this.queue.rule.channelMap
    val client = Base64Converter.encode(user)
    val fields = this.getFields()

    fields("nm_envio") = "BTG:%s:%s".format(this.queue.ruleName, this.queue.rule.latinName)
    fields("nm_subject") = URLDecoder.decode(this.queue.rule.subject, Base64Converter.UTF_8)
    fields("nm_remetente") = this.queue.rule.senderName
    fields("email_remetente") = this.queue.rule.senderEmail
    fields("nm_reply") = this.queue.rule.replyEmail
    fields("dt_envio") = this.queue.today
    fields("hr_envio") = this.queue.deliveryHourAt
    fields("nm_titulo") = TC.toString(channelMap(this.queue.channelName).subject)
    fields("nm_mensagem") = TC.toString(channelMap(this.queue.channelName).message)
    fields("url_scheme") = TC.toString(channelMap(this.queue.channelName).urlScheme)
    fields("id_tipo_envio") = Channel.all(this.queue.channelName)
    fields("id_template") = this.templateId
    fields("btg_user_rule_id") = this.queue.userRuleId
    fields("nm_email") = this.fillUser(user, List(Channel.EMAIL))
    fields("nm_celular") = this.fillUser(user, List(Channel.SMS))
    fields("nm_push") = this.fillUser(user, List(Channel.PUSH_ANDROID, Channel.PUSH_IOS))
    fields("nm_facebook") = this.fillUser(user, List(Channel.FACEBOOK))
    fields("valor_json") = new JsonService().encode(new StockEntity(
      products = stock.products,
      recommendations = stock.recommendations,
      references = stock.references,
      configs = this.themeConfigs,
      email = user,
      client = client,
      pixel = "%s?btgId=%s&userId=%s&userRuleId=%s&channel=%s&client=%s&deliveryAt=%s".format(
        Url.PIXEL_VIEW,
        this.queue.rule.btgId,
        this.queue.userId,
        this.queue.userRuleId,
        this.queue.channelName,
        client,
        URLEncoder.encode(this.queue.deliveryAt, Base64Converter.UTF_8)
      ),
      virtual_mta = this.queue.vmta
    ))

    fields
  }

  /**
    *
    * @param user
    * @param channels
    * @return
    */
  private def fillUser(user: String, channels: List[String]): String = {
    if (channels.contains(this.queue.channelName)) {
      return user
    }
    TC.VOID
  }

  /**
    * Save in transactional
    *
    * @return Boolean
    */
  def save: Boolean = {
    try {
      val fields = getFields()
      val query: String = String.format("INSERT IGNORE INTO %s (%s) VALUES (%s);",
        this.table, fields.keys.mkString(","), List.fill(fields.size)("?").mkString(",")
      )

      this.data.repartition(this.numPartitions).foreachPartition(rddPartition => {
        val conn = new MySqlAllin().open
        conn.setAutoCommit(false)
        val pstmt = conn.prepareStatement(query)

        rddPartition.foreach(row => {
          val columns = this.fillColumns(row._1, row._2)

          var index = 1
          for ((key, value) <- columns) {
            if (value == null) pstmt.setNull(index, 0) else pstmt.setObject(index, value)
            index += 1
          }

          pstmt.addBatch()
        })

        pstmt.executeLargeBatch()
        conn.commit()
        pstmt.close()
        conn.close()
      })

      true
    } catch {
      case e: Exception => println(e.printStackTrace())
        false
    }
  }

}
