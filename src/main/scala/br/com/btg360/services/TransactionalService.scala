package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.constants.{Message, Template}
import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.repositories.{TemplateRepository, ThemeRepository, TransactionalRepository}
import br.com.btg360.constants.{TypeConverter => TC}
import org.apache.spark.rdd.RDD

class TransactionalService() extends Service {

  private val themeRepository = new ThemeRepository()

  private val templateRepository = new TemplateRepository()

  private val transactionalRepository = new TransactionalRepository()

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
      return ("%s%s".format(this.pixelImg(), new UrlService().parse(queue, queue.rule.content)), Map())
    }

    val theme = new JsonService().decode[Map[String, Any]](
      this.themeRepository.findById(queue.rule.themeId).configs
    )
    val configs = theme -- Set("header", "footer")
    (
      "%s%s%s%s".format(
        this.pixelImg(),
        new UrlService().parse(queue, TC.toString(theme("header"))),
        this.templateRepository.findById(queue.rule.templateId).html,
        new UrlService().parse(queue, TC.toString(theme("footer"))),
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
      queue.rule.transactionalId = 1111
      this.transactionalRepository
        .queue(queue)
        .data(data)
        .templateId(this.transactionalRepository.saveTemplate(layout._1))
        .themeConfigs(layout._2)
        .createSendTable
        .saveSend

      println(Message.TRANSACTIONAL_SUCCESS)
    } catch {
      case e: Exception => println(e.printStackTrace())
        println(Message.TRANSACTIONAL_ERROR)
    }
  }

}
