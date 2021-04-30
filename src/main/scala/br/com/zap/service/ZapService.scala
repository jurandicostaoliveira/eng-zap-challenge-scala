package br.com.zap.service

import br.com.zap.domain.Immobile
import br.com.zap.handler.{GeoLocation => geo}
import br.com.zap.spark.SparkConfig
import org.apache.spark.rdd.RDD

class ZapService extends ImmobileServiceTrait {

  val PERCENTAGE: Int = 10

  val SQUARE_PRICE: Int = 3500

  /**
   *
   * @return
   */
  override def getTopic: String = "output-zap"

  /**
   *
   * @param data
   * @return
   */
  override def dataFilter(data: RDD[Immobile]): RDD[Immobile] = {
    try {
      data
        .filter(row => {
          row.address_geoLocation_location_lat != 0 || row.address_geoLocation_location_lon != 0
        })
        .filter(row => row.usableAreas.toInt > 0)
        .filter(row => {
          val sPrice = (row.pricingInfos_price.toInt / row.usableAreas.toInt)
          !(row.pricingInfos_businessType.equals("SALE") && (sPrice <= SQUARE_PRICE))
        })
    } catch {
      case e: Exception => throw new NullPointerException
    }
  }

  /**
   *
   * @param data
   * @return
   */
  override def dataMap(data: RDD[Immobile]): RDD[Immobile] = {
    try {
      var total: Int = 0
      var quantity: Int = 0
      var totalAvg = 0
      var immobileList: List[Immobile] = List()

      data.flatMap(row => {
        if (row.pricingInfos_businessType.equals("SALE")) {
          total += row.pricingInfos_price.toInt
          quantity += 1
          totalAvg = total / quantity
        }

        if (
          (row.address_geoLocation_location_lat >= geo.MAX_LAT && row.address_geoLocation_location_lat <= geo.MIN_LAT)
            &&
            (row.address_geoLocation_location_lon >= geo.MAX_LON && row.address_geoLocation_location_lon <= geo.MIN_LON)
        ) {
          val percentage = (PERCENTAGE * totalAvg) / 100
          val minPrice = totalAvg - percentage
          if (row.pricingInfos_price.toInt >= minPrice) {
            immobileList = immobileList :+ row
          }
        } else {
          immobileList = immobileList :+ row
        }

        immobileList.iterator
      }).distinct()
    } catch {
      case e: Exception => throw new NullPointerException
    }
  }

}
