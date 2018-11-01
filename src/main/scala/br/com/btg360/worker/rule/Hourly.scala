package br.com.btg360.worker.rule

import br.com.btg360.constants.{QueueStatus, Rule}
import br.com.btg360.traits.RuleTrait

class Hourly extends RuleTrait {

  /**
    * @return List[Int]
    */
  override def getTypes: List[Int] = List(Rule.HOURLY_ID)

  /**
    * @return Int
    */
  override def getCompletedStatus: Int = QueueStatus.CREATED


}
