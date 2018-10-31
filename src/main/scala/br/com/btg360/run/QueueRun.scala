package br.com.btg360.run

import br.com.btg360.services.{PeriodService, QueueManagerService}

object QueueRun extends App {

  val queueManagerService = new QueueManagerService()
  queueManagerService.create(14).create(14, true)
  println(">>>>>SUCCESS")

}
