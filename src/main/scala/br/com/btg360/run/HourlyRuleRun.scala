package br.com.btg360.run

import br.com.btg360.worker.rule.Hourly

object HourlyRuleRun extends App {

  new Hourly().dispatch(14)

}
