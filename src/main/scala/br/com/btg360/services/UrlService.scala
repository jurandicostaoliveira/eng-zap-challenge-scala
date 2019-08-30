package br.com.btg360.services

import java.net.{URI, URLEncoder}

import br.com.btg360.application.Service
import br.com.btg360.constants.{HtmlPosition, Url, Base64Converter => base64}
import br.com.btg360.entities.QueueEntity

class UrlService extends Service {

  private val exceptLinks = List("##optout##", "##preview##", "#")

  private val replacePatterns: Map[String, String] = Map(
    "[\\n]" -> "",
    "href[\\s]+\\=[\\s]+|href[\\s]+\\=|href=[\\s]+" -> "href="
  )

  /**
    * Generate URI
    *
    * @param String uri
    * @param String queryString
    * @return String
    */
  private def generateUri(uri: String, qs: String): String = {
    if (!qs.isEmpty) {
      val uriSymbol: String = if (uri.contains("?")) "" else "?"
      var qsSymbol: String = if (new URI(uri).getQuery == null) "" else "&"
      if (uri.takeRight(1).equals("?")) {
        qsSymbol = ""
      }
      return "%s%s%s%s".format(uri, uriSymbol, qsSymbol, qs)
    }

    uri
  }

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
    val uri = this.generateUri(link, queue.utmLink)
    val params: List[String] = List(
      "btgId=%s".format(queue.rule.btgId),
      "userId=%s".format(queue.userId),
      "userRuleId=%s".format(queue.userRuleId),
      "channel=%s".format(queue.channelName),
      "client=%s".format(strClient),
      "deliveryAt=%s".format(URLEncoder.encode(queue.deliveryAt, base64.UTF_8)),
      "templateType=%s".format(queue.rule.layoutId),
      "position=%s".format(position),
      "isRecommendation=%s".format(isRecommendation),
      "productId=%s".format(productId),
      "link=%s".format(URLEncoder.encode(uri, base64.UTF_8))
    )

    "%s?%s".format(Url.REDIRECTOR, params.mkString("&"))
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
