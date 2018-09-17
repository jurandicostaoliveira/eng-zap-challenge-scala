package br.com.btg360.run

import br.com.btg360.constants.Keyspace
import br.com.btg360.spark.SparkContextSingleton
import com.datastax.spark.connector.toSparkContextFunctions
import org.apache.log4j.{Logger}


object ProductTableCounter {

  val TABLE_NAME: String = "product"

  def main(args: Array[String]) {
    val allinId = readLine("Digite o Allin ID : ")
    val tableName = String.format("%s_%s", ProductTableCounter.TABLE_NAME, allinId.trim())

    System.setProperty("LOG_CUSTOM_NAME", tableName)
    val LOG = Logger.getRootLogger()

    val sc = SparkContextSingleton.getSparkContext()
    val total = sc.cassandraTable(Keyspace.BTG360, tableName).count()
    System.out.println(">>Total product in table '%s' : %d".format(tableName, total))
    LOG.info(">>Total product in table '%s' : %d".format(tableName, total))
    sc.stop()
  }

}