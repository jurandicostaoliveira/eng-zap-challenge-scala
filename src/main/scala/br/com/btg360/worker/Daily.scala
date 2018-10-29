package br.com.btg360.worker

import br.com.btg360.traits.RuleTrait
import br.com.btg360.constants.Rule

class Daily extends RuleTrait {

  override def shouldUseRuleTypes: List[Int] = {
    List(Rule.DAILY_ID, Rule.AUTOMATIC_ID)
  }

}
