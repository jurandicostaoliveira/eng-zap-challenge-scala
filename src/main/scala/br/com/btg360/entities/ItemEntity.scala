package br.com.btg360.entities

import br.com.btg360.application.Entity
import br.com.btg360.services.UtmService

import scala.collection.mutable.HashMap
import scala.collection.Map

case class ItemEntity(
                       products: List[HashMap[String, Any]] = List(),
                       recommendations: List[HashMap[String, Any]] = List(),
                       references: Map[String, Any] = Map()
                     ) extends Entity {

  /**
    * Convert to data map
    *
    * @param consolidatedEntity
    * @param productEntity
    * @return Map
    */
  def toMap(consolidatedEntity: ConsolidatedEntity, productEntity: ProductEntity, queue: QueueEntity): HashMap[String, Any] = {
    HashMap(
      "userId" -> consolidatedEntity.userId,
      "userSent" -> consolidatedEntity.userSent,
      "isSent" -> consolidatedEntity.isSent,
      "isRecommendation" -> consolidatedEntity.isRecommendation,
      "percentage" -> consolidatedEntity.percentage,
      "platformId" -> consolidatedEntity.platformId,
      "createdAt" -> consolidatedEntity.createdAt,
      //
      "attribute1" -> productEntity.attribute1,
      "attribute2" -> productEntity.attribute2,
      "attribute3" -> productEntity.attribute3,
      "attribute4" -> productEntity.attribute4,
      "attribute5" -> productEntity.attribute5,
      "attribute6" -> productEntity.attribute6,
      "attribute7" -> productEntity.attribute7,
      "attribute8" -> productEntity.attribute8,
      "attribute9" -> productEntity.attribute9,
      "attribute10" -> productEntity.attribute10,
      "attribute11" -> productEntity.attribute11,
      "attribute12" -> productEntity.attribute12,
      "attribute13" -> productEntity.attribute13,
      "attribute14" -> productEntity.attribute14,
      "attribute15" -> productEntity.attribute15,
      "availability" -> productEntity.availability,
      "barCode" -> productEntity.barCode,
      "brand" -> productEntity.brand,
      "category" -> productEntity.category,
      "city" -> productEntity.city,
      "condition" -> productEntity.condition,
      "country" -> productEntity.country,
      "establishment" -> productEntity.establishment,
      "establishmentLink" -> productEntity.establishmentLink,
      "finalDate" -> productEntity.finalDate,
      "image" -> productEntity.image,
      "initDate" -> productEntity.initDate,
      "installment" -> productEntity.installment,
      "installmentDesc" -> productEntity.installmentDesc,
      "installmentValue" -> productEntity.installmentValue,
      "otherOffersLink" -> productEntity.otherOffersLink,
      "productId" -> productEntity.productId,
      "price" -> productEntity.price,
      "priceBy" -> productEntity.priceBy,
      "priceNum" -> productEntity.priceNum,
      "priceByNum" -> productEntity.priceByNum,
      "productDesc" -> productEntity.productName, //Hack .: productDesc
      "productGroupId" -> productEntity.productGroupId,
      "productLink" -> new UtmService().generateLink(queue, productEntity.productLink, productEntity.productId),
      "productName" -> productEntity.productName,
      "quantity" -> productEntity.quantity,
      "state" -> productEntity.state,
      "subCategory" -> productEntity.subCategory,
      "subCategoryTwo" -> productEntity.subCategoryTwo,
      "totalQtySold" -> productEntity.totalQtySold
    )
  }

}
