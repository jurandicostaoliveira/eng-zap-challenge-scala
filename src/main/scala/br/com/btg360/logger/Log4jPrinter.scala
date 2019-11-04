package br.com.btg360.logger

import java.io.PrintStream

import br.com.btg360.extras.jimmoore.LoggingOutputStream
import org.apache.log4j.{Level, Logger, PatternLayout, RollingFileAppender}

object Log4jPrinter extends Serializable {

  @transient var logger: Logger = _

  /**
    * Print to custom file
    *
    * @param String path
    * @return
    */
  def configure(path: String, maxFileSize: String = "100MB", maxBackupIndex: Int = 10) = {
    this.logger = Logger.getLogger(path)
    try {
      if (this.logger.getAllAppenders.hasMoreElements) {
        this.logger.removeAllAppenders()
      }
      val layout: PatternLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p - %m%n")
      val appender: RollingFileAppender = new RollingFileAppender(layout, path, true)
      appender.setMaxFileSize(maxFileSize)
      appender.setMaxBackupIndex(maxBackupIndex)
      this.logger.setLevel(Level.INFO)
      this.logger.addAppender(appender)
      Console.setOut(new PrintStream(new LoggingOutputStream(this.logger, Level.INFO), true))
    } catch {
      case e: Exception => println(e.printStackTrace())
    }
  }

  /**
    * Getter
    *
    * @return Logger
    */
  def get: Logger = this.logger

}
