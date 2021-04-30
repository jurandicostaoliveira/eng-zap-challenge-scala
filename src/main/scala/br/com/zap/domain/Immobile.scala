package br.com.zap.domain

case class Immobile(
                     usableAreas: Long = 0,
                     listingType: String = "",
                     createdAt: String = "",
                     listingStatus: String = "",
                     id: String = "",
                     parkingSpaces: Any = null,
                     updatedAt: String = "",
                     owner: Boolean = false,
                     images: java.util.List[String] = null,
                     address_city: String = "",
                     address_neighborhood: String = "",
                     address_geoLocation_precision: String = "",
                     address_geoLocation_location_lon: Double = 0,
                     address_geoLocation_location_lat: Double = 0,
                     bathrooms: Long = 0,
                     bedrooms: Long = 0,
                     pricingInfos_yearlyIptu: String = "",
                     pricingInfos_price: String = "",
                     pricingInfos_businessType: String = "",
                     pricingInfos_monthlyCondoFee: String = ""
                   ) {

  override def toString: String = {
    s""" ----------------------------------------------------------------------------------------
    UsableAreas: ${usableAreas}
    ListingType: ${listingType}
    CreatedAt: ${createdAt}
    ListingStatus: ${listingStatus}
    UpdatedAt: ${updatedAt}
    Images: ${images}
    City: ${address_city}
    Neighborhood: ${address_neighborhood}
    YearlyIptu: ${pricingInfos_yearlyIptu}
    Price: ${pricingInfos_price}
    BusinessType: ${pricingInfos_businessType}
    MonthlyCondoFee: ${pricingInfos_monthlyCondoFee}
    Address.geoLocation.location.lon: ${address_geoLocation_location_lon}
    Address.geoLocation.location.lat: ${address_geoLocation_location_lat}
    """
  }
}