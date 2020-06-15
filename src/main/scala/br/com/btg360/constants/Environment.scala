package br.com.btg360.constants

import br.com.btg360.services.PropService

object Environment {

  val FILE_ENV: String = "environment.properties"

  val APP_ENV: String = "APP_ENV"

  val IS_DEDICATED_ENV: String = "IS_DEDICATED_ENV"

  val DEVELOPMENT: String = "development"

  val HOMOLOGATION: String = "homologation"

  val PRODUCTION: String = "production"

  val CLUSTER: String = "cluster"

  /**
    * @return Boolean
    */
  def isDevelopment: Boolean = {
    getAppEnv.equals(DEVELOPMENT)
  }

  /**
    * @return Boolean
    */
  def isHomologation: Boolean = {
    getAppEnv.equals(HOMOLOGATION)
  }

  /**
    * @return Boolean
    */
  def isProduction: Boolean = {
    getAppEnv.equals(PRODUCTION)
  }

  /**
    * @return Boolean
    */
  def isCluster: Boolean = {
    getAppEnv.equals(CLUSTER)
  }

  /**
    * @return String
    */
  def getAppEnv: String = {
    val value = new PropService().get(FILE_ENV).getProperty(APP_ENV)
    if (value == null || value.isEmpty || value.trim.equals(PRODUCTION)) {
      return PRODUCTION
    }
    value.trim
  }

  /**
    * @return Boolean
    */
  def isDedicatedEnv: Boolean = {
    val value = new PropService().get(FILE_ENV).getProperty(IS_DEDICATED_ENV)
    if (value == null || value.isEmpty || value.trim.equals("false")) {
      return false
    }
    true
  }

}
