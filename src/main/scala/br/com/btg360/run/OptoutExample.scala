package br.com.btg360.run

import br.com.btg360.services.OptoutService
//import br.com.btg360.jdbc.{MySqlAllin, MySqlBtg360}

object OptoutExample extends App {

  val optoutService: OptoutService = new OptoutService()
  val users = List("mariana@test.com", "paula@test.com", "angela@test.com", "carla@test.com", "solange@test.com")
  optoutService.filter(users)

  //  val allinConn = new MySqlAllin().open
  //  val btgConn = new MySqlBtg360().open
  //
  //  println("-------------ALLIN---------------")
  //
  //  val ps = allinConn.prepareStatement("select * from postmatic.cor_envio_trans_5884_03 limit 10;")
  //  val rs = ps.executeQuery()
  //
  //  while(rs.next()) {
  //    println(rs.getString("nm_email"))
  //  }
  //
  //  println("-------------BTG---------------")
  //
  //  val psb = btgConn.prepareStatement("select * from btg_jobs.rules_queue limit 10;")
  //  val rsb = psb.executeQuery()
  //
  //  while(rsb.next()) {
  //    println(rsb.getString("today"))
  //  }


}
