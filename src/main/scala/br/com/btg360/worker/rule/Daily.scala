package br.com.btg360.worker.rule

import br.com.btg360.constants.{QueueStatus, Rule}
import br.com.btg360.traits.RuleTrait

class Daily extends RuleTrait {

  override def getTypes: List[Int] = List(Rule.DAILY_ID, Rule.AUTOMATIC_ID)

  override def getCompletedStatus: Int = QueueStatus.FINALIZED

}
