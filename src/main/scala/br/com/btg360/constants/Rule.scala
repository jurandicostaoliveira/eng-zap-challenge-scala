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
    * RULE LABELS
    */
  val NAVIGATION_DAILY_LABEL: String = "navigation-daily"
  val NAVIGATION_WEEKLY_LABEL: String = "navigation-weekly"
  val NAVIGATION_MONTHLY_LABEL: String = "navigation-monthly"
  val AFTER_BUY_LABEL: String = "after-buy"
  val SEARCH_LABEL: String = "search"
  val NEWS_LABEL: String = "news"
  val DECIDED_LABEL: String = "decided"
  val WISH_LIST_LABEL: String = "wishlist"
  val RECURRENCE_LABEL: String = "recurrence"
  val REGAIN_LABEL: String = "regain"
  val FIDELITY_LABEL: String = "fidelity"
  val UNDECIDED_LABEL: String = "undecided"
  val WARN_ME_LABEL: String = "warn-me"
  val CART_ABANDONMENT_LABEL: String = "cart-abandonment"
  val PRICE_REDUCTION_LABEL: String = "price-reduction"
  val SIMILAR_PRICE_REDUCTION_LABEL: String = "similar-price-reduction"
  val TREND_LABEL: String = "trend"
  val BIRTHDAY_LABEL: String = "birthday"
  val INACTIVE_LABEL: String = "inactive"
  val SENDING_DATE_LABEL: String = "sending-date"

  /**
    * Check removal of daily limit
    *
    * @param String ruleLabel
    * @return Boolean
    */
  def isRemoveDailyLimit(ruleLabel: String): Boolean = {
    val group: List[String] = List(CART_ABANDONMENT_LABEL)
    if (group.contains(ruleLabel)) {
      return false
    }

    return true
  }

}
