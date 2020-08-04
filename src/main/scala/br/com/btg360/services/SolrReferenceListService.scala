package br.com.btg360.services

import br.com.allin.ChannelManagerRepository.ChannelManagerRepository
import br.com.btg360.constants.{Token, Url}
import br.com.btg360.entities.StockEntity
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

  private var _allinId: Int = 0

  private var _listId: Int = 0

  /**
    * Getter
    *
    * @return Int
    */
  def allinId: Int = this._allinId

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def allinId(value: Int): SolrReferenceListService = {
    this._allinId = value
    this
  }

  /**
    * Getter
    *
    * @return Int
    */
  def listId: Int = this._listId

  /**
    * Setter
    *
    * @param Int value
    * @return this
    */
  def listId(value: Int): SolrReferenceListService = {
    this._listId = value
    this
  }

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
    * @return RDD
    */
  private def getData(): RDD[(String, Map[String, Any])] = {
    val rdd = this.repository.getHDFS(this.listId.toString, this.allinId).rdd

    if (rdd.isEmpty()) {
      println("LIST NOT FOUND >> ALLIN-ID: " + this.allinId + ", LIST-ID: " + this.listId)
      return SparkCoreSingleton.getContext.emptyRDD[(String, Map[String, Any])]
    }

    rdd.map(json => {
      implicit val formats = DefaultFormats
      val row = parse(json).asInstanceOf[JObject]
      ((row \ "email").extract[String].trim, row.extract[Map[String, Any]])
    })
  }

  /**
    * @param RDD data
    * @return RDD
    */
  private def toStandard(data: RDD[(String, StockEntity)]): RDD[(String, StockEntity)] = {
    data.leftOuterJoin(this.getData()).map(row => {
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
  }

  /**
    * @param RDD data
    * @return RDD
    */
  private def toApp(data: RDD[(String, StockEntity)]): RDD[(String, StockEntity)] = {
    data.leftOuterJoin(this.getData()).map(row => {
      var item = row._2._1
      if (row._2._2.isDefined) {
        item = new StockEntity(
          products = item.products,
          recommendations = item.recommendations,
          references = item.references,
          referencesToApp = row._2._2.get
        )
      }
      (row._1, item)
    })
  }

  /**
    * Places the data referring to the user coming from the reference list
    *
    * @param RDD     data
    * @param Boolean toApp
    * @return RDD
    */
  def add(data: RDD[(String, StockEntity)], toApp: Boolean = false): RDD[(String, StockEntity)] = {
    try {
      val response = this.find
      if (response.getStatus != 200) {
        return data
      }

      val json = this.jsonParse(response.getTextBody)
      if (!json("active") || json("archived")) {
        return data
      }

      if (toApp) {
        return this.toApp(data)
      }

      this.toStandard(data)
    } catch {
      case e: Exception => println(e.printStackTrace)
        data
    }
  }

}
