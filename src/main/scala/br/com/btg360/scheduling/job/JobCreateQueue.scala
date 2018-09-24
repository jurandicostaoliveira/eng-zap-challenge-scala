package br.com.btg360.scheduling.job

import java.sql.ResultSet

import akka.actor.{ActorSystem, Props}
import br.com.btg360.actors.BtgAkkaActors
import br.com.btg360.model.QueueCreateModel
import br.com.btg360.repositories.{ConfigRepository, UserRuleRepository}
import br.com.btg360.traits.JobTrait
import org.quartz.{DisallowConcurrentExecution, Job, JobExecutionContext, PersistJobDataAfterExecution}

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
class JobCreateQueue extends Job with JobTrait {

  var configRepository: ConfigRepository = null

  var userRuleRepository: UserRuleRepository = null

  override def execute(jobExecutionContext: JobExecutionContext): Unit = {
    configRepository = new ConfigRepository
    userRuleRepository = new UserRuleRepository
    asyncDispatch
  }

  override def asyncDispatch: Unit = {
    val activeUsers: ResultSet = userRuleRepository.findActiveUsers
    while (activeUsers.next()) {
      dispatch(activeUsers.getInt("userId"))
    }
  }

  def dispatch(userId: Int): Unit = {
    import br.com.btg360.actors.BtgAkkaActors._

    val configs: ResultSet = configRepository.findByUserId(userId)
    val system = ActorSystem("btgActorSystem")

    while (configs.next()) {

      if(configs != null && configs.getInt("btg")  == 1){

        var firstRef = system.actorOf(Props[BtgAkkaActors], "first-create-queue-actor" + userId)
        println(s"First: $firstRef")

        firstRef ! callIt(userId, classOf[QueueCreateModel])
      }
    }

    //system.shutdown()
  }
}