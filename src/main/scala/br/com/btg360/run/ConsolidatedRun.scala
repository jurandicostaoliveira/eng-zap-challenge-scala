package br.com.btg360.run

import br.com.btg360.repositories.ConsolidatedRepository

object ConsolidatedRun extends App {

  val consolidatedRepository = new ConsolidatedRepository()

  val rdd = consolidatedRepository.table("btg_consolidated.navigation_daily_8232_2975_2018_09_04_email").findAll
  rdd.foreach(row => {
    println(row.userId + " -> " + row.userSent)
  })

}
