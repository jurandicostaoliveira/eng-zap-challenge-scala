package br.com.btg360.worker.rule

import br.com.btg360.constants.{QueueStatus, Rule}
import br.com.btg360.entities.StockEntity
import br.com.btg360.services.DataMapperService
import br.com.btg360.spark.SparkCoreSingleton
import br.com.btg360.traits.RuleTrait
import org.apache.spark.rdd.RDD

class Daily extends RuleTrait {

  /**
    * @return List[Int]
    */
  override def getTypes: List[Int] = List(Rule.DAILY_ID, Rule.AUTOMATIC_ID)

  /**
    * @return Int
    */
  override def getCompletedStatus: Int = QueueStatus.FINALIZED

  /**
    * @return RDD
    */
  override def getData: RDD[(String, StockEntity)] = {
    if (this.queue.ruleTypeId == Rule.AUTOMATIC_ID) {

      /**
        * Test customization to run only for mx parts and mx bikes
        */
      if (List(16, 17).contains(this.queue.userId)) {
        return new Automatic(this.queue).getData
      } else {
        return SparkCoreSingleton.getContext.emptyRDD[(String, StockEntity)]
      }

    }

    //No automatic
    new DataMapperService(this.queue).get
  }

}
