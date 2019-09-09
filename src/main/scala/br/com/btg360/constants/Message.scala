package br.com.btg360.constants

object Message {

  val START_LOG: String = "START LOG ================================================================================"
  val END_LOG: String = "END LOG =================================================================================="
  val CHANNEL_RUNNING: String = "***** CHANNEL RUNNING: %s *****"
  val ITEMS_NOT_FOUND: String = "ITEMS NOT FOUND"
  val TOTAL_ITEMS_FOUND: String = "TOTAL ITEMS FOUND: %d"
  val TOTAL_DAILY_LIMIT_REMOVED: String = "QUANTITY OF ITEMS WITH DAILY SHIPPING LIMIT REMOVED: %d"
  val TOTAL_OPTOUT_REMOVED: String = "QUANTITY OF ITEMS WITH OPTOUT REMOVED: %d"
  val PRODUCT_TABLE_NAME: String = "PRODUCT TABLE NAME: %s"
  val CONSOLIDATED_TABLE_NAME: String = "CONSOLIDATED TABLE NAME: %s"
  val CONSOLIDATED_TABLE_NOT_FOUND: String = "CONSOLIDATED TABLE %s NOT FOUND"
  val PRODUCT_TABLE_NOT_FOUND: String = "PRODUCT TABLE %s NOT FOUND"
  val TRANSACTIONAL_SUCCESS: String = "DATA SAVED IN TRANSACTIONAL SUCCESSFULLY"
  val TRANSACTIONAL_NOT_REGISTER: String = "ERROR TRANSACTIONAL NOT REGISTER"
  val TRANSACTIONAL_ERROR: String = "ERROR WHILE TRYING TO SAVE DATA IN TRANSACTIONAL"
  val TEMPLATE_ERROR: String = "ERROR BY TRYING TO REGISTER THE TEMPLATE"
  val UPDATE_RULE_STATUS: String = "UPDATE RULE STATUS: (RULE ID = %d, STATUS = %d)"
  val UPDATE_CONSOLIDATED_STATUS: String = "UPDATE CONSOLIDATED STATUS: (IS SENT = %d, PLATFORM ID = %d)"
  val CONSOLIDATED_OR_PRODUCT_NOT_FOUND: String = "CONSOLIDATED OR PRODUCT NOT FOUND"

}
