//package br.com.btg360.run
//
//import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
//import br.com.btg360.application.Worker
//import br.com.btg360.entities.AccountEntity
//
//object PrintMyActorRefActor {
//  final case class callIt(id: Int)
//}
//
//class PrintMyActorRefActor extends Actor with ActorLogging {
//  import PrintMyActorRefActor._
//  override def receive: Receive = {
//    case callIt(id) =>
//      val secondRef = context.actorOf(Props.empty, "second-actor")
//      log.info(s"Second: $secondRef")
//      loadWork(id)
//  }
//
//  def loadWork(id: Int): Unit = {
//    var accountEntity: AccountEntity = new AccountEntity
//    accountEntity.accountId_=(id)
//    val worker: Worker = new Worker(accountEntity)
//    worker.process(classOf[QueueCreateModel])
//  }
//}
//
//object AkkaExample1 extends App {
//
//  import PrintMyActorRefActor._
//
//  val system = ActorSystem("testSystem")
//
//  val firstRef1 = system.actorOf(Props[PrintMyActorRefActor], "first-actor1")
//  val firstRef2 = system.actorOf(Props[PrintMyActorRefActor], "first-actor2")
//
//  println(s"First1: $firstRef1")
//  firstRef1 ! callIt(10)
//  firstRef2 ! callIt(20)
//
//  println(">>> Press ENTER to exit <<<")
//  try readLine()
//  finally system.shutdown()
//
//}
