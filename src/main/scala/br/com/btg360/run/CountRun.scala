package br.com.btg360.run

import br.com.btg360.constants.Keyspace
import br.com.btg360.spark.SparkCoreSingleton
import com.datastax.spark.connector.{CassandraRow, toSparkContextFunctions}


object CountRun extends App {

  try {

    List(8932, 8165, 353).foreach(btgId => {
      val rdd = SparkCoreSingleton.getContext
        .cassandraTable[CassandraRow](Keyspace.BTG360, "cookie_email_8932")
        .select("email")
        .distinct()

      println(" BTGID: " + btgId + " >> " + rdd.count())
    })

  } catch {
    case e: Exception => e.printStackTrace()
  }


}