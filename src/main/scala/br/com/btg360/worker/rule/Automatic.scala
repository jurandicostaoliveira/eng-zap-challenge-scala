package br.com.btg360.worker.rule

import br.com.btg360.constants.{QueueStatus, Rule}
import br.com.btg360.entities.StockEntity
import br.com.btg360.traits.RuleTrait
import org.apache.spark.rdd.RDD

class Automatic extends RuleTrait {

  /**
    * @return List[Int]
    */
  override def getTypes: List[Int] = List(Rule.AUTOMATIC_ID)

  /**
    * @return Int
    */
  override def getCompletedStatus: Int = QueueStatus.FINALIZED

  /**
    * @return RDD
    */
  override def getData: RDD[(String, StockEntity)] = {

    println(this.queue.rule.automatics.field)

    null
  }

  /**
    * Other methods
    */

}
