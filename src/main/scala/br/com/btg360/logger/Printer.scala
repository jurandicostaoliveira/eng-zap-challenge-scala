package br.com.btg360.logger

import java.io.PrintStream
import br.com.btg360.extras.jimmoore.LoggingOutputStream
import org.apache.log4j.{Level, Logger, PatternLayout, RollingFileAppender}

class Printer extends Serializable {

  /**
    * Print to custom file
    *
    * @param String path
    * @return
    */
  def inFile(path: String, maxFileSize: String = "100MB", maxBackupIndex: Int = 10): Logger = {
    val logger: Logger = Logger.getRootLogger
    try {
      val layout: PatternLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p - %m%n")
      val appender: RollingFileAppender = new RollingFileAppender(layout, path, true)
      appender.setMaxFileSize(maxFileSize)
      appender.setMaxBackupIndex(maxBackupIndex)
      logger.setLevel(Level.INFO)
      logger.addAppender(appender)
      Console.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true))
      logger
    } catch {
      case e: Exception => println(e.printStackTrace())
        logger
    }
  }

}
