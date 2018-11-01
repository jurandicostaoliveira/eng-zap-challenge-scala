package br.com.btg360.run

import br.com.btg360.services.{QueueManagerService}

object QueueCreateRun extends App {

  new QueueManagerService().create(14).create(14, true)
  println(">>>>>SUCCESS")

}
