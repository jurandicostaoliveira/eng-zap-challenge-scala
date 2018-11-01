package br.com.btg360.logger

import java.io.PrintStream

import br.com.btg360.extras.jimmoore.LoggingOutputStream
import org.apache.log4j.{Level, Logger, RollingFileAppender, SimpleLayout}

object PrintLogger {

  def create(path: String) {
    val logger: Logger = Logger.getRootLogger
    val appender: RollingFileAppender = new RollingFileAppender(new SimpleLayout(), path, true)
    logger.setLevel(Level.INFO)
    logger.addAppender(appender)
    Console.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true))
  }

}
