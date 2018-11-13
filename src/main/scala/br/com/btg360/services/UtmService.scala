package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.constants.{Channel, Url}
import br.com.btg360.entities.QueueEntity
import br.com.btg360.services.{TypeConverterService => TCS}

import scala.collection.mutable.HashMap

class UtmService extends Service {

  /**
    * @param QueueEntity queue
    * @param String      link
    * @param String      productId
    * @param Int         isRecommendation
    * @param Int         position
    * @return String
    */
  def generateLink(queue: QueueEntity,
                   link: String,
                   productId: String,
                   isRecommendation: Int = 0,
                   position: Int = 0
                  ): String = {

    val strUtms: String = ""

    "%s/%d/%d/%d/%s/%d/%d/%d/%d/%s/%s?%s".format(
      Url.REDIRECTOR,
      queue.userId,
      queue.userRuleId,
      Channel.all(queue.channelName),
      "{{client}}",
      queue.getDeliveryTimestamp,
      queue.rule.layoutId,
      position,
      isRecommendation,
      productId,
      Base64Service.encode(link),
      strUtms
    )
  }

  def addLink(queue: QueueEntity, data: List[HashMap[String, Any]]): List[HashMap[String, Any]] = {

    data.map(row => {
      var position = 0
      println(position)
//      row("productLink") = TCS.toString(new UtmService().generateLink(
//        queue,
//        TCS.toString(row("productLink")),
//        TCS.toString(row("productId")),
//        TCS.toInt(row("isRecommendation")),
//        position
//      ))
position += 1
      row
    })
  }


}
