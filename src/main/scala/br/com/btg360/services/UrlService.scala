package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.constants.{Channel, HtmlPosition, Url, Base64Converter => base64}
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
    * @param String      position
    * @param String      productId
    * @param Int         isRecommendation
    * @return String
    */
  def redirect(
                queue: QueueEntity,
                link: String,
                position: String = HtmlPosition.CONTENT,
                productId: String = "0",
                isRecommendation: Int = 0,
                client: String = ""
              ): String = {

    val strClient = if (client.isEmpty) """{{ client }}""".trim else base64.encode(client)
    val utmLink = if (queue.utmLink.isEmpty) null else "?%s".format(queue.utmLink)

    "%s/%d/%d/%d/%s/%s/%d/%d/%s/%d/%s/%s%s".format(
      Url.REDIRECTOR,
      queue.rule.btgId,
      queue.userId,
      queue.userRuleId,
      queue.channelName,
      strClient,
      queue.deliveryTimestamp,
      queue.rule.layoutId,
      position,
      isRecommendation,
      productId,
      base64.encode(link.replace("/?", "?")),
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
  def parse(queue: QueueEntity, html: String, position: String): String = {
    try {
      var content = html.trim
      for ((key, value) <- this.replacePatterns) {
        content = key.r.replaceAllIn(content, value)
      }

      "(href=[\"|\'](.*?)[\"|\'])".r.replaceAllIn(content, row => {
        var href = row.group(2)
        if (!this.exceptLinks.contains(href)) {
          href = this.redirect(queue, href, position)
        }
        """href="%s"""".format(href)
      })
    } catch {
      case e: Exception => println(e.printStackTrace())
        html
    }
  }

}
