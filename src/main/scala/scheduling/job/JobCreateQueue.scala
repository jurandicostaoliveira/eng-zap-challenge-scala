package scheduling.job

import java.sql.ResultSet

import models.ConfigModel
import org.quartz.JobExecutionContext
import org.quartz.Job

class JobCreateQueue extends Job {

  var configModel : ConfigModel = null

  override def execute(jobExecutionContext: JobExecutionContext) : Unit = {
    configModel = new ConfigModel
    dispatch
  }

  def dispatch : Unit = {
    val configs : ResultSet = configModel.findByUserId(14)

    while(configs.next()) {
      println("btg: " + configs.getInt("btg"))
    }
  }
}