package br.com.btg360.scheduling.cron

import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import org.quartz.JobKey
import br.com.btg360.scheduling.job.{JobCall, JobCreateQueue}

object CronTrigger {
   var jobCreateQueue: JobDetail = null
   var triggerJobCreateQueue: Trigger = null
   
   def main(args: Array[String]) {
     jobDefinition()
     triggerDefinition()
     run()
   }
   
   def jobDefinition() {
     val jobKey1: JobKey = new JobKey("jobCall", "group1")
     jobCreateQueue = JobBuilder.newJob(classOf[JobCreateQueue]).withIdentity(jobKey1).build()
   }
   
   def triggerDefinition() {
     triggerJobCreateQueue = TriggerBuilder.newTrigger().withIdentity("triggerJobCall", "group1")
                     .withSchedule(CronScheduleBuilder.cronSchedule("0 01 14 * * ?")).build()
   }
   
   def run() {
     val scheduler: Scheduler = new StdSchedulerFactory().getScheduler()
     scheduler.start()
     scheduler.scheduleJob(jobCreateQueue, triggerJobCreateQueue)
   }
}