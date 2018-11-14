package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.constants.{Channel, Url, Base64Converter => base64}
import br.com.btg360.entities.QueueEntity

class UrlService extends Service {

  /**
    * Generating the link to the redirector
    *
    * @param QueueEntity queue
    * @param String      link
    * @param String      productId
    * @param Int         isRecommendation
    * @param Int         position
    * @return String
    */
  def redirect(
                queue: QueueEntity,
                link: String,
                productId: String = "0",
                isRecommendation: Int = 0,
                position: Int = 0
              ): String = {

    val utmLink = if (queue.utmLink.isEmpty) null else "?%s".format(queue.utmLink)
    "%s/%d/%d/%d/%s/%d/%d/%d/%d/%s/%s%s".format(
      Url.REDIRECTOR,
      queue.userId,
      queue.userRuleId,
      Channel.all(queue.channelName),
      "{{client}}",
      queue.deliveryTimestamp,
      queue.rule.layoutId,
      position,
      isRecommendation,
      productId,
      base64.encode(link),
      utmLink
    )
  }

  /**
    * Link generation for static html
    *
    * @param QueueEntity queue
    * @param String      html
    * @return String
    */
  def parse(queue: QueueEntity, html: String): String = {
    try {
      val patterns: Map[String, String] = Map(
        "[\\\\n]" -> "",
        "href[\\s]+\\=[\\s]+|href[\\s]+\\=|href=[\\s]+" -> "href="
      )

      var textHtml = html.trim
      for ((key, value) <- patterns) {
        textHtml = key.r.replaceAllIn(textHtml, value)
      }

      "(href=[\"|\'](.*?)[\"|\'])".r.replaceAllIn(textHtml, row => {
        """href="%s"""".format(this.redirect(queue, row.group(2)))
      })
    } catch {
      case e: Exception => println(e.printStackTrace())
        html
    }
  }

}
