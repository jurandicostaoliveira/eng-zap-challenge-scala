package br.com.btg360.run

import br.com.btg360.services.{QueueManagerService}

object QueueCreateRun extends App {

  //new QueueManagerService().create(14).create(14, true)
  new QueueManagerService().create(17).create(17, true)
  println(">>>>>SUCCESS")

}
