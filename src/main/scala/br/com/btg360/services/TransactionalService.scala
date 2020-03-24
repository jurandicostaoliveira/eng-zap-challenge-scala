package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.constants.{HtmlPosition, Message, Template, TypeConverter => TC}
import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.repositories.{TemplateRepository, ThemeRepository, TransactionalPartitionRepository, TransactionalRepository}
import org.apache.spark.rdd.RDD

class TransactionalService() extends Service {

  private val themeRepository = new ThemeRepository()

  private val templateRepository = new TemplateRepository()

  private val transactionalRepository = new TransactionalRepository()

  private val transactionalPartitionRepository = new TransactionalPartitionRepository()

  /**
    * @return String
    */
  private def pixelImg(): String = {
    """<img src="{{ pixel }}" style="opacity:0;">"""
  }

  /**
    * Template settings
    *
    * @param queue
    * @return
    */
  private def createLayout(queue: QueueEntity): (String, Map[String, Any]) = {
    if (queue.rule.layoutId == Template.STATIC_ID) {
      return ("%s%s".format(this.pixelImg(), new UrlService().parse(
        queue, queue.rule.content, HtmlPosition.CONTENT)), Map()
      )
    }

    val theme = new JsonService().decode[Map[String, Any]](
      this.themeRepository.findById(queue.rule.themeId).configs
    )
    val configs = theme -- Set("header", "footer")
    (
      "%s%s%s%s".format(
        this.pixelImg(),
        new UrlService().parse(queue, TC.toString(theme("header")), HtmlPosition.HEADER),
        this.templateRepository.findById(queue.rule.templateId).html,
        new UrlService().parse(queue, TC.toString(theme("footer")), HtmlPosition.FOOTER)
      ),
      configs
    )
  }

  /**
    * @param QueueEntity queue
    * @param RDD         data
    */
  def persist(queue: QueueEntity, data: RDD[(String, StockEntity)]): Unit = {
    try {
      val layout = this.createLayout(queue)
      val templateId: Int = this.transactionalRepository
        .queue(queue)
        .data(data)
        .themeConfigs(layout._2)
        .createTemplateTable
        .saveTemplate(layout._1)

      if (templateId <= 0) {
        println(Message.TEMPLATE_ERROR)
        return
      }

      this.transactionalRepository
        .templateId(templateId)
        .createSendTable
        .createClickTable
        .alterSendTable

      val registered = this.transactionalPartitionRepository
        .queue(queue)
        .data(data)
        .table(this.transactionalRepository.generateSendTable)
        .templateId(templateId)
        .themeConfigs(this.transactionalRepository.themeConfigs)
        .numPartitions(10)
        .save

      if (!registered) {
        println(Message.TRANSACTIONAL_NOT_REGISTER)
        return
      }

      this.transactionalRepository.updateLastSend
      this.transactionalRepository.closeConnection
      println(Message.TRANSACTIONAL_SUCCESS)
    } catch {
      case e: Exception => println(e.printStackTrace())
        println(Message.TRANSACTIONAL_ERROR)
    }
  }

}
