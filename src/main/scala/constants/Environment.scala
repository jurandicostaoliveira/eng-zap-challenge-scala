package constants

import services.PropService

object Environment {

  val FILE_ENV : String = ".environment.properties"

  val APP_ENV : String = "APP_ENV"

  val DEVELOPMENT : String = "development"

  val HOMOLOGATION : String = "homologation"

  val PRODUCTION : String = "production"

  def isDevelopment : Boolean = { getAppEnv.equals(DEVELOPMENT) }

  def isHomologation : Boolean = { getAppEnv.equals(HOMOLOGATION) }

  def isProduction : Boolean = { getAppEnv.equals(PRODUCTION) }

  def getAppEnv : String = {
    val appEnv = new PropService().get(FILE_ENV).getProperty(APP_ENV)
    if(appEnv == null || appEnv.isEmpty || appEnv.trim.equals(PRODUCTION) ) {
      return PRODUCTION
    }
    appEnv.trim
  }
}
