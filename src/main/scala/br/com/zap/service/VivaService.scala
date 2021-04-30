package br.com.zap.service

import br.com.zap.domain.Immobile
import br.com.zap.handler.{GeoLocation => geo}
import org.apache.spark.rdd.RDD

class VivaService extends ImmobileServiceTrait {

  val CONDO_FEE_PERCENTAGE: Int = 30

  val RENTAL_PERCENTAGE: Int = 50

  /**
   *
   * @return
   */
  override protected def getTopic: String = "output-viva"

  /**
   *
   * @param data
   * @return
   */
  override protected def dataFilter(data: RDD[Immobile]): RDD[Immobile] = {
    data
      .filter(row => {
        row.address_geoLocation_location_lat != 0 || row.address_geoLocation_location_lon != 0
      })
      .filter(row => {
        !row.pricingInfos_monthlyCondoFee.isEmpty ||
          !row.pricingInfos_monthlyCondoFee.equals("0")
      })
      .filter(row => {
        val calc = (row.pricingInfos_monthlyCondoFee.toInt * 100) / row.pricingInfos_price.toInt
        !(row.pricingInfos_businessType.equals("RENTAL") && (calc >= CONDO_FEE_PERCENTAGE))
      })
  }

  /**
   *
   * @param data
   * @return
   */
  override protected def dataMap(data: RDD[Immobile]): RDD[Immobile] = {
    var total: Int = 0
    var quantity: Int = 0
    var totalAvg = 0
    var immobileList: List[Immobile] = List()

    data.flatMap(row => {
      if (row.pricingInfos_businessType.equals("RENTAL")) {
        total += row.pricingInfos_price.toInt
        quantity += 1
        totalAvg = total / quantity
      }

      if (
        (row.address_geoLocation_location_lat >= geo.MAX_LAT && row.address_geoLocation_location_lat <= geo.MIN_LAT)
          &&
          (row.address_geoLocation_location_lon >= geo.MAX_LON && row.address_geoLocation_location_lon <= geo.MIN_LON)
      ) {
        val percentage = (RENTAL_PERCENTAGE * totalAvg) / 100
        val maxRental = totalAvg + percentage
        if (row.pricingInfos_price.toInt <= maxRental) {
          immobileList = immobileList :+ row
        }
      } else {
        immobileList = immobileList :+ row
      }

      immobileList.iterator.take(1)
    })
  }
}
