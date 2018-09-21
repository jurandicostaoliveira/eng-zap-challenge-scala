package br.com.btg360.run

import br.com.btg360.spark.SparkContextSingleton
import com.datastax.spark.connector._

object Main {
  def main(args: Array[String]) {
    val sc = SparkContextSingleton.getSparkContext()

    val cart = sc.cassandraTable("btg360", "cart_15")
      .where("created_at >= ? AND created_at <= ?", "2018-08-07", "2018-08-07")

    cart.foreach(action => {
      println(action.getString("cookie_bid"))
    })

    val data = Array(1, 2, 3, 4, 5)
    val distData = sc.parallelize(data)
    distData.foreach(data => {
      println(data)
    })
    sc.stop()
  }
}
