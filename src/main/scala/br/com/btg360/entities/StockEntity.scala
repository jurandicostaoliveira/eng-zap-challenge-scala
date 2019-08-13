package br.com.btg360.entities

import br.com.btg360.application.Entity
import br.com.btg360.constants.CustomEscape

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

    val productName = CustomEscape.toJson(product.productName)
    HashMap(
      "userId" -> consolidated.userId,
      "userSent" -> consolidated.userSent,
      "isSent" -> consolidated.isSent,
      "isRecommendation" -> consolidated.isRecommendation,
      "percentage" -> consolidated.percentage,
      "platformId" -> consolidated.platformId,
      "createdAt" -> consolidated.createdAt,
      //
      "attribute1" -> CustomEscape.toJson(product.attribute1),
      "attribute2" -> CustomEscape.toJson(product.attribute2),
      "attribute3" -> CustomEscape.toJson(product.attribute3),
      "attribute4" -> CustomEscape.toJson(product.attribute4),
      "attribute5" -> CustomEscape.toJson(product.attribute5),
      "attribute6" -> CustomEscape.toJson(product.attribute6),
      "attribute7" -> CustomEscape.toJson(product.attribute7),
      "attribute8" -> CustomEscape.toJson(product.attribute8),
      "attribute9" -> CustomEscape.toJson(product.attribute9),
      "attribute10" -> CustomEscape.toJson(product.attribute10),
      "attribute11" -> CustomEscape.toJson(product.attribute11),
      "attribute12" -> CustomEscape.toJson(product.attribute12),
      "attribute13" -> CustomEscape.toJson(product.attribute13),
      "attribute14" -> CustomEscape.toJson(product.attribute14),
      "attribute15" -> CustomEscape.toJson(product.attribute15),
      "availability" -> product.availability,
      "barCode" -> product.barCode,
      "brand" -> CustomEscape.toJson(product.brand),
      "category" -> CustomEscape.toJson(product.category),
      "city" -> CustomEscape.toJson(product.city),
      "condition" -> CustomEscape.toJson(product.condition),
      "country" -> CustomEscape.toJson(product.country),
      "establishment" -> CustomEscape.toJson(product.establishment),
      "establishmentLink" -> CustomEscape.toJson(product.establishmentLink),
      "finalDate" -> product.finalDate,
      "image" -> CustomEscape.toJson(product.image),
      "initDate" -> product.initDate,
      "installment" -> product.installment,
      "installmentDesc" -> product.installmentDesc,
      "installmentValue" -> product.installmentValue,
      "otherOffersLink" -> CustomEscape.toJson(product.otherOffersLink),
      "productId" -> CustomEscape.toJson(product.productId),
      "price" -> CustomEscape.toJson(product.price),
      "priceBy" -> CustomEscape.toJson(product.priceBy),
      "priceNum" -> product.priceNum,
      "priceByNum" -> product.priceByNum,
      "productDesc" -> productName, //Hack .: productDesc
      "productGroupId" -> product.productGroupId,
      "productLink" -> CustomEscape.toJson(product.productLink),
      "productName" -> productName,
      "quantity" -> product.quantity,
      "state" -> product.state,
      "subCategory" -> CustomEscape.toJson(product.subCategory),
      "subCategoryTwo" -> CustomEscape.toJson(product.subCategoryTwo),
      "totalQtySold" -> product.totalQtySold
    )
  }

}