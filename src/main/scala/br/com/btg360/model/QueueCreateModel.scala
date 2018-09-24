package br.com.btg360.model

import br.com.btg360.application.{Context, Model}
import br.com.btg360.spark.SparkContextSingleton
import br.com.btg360.traits.ModelFactoryTrait

class QueueCreateModel(context: Context) extends Model with Serializable with ModelFactoryTrait{
  override def build: Unit = {
    val accountId = context.accountEntity.accountId

    println("CHEGUEI NA MODEL: accountId => " + accountId)

    val sc = SparkContextSingleton.getSparkContext()
    val data = Array(accountId+1, accountId+2, accountId+3, accountId+4, accountId+5)
    val distData = sc.parallelize(data)
    distData.foreach(data => {
      println("Serial: " + data)
    })

    println("APOS FOREACH")

  }
}
