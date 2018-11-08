package br.com.btg360.worker.rule

import br.com.btg360.constants.{QueueStatus, Rule}
import br.com.btg360.entities.{ConsolidatedProductEntity, ItemEntity}
import br.com.btg360.traits.RuleTrait
import org.apache.spark.rdd.RDD
import br.com.btg360.services.DataStockService
import br.com.btg360.spark.SparkCoreSingleton

import scala.collection.mutable.Map

class Daily extends RuleTrait with Serializable {

  override def getTypes: List[Int] = List(Rule.DAILY_ID, Rule.AUTOMATIC_ID)

  override def getCompletedStatus: Int = QueueStatus.FINALIZED

  /**
    * @return RDD
    */
  override def getData: RDD[(String, ItemEntity)] = {
    if (this.queue.ruleTypeId == Rule.AUTOMATIC_ID) {
      return this.automatic
    }

    //val dataStockService = new DataStockService(this.queue)
    //dataStockService.get
    this.test
  }

  private def automatic: RDD[(String, ItemEntity)] = {
    null
  }

  /**
    * TO ONLY TEST
    *
    * @return
    */
  private def test: RDD[(String, ItemEntity)] = {
    val p1 = new ConsolidatedProductEntity()
    p1.userSent = "paula@hotmail.com"
    p1.productId = "000001"

    val p2 = new ConsolidatedProductEntity()
    p2.userSent = "carla@hotmail.com"
    p2.productId = "000002"

    val p3 = new ConsolidatedProductEntity()
    p3.userSent = "sandra@hotmail.com"
    p3.productId = "000003"

    val p4 = new ConsolidatedProductEntity()
    p4.userSent = "simone@hotmail.com"
    p4.productId = "000004"

    val p5 = new ConsolidatedProductEntity()
    p5.userSent = "adriana@hotmail.com"
    p5.productId = "000005"

    val p6 = new ConsolidatedProductEntity()
    p6.userSent = "marciaribeiroportugal@hotmail.com"
    p6.productId = "000006"

    val list: List[ConsolidatedProductEntity] = List(p1, p2, p3, p4, p6, p5)
    //val list: List[ConsolidatedProductEntity] = List()

    val data: RDD[(String, ItemEntity)] = SparkCoreSingleton.getContext.parallelize(list).map(row => {
      val item = new ItemEntity()
      item.addProducts(row)
      (row.userSent, item)
    })

    data
  }

}
