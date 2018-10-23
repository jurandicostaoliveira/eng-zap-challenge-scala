package br.com.btg360.repositories

import br.com.btg360.application.Repository
import br.com.btg360.services.PeriodService

class RuleQueueRepository extends Repository {

  def countByUserId(userId: Int): Int = {
    try {
      val periodService: PeriodService = new PeriodService
      val today = periodService.format_=("yyyy-MM-dd").now
      val query: String = s"SELECT userId FROM btg_jobs.rules_queue WHERE today='$today' AND userId = $userId"
      countRows(queryExecutor(query))
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
        0
    }
  }
}
