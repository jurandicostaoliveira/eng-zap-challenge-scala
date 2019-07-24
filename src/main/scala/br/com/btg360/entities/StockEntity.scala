package br.com.btg360.entities

import br.com.btg360.application.Entity
import org.apache.commons.lang3.StringEscapeUtils

import scala.collection.Map
import scala.collection.mutable.HashMap

case class StockEntity(
                        products: List[HashMap[String, Any]] = List(),
                        recommendations: List[HashMap[String, Any]] = List(),
                        references: Map[String, Any] = Map(),
                        configs: Map[String, Any] = Map(),
                        email: String = "",
                        client: String = "",
                        pixel: String = "",
                        virtual_mta: String = ""
                      ) extends Entity {

  /**
    * Convert to data hash map
    *
    * @param QueueEntity queue
    * @param consolidated
    * @param product
    * @param position
    * @return
    */
  def toMap(
             consolidated: ConsolidatedEntity,
             product: ProductEntity
           ): HashMap[String, Any] = {

    val productName = StringEscapeUtils.escapeJson(product.productName)
    HashMap(
      "userId" -> consolidated.userId,
      "userSent" -> consolidated.userSent,
      "isSent" -> consolidated.isSent,
      "isRecommendation" -> consolidated.isRecommendation,
      "percentage" -> consolidated.percentage,
      "platformId" -> consolidated.platformId,
      "createdAt" -> consolidated.createdAt,
      //
      "attribute1" -> StringEscapeUtils.escapeJson(product.attribute1),
      "attribute2" -> StringEscapeUtils.escapeJson(product.attribute2),
      "attribute3" -> StringEscapeUtils.escapeJson(product.attribute3),
      "attribute4" -> StringEscapeUtils.escapeJson(product.attribute4),
      "attribute5" -> StringEscapeUtils.escapeJson(product.attribute5),
      "attribute6" -> StringEscapeUtils.escapeJson(product.attribute6),
      "attribute7" -> StringEscapeUtils.escapeJson(product.attribute7),
      "attribute8" -> StringEscapeUtils.escapeJson(product.attribute8),
      "attribute9" -> StringEscapeUtils.escapeJson(product.attribute9),
      "attribute10" -> StringEscapeUtils.escapeJson(product.attribute10),
      "attribute11" -> StringEscapeUtils.escapeJson(product.attribute11),
      "attribute12" -> StringEscapeUtils.escapeJson(product.attribute12),
      "attribute13" -> StringEscapeUtils.escapeJson(product.attribute13),
      "attribute14" -> StringEscapeUtils.escapeJson(product.attribute14),
      "attribute15" -> StringEscapeUtils.escapeJson(product.attribute15),
      "availability" -> product.availability,
      "barCode" -> product.barCode,
      "brand" -> StringEscapeUtils.escapeJson(product.brand),
      "category" -> StringEscapeUtils.escapeJson(product.category),
      "city" -> StringEscapeUtils.escapeJson(product.city),
      "condition" -> StringEscapeUtils.escapeJson(product.condition),
      "country" -> StringEscapeUtils.escapeJson(product.country),
      "establishment" -> StringEscapeUtils.escapeJson(product.establishment),
      "establishmentLink" -> StringEscapeUtils.escapeJson(product.establishmentLink),
      "finalDate" -> product.finalDate,
      "image" -> StringEscapeUtils.escapeJson(product.image),
      "initDate" -> product.initDate,
      "installment" -> product.installment,
      "installmentDesc" -> product.installmentDesc,
      "installmentValue" -> product.installmentValue,
      "otherOffersLink" -> StringEscapeUtils.escapeJson(product.otherOffersLink),
      "productId" -> StringEscapeUtils.escapeJson(product.productId),
      "price" -> StringEscapeUtils.escapeJson(product.price),
      "priceBy" -> StringEscapeUtils.escapeJson(product.priceBy),
      "priceNum" -> product.priceNum,
      "priceByNum" -> product.priceByNum,
      "productDesc" -> productName, //Hack .: productDesc
      "productGroupId" -> product.productGroupId,
      "productLink" -> StringEscapeUtils.escapeJson(product.productLink),
      "productName" -> productName,
      "quantity" -> product.quantity,
      "state" -> product.state,
      "subCategory" -> StringEscapeUtils.escapeJson(product.subCategory),
      "subCategoryTwo" -> StringEscapeUtils.escapeJson(product.subCategoryTwo),
      "totalQtySold" -> product.totalQtySold
    )
  }

}