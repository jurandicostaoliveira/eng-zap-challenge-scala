package br.com.btg360.run

import br.com.btg360.services.QueueManagerService

object QueueRun extends App {

  val queueManagerService = new QueueManagerService()
  queueManagerService.create(14)

}
