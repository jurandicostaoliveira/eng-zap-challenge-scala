package br.com.btg360.worker.rule

import br.com.btg360.constants.{QueueStatus, Rule, Automatic}
import br.com.btg360.entities.{ConsolidatedEntity, StockEntity, ProductEntity, QueueEntity}
import br.com.btg360.traits.RuleTrait
import org.apache.spark.rdd.RDD
import br.com.btg360.services.{DataMapperService, UrlService}
import br.com.btg360.spark.SparkCoreSingleton

import scala.collection.mutable.HashMap

class Daily extends RuleTrait {

  //override def getTypes: List[Int] = List(Rule.DAILY_ID, Rule.AUTOMATIC_ID)
  override def getTypes: List[Int] = List(Rule.DAILY_ID)

  override def getCompletedStatus: Int = QueueStatus.FINALIZED

  /**
    * @return RDD
    */
  override def getData: RDD[(String, StockEntity)] = {
    if (this.queue.ruleTypeId == Rule.AUTOMATIC_ID) {
      return this.automatic
    }

    val dataMapperService = new DataMapperService(this.queue)
    dataMapperService.get
  }

  private def automatic: RDD[(String, StockEntity)] = {

    this.queue.ruleName match {
      case Automatic.BIRTHDAY =>
        println("REGRA AUTOMATICA: " + this.queue.ruleName)
      case Automatic.SENDING_DATE =>
        println("REGRA AUTOMATICA: " + this.queue.ruleName)
      case Automatic.SENDING_DATE =>
        println("REGRA AUTOMATICA: " + this.queue.ruleName)
    }


    null
  }

  /**
    * TO ONLY TEST
    *
    * @return
    */
  /*private def test: RDD[(String, StockEntity)] = {

    val c1 = new ConsolidatedEntity()
    c1.userSent = "paula@hotmail.com"
    val p1 = new ProductEntity()
    p1.productId = "000001"
    p1.productLink = "http://google.com"

    val c2 = new ConsolidatedEntity()
    c2.userSent = "carla@hotmail.com"
    val p2 = new ProductEntity()
    p2.productId = "000002"
    p2.productLink = "http://google.com"

    val c3 = new ConsolidatedEntity()
    c3.userSent = "sandra@hotmail.com"
    c3.isRecommendation = 1
    val p3 = new ProductEntity()
    p3.productId = "000003"
    p3.productLink = "http://google.com"

    val c4 = new ConsolidatedEntity()
    c4.userSent = "simone@hotmail.com"
    val p4 = new ProductEntity()
    p4.productId = "000004"
    p4.productLink = "http://google.com"

    val c5 = new ConsolidatedEntity()
    c5.userSent = "adriana@hotmail.com"
    val p5 = new ProductEntity()
    p5.productId = "000005"
    p5.productLink = "http://google.com"

    val c6 = new ConsolidatedEntity()
    c6.userSent = "marciaribeiroportugal@hotmail.com"
    val p6 = new ProductEntity()
    p6.productId = "000006"
    p6.productLink = "http://google.com"

    val list: List[HashMap[String, Any]] = List(
      new StockEntity().toMap(this.queue, c1, p1, c1.isRecommendation),
      new StockEntity().toMap(this.queue, c2, p2, c2.isRecommendation),
      new StockEntity().toMap(this.queue, c3, p3, c3.isRecommendation),
      new StockEntity().toMap(this.queue, c4, p4, c4.isRecommendation),
      new StockEntity().toMap(this.queue, c5, p5, c5.isRecommendation),
      new StockEntity().toMap(this.queue, c6, p6, c6.isRecommendation)
    )

    val data: RDD[(String, StockEntity)] = SparkCoreSingleton.getContext.parallelize(list).map(row => {
      var products: List[HashMap[String, Any]] = List()
      var recommendations: List[HashMap[String, Any]] = List()

      if (row("isRecommendation").equals(1)) {
        recommendations ::= row
      } else {
        products ::= row
      }

      (row("userSent").toString, new StockEntity(
        products = products,
        recommendations = recommendations
      ))
    })

    data
  }*/

}
