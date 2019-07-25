package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.constants.{Channel, Url, Base64Converter => base64}
import br.com.btg360.entities.QueueEntity

class UrlService extends Service {

  private val exceptLinks = List("##optout##", "##preview##", "#")

  private val replacePatterns: Map[String, String] = Map(
    "[\\n]" -> "",
    "href[\\s]+\\=[\\s]+|href[\\s]+\\=|href=[\\s]+" -> "href="
  )

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
      """ {{ client }} """,
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
      var content = html.trim
      for ((key, value) <- this.replacePatterns) {
        content = key.r.replaceAllIn(content, value)
      }

      "(href=[\"|\'](.*?)[\"|\'])".r.replaceAllIn(content, row => {
        var href = row.group(2)
        if (!this.exceptLinks.contains(href)) {
          href = this.redirect(queue, href)
        }
        """href="%s"""".format(href)
      })
    } catch {
      case e: Exception => println(e.printStackTrace())
        html
    }
  }

}
