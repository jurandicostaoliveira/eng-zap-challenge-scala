package br.com.btg360.run

import br.com.btg360.spark.SparkContextSingleton
import org.apache.spark.rdd.RDD

object RDDExample {

  def main(args: Array[String]) {
    val p1 = new ProductTest()
    p1.name = "Notebook"
    val p2 = new ProductTest()
    p2.name = "Tv"
    val list: List[ProductTest] = List(p1, p2)

    val sc = SparkContextSingleton.getSparkContext()
    val rdd: RDD[ProductTest] = sc.parallelize(list)

    rdd.foreach(row => {
      println(">> " + row.name)
    })
  }

}

class ProductTest extends Serializable {

  var name: String = ""

}
