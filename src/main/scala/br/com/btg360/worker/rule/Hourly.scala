package br.com.btg360.worker.rule

import br.com.btg360.constants.{QueueStatus, Rule}
import br.com.btg360.entities.StockEntity
import br.com.btg360.traits.RuleTrait
import org.apache.spark.rdd.RDD

class Hourly extends RuleTrait {

  /**
    * @return List[Int]
    */
  override def getTypes: List[Int] = List(Rule.HOURLY_ID)

  /**
    * @return Int
    */
  override def getCompletedStatus: Int = QueueStatus.CREATED

  /**
    * @return RDD
    */
  override def getData: RDD[(String, StockEntity)] = {
    null
  }
}
