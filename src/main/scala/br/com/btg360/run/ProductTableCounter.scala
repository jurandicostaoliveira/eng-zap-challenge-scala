package br.com.btg360.run

import br.com.btg360.constants.Keyspace
import br.com.btg360.logger.PrintLogger
import br.com.btg360.spark.SparkCoreSingleton
import com.datastax.spark.connector.toSparkContextFunctions
import org.apache.log4j.Logger

object ProductTableCounter {

  val TABLE_NAME: String = "product"

  def main(args: Array[String]) {
    val allinId = readLine("Digite o Allin ID : ")
    val tableName = "%s_%s".format(ProductTableCounter.TABLE_NAME, allinId.trim())

    System.setProperty("LOG_CUSTOM_NAME", tableName)
    val LOG = Logger.getRootLogger()
    //PrintLogger.create(LOG)

    val sc = SparkCoreSingleton.getContext
    val total = sc.cassandraTable(Keyspace.BTG360, tableName).count()
    LOG.info(">>Total product in table '%s' : %d".format(tableName, total))
    sc.stop()
  }

}


