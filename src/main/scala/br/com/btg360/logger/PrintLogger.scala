package br.com.btg360.logger

import java.io.PrintStream
import br.com.btg360.extras.jimmoore.LoggingOutputStream
import org.apache.log4j.{Level, Logger}

object PrintLogger {

  def create(LOG : Logger) {
      Console.setOut(new PrintStream(new LoggingOutputStream(LOG, Level.INFO), true))
  }

}
