package br.com.btg360.constants

object Rule {

  val DAILY_ID: Int = 1
  val DAILY: String = "daily"

  val HOURLY_ID: Int = 2
  val HOURLY: String = "hourly"

  val AUTOMATIC_ID: Int = 3
  val AUTOMATIC: String = "automatic"

  /**
    * RULE GROUPS ID
    */
  val NAVIGATION_GROUP_ID: Int = 1
  val AFTER_BUY_GROUP_ID: Int = 2
  val SEARCH_GROUP_ID: Int = 3
  val NEWS_GROUP_ID: Int = 4
  val DECIDED_GROUP_ID: Int = 5
  val WISH_LIST_GROUP_ID: Int = 6
  val RECURRENCE_GROUP_ID: Int = 7
  val REGAIN_GROUP_ID: Int = 8
  val FIDELITY_GROUP_ID: Int = 9
  val UNDECIDED_GROUP_ID: Int = 10
  val WARN_ME_GROUP_ID: Int = 11
  val CART_ABANDONMENT_GROUP_ID: Int = 12
  val PRICE_REDUCTION_GROUP_ID: Int = 13
  val SIMILAR_PRICE_REDUCTION_GROUP_ID: Int = 14
  val TREND_GROUP_ID: Int = 15
  val BIRTHDAY_GROUP_ID: Int = 16
  val INACTIVE_GROUP_ID: Int = 17
  val SENDING_DATE_GROUP_ID: Int = 18

  /**
    * Check removal of daily limit
    *
    * @param Int ruleGroupId
    * @return Boolean
    */
  def isRemoveDailyLimit(ruleGroupId: Int): Boolean = {
    val group: List[Int] = List(CART_ABANDONMENT_GROUP_ID)
    if (group.contains(ruleGroupId)) {
      return false
    }

    return true
  }

}
