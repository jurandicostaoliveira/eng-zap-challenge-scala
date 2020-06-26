package br.com.btg360.services

import br.com.allin.ChannelManagerRepository.ChannelManagerRepository
import br.com.btg360.constants.{Token, Url}
import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.spark.SparkCoreSingleton
import com.m3.curly.Response
import org.apache.spark.rdd.RDD
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JObject
import org.json4s.jackson.JsonMethods.parse

class SolrReferenceListService extends Serializable {

  private val sc = SparkCoreSingleton.getContext

  private val httpService: HttpService = new HttpService()

  private val repository = new ChannelManagerRepository(sc.getConf, null)

  private var allinId: Int = 0

  private var listId: Int = 0

  /**
    * @return Response
    */
  private def find: Response = {
    this.httpService
      .url("%s/list/%s?customerId=%s".format(Url.API_CHANNEL_MANAGER, this.listId, this.allinId))
      .headers(Map("Authorization" -> Token.API_CHANNEL_MANAGER))
      .get
  }

  /**
    * @param String value
    * @return Map
    */
  private def jsonParse(value: String): Map[String, Boolean] = {
    implicit val formats = DefaultFormats
    val json = parse(value).asInstanceOf[JObject]
    Map(
      "active" -> (json \ "result" \ "active").extract[Boolean],
      "archived" -> (json \ "result" \ "archived").extract[Boolean],
      "isDataNormalized" -> (json \ "result" \ "isDataNormalized").extract[Boolean]
    )
  }

  /**
    * Get list data
    *
    * @param Boolean isDataNormalized
    * @return RDD
    */
  private def getData(isDataNormalized: Boolean): RDD[(String, Map[String, Any])] = {
    this.repository.get(this.listId.toString, this.allinId, isDataNormalized, null).rdd.map(json => {
      implicit val formats = DefaultFormats
      val row = parse(json).asInstanceOf[JObject]
      ((row \ "email").extract[String].trim, row.extract[Map[String, Any]])
    })
  }

  /**
    * Places the data referring to the user coming from the reference list
    *
    * @param QueueEntity queue
    * @param RDD         data
    * @return RDD
    */
  def add(queue: QueueEntity, data: RDD[(String, StockEntity)]): RDD[(String, StockEntity)] = {
    try {
      this.allinId = queue.rule.allinId
      this.listId = queue.rule.referenceListId

      val response = this.find
      if (response.getStatus != 200) {
        return data
      }

      val json = this.jsonParse(response.getTextBody)
      if (!json("active") && json("archived")) {
        return data
      }

      data.leftOuterJoin(this.getData(json("isDataNormalized"))).map(row => {
        var item = row._2._1
        if (row._2._2.isDefined) {
          item = new StockEntity(
            products = item.products,
            recommendations = item.recommendations,
            references = row._2._2.get
          )
        }
        (row._1, item)
      })
    } catch {
      case e: Exception => println(e.printStackTrace)
        data
    }
  }

}
