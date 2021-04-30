package br.com.zap.repository

import br.com.zap.domain.Immobile
import br.com.zap.spark.SparkConfig
import org.apache.spark.rdd.RDD

class ImmobileRepository {

  val PATH = "src/main/resources/source.json"

  def all(): RDD[Immobile] = {
    try {
      SparkConfig.getSql
        .read
        .option("multiline", "true")
        .json(PATH)
        .select(
          "usableAreas",
          "listingType",
          "createdAt",
          "listingStatus",
          "id",
          "parkingSpaces",
          "updatedAt",
          "owner",
          "images",
          "address.city",
          "address.neighborhood",
          "address.geoLocation.precision",
          "address.geoLocation.location.lon",
          "address.geoLocation.location.lat",
          "bathrooms",
          "bedrooms",
          "pricingInfos.yearlyIptu",
          "pricingInfos.price",
          "pricingInfos.businessType",
          "pricingInfos.monthlyCondoFee"
        ).rdd.map(row => {
        Immobile(
          row.getLong(0),
          row.getString(1),
          row.getString(2),
          row.getString(3),
          row.getString(4),
          row.get(5),
          row.getString(6),
          row.getBoolean(7),
          row.getList[String](8),
          row.getString(9),
          row.getString(10),
          row.getString(11),
          row.getDouble(12),
          row.getDouble(13),
          row.getLong(14),
          row.getLong(15),
          row.getString(16),
          row.getString(17),
          row.getString(18),
          Option(row.getString(19)).getOrElse("0")
        )
      })
    } catch {
      case e: Exception => e.printStackTrace()
        SparkConfig.getContext.emptyRDD
    }
  }

}
