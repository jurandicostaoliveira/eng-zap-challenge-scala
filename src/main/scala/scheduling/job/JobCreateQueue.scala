package scheduling.job

import java.sql.ResultSet

import repositories.{ConfigRepository, UserRuleRepository}
import org.quartz.{DisallowConcurrentExecution, Job, JobExecutionContext, PersistJobDataAfterExecution}
import traits.JobTrait

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
class JobCreateQueue extends Job with JobTrait {

  var configRepository : ConfigRepository = null

  var userRuleRepository : UserRuleRepository = null

  override def execute(jobExecutionContext: JobExecutionContext) : Unit = {
    configRepository = new ConfigRepository
    userRuleRepository = new UserRuleRepository
    asyncDispatch
  }

  override def asyncDispatch: Unit = {
    val activeUsers : ResultSet = userRuleRepository.findActiveUsers
    while(activeUsers.next()) {
      dispatch(activeUsers.getString("userId"))
    }
  }

  def dispatch(userId : String) : Unit = {
    val configs : ResultSet = configRepository.findByUserId(14)

    while(configs.next()) {
      println("btg: " + configs.getInt("btg"))
    }
  }
}