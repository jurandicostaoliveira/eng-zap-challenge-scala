package br.com.btg360.run

import br.com.btg360.services.OptoutService

object OptoutExample extends App {

  val optoutService : OptoutService = new OptoutService()

  val users = List("mariana@test.com", "paula@test.com", "angela@test.com", "carla@test.com", "solange@test.com")

  optoutService.filter(users)


}
