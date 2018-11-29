package br.com.btg360.run

import br.com.btg360.worker.rule.Daily

object DailyRuleRun extends App {

  new Daily().dispatch(14)

}


