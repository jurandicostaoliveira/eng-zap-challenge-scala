package br.com.btg360.run

import br.com.btg360.constants.Keyspace
import br.com.btg360.spark.SparkContextSingleton
import com.datastax.spark.connector.{CassandraRow, toSparkContextFunctions}
import org.apache.spark.rdd.RDD

object DeleteCookieEmail extends App {

  val sc = SparkContextSingleton.getSparkContext()
  //val tableName: String = "cookie_email_8165"
  val tableName: String = "cookie_email_9"

  def count: Unit = {

    val total = sc.cassandraTable(Keyspace.BTG360, tableName).count()
    println(">>>> TOTAL : " + total)

  }

  def map: Unit = {

    val data: RDD[(String, CassandraRow)] = sc.cassandraTable(Keyspace.BTG360, tableName)
      .keyBy(row => row.getString("cookie_bid"))

    var total = 0
    data.groupByKey().foreach(row => {
      val count = row._2.toList.size
      if(count <= 1) {
        total += 1
        println(row._1 + " -> "+ count + " -> " + total)
      }
    })

  }

  map


}
