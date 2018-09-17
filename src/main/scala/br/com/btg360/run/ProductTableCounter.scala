package br.com.btg360.run

import java.io.PrintStream

import br.com.btg360.constants.Keyspace
import br.com.btg360.extras.jimmoore.LoggingOutputStream
import br.com.btg360.spark.SparkContextSingleton
import com.datastax.spark.connector.toSparkContextFunctions
import org.apache.log4j.{Level, Logger}

object ProductTableCounter {

  val TABLE_NAME: String = "product"

  def main(args: Array[String]) {
    val allinId = readLine("Digite o Allin ID : ")
    val tableName = "%s_%s".format(ProductTableCounter.TABLE_NAME, allinId.trim())

    System.setProperty("LOG_CUSTOM_NAME", tableName)
    val LOG = Logger.getRootLogger()
    Console.setOut(new PrintStream(new LoggingOutputStream(LOG, Level.INFO), true))

    val sc = SparkContextSingleton.getSparkContext()
    val total = sc.cassandraTable(Keyspace.BTG360, tableName).count()
    LOG.info(">>Total product in table '%s' : %d".format(tableName, total))
    sc.stop()
  }

}


