package br.com.btg360.entities

import br.com.btg360.application.Entity
import br.com.btg360.services.UrlService

import scala.collection.mutable.HashMap
import scala.collection.Map

case class StockEntity(
                        products: List[HashMap[String, Any]] = List(),
                        recommendations: List[HashMap[String, Any]] = List(),
                        references: Map[String, Any] = Map()
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
             queue: QueueEntity,
             consolidated: ConsolidatedEntity,
             product: ProductEntity,
             position: Int = 0
           ): HashMap[String, Any] = {
    HashMap(
      "userId" -> consolidated.userId,
      "userSent" -> consolidated.userSent,
      "isSent" -> consolidated.isSent,
      "isRecommendation" -> consolidated.isRecommendation,
      "percentage" -> consolidated.percentage,
      "platformId" -> consolidated.platformId,
      "createdAt" -> consolidated.createdAt,
      //
      "attribute1" -> product.attribute1,
      "attribute2" -> product.attribute2,
      "attribute3" -> product.attribute3,
      "attribute4" -> product.attribute4,
      "attribute5" -> product.attribute5,
      "attribute6" -> product.attribute6,
      "attribute7" -> product.attribute7,
      "attribute8" -> product.attribute8,
      "attribute9" -> product.attribute9,
      "attribute10" -> product.attribute10,
      "attribute11" -> product.attribute11,
      "attribute12" -> product.attribute12,
      "attribute13" -> product.attribute13,
      "attribute14" -> product.attribute14,
      "attribute15" -> product.attribute15,
      "availability" -> product.availability,
      "barCode" -> product.barCode,
      "brand" -> product.brand,
      "category" -> product.category,
      "city" -> product.city,
      "condition" -> product.condition,
      "country" -> product.country,
      "establishment" -> product.establishment,
      "establishmentLink" -> product.establishmentLink,
      "finalDate" -> product.finalDate,
      "image" -> product.image,
      "initDate" -> product.initDate,
      "installment" -> product.installment,
      "installmentDesc" -> product.installmentDesc,
      "installmentValue" -> product.installmentValue,
      "otherOffersLink" -> product.otherOffersLink,
      "productId" -> product.productId,
      "price" -> product.price,
      "priceBy" -> product.priceBy,
      "priceNum" -> product.priceNum,
      "priceByNum" -> product.priceByNum,
      "productDesc" -> product.productName, //Hack .: productDesc
      "productGroupId" -> product.productGroupId,
      "productLink" -> new UrlService().redirect(
        queue,
        product.productLink,
        product.productId,
        consolidated.isRecommendation,
        position
      ),
      "productName" -> product.productName,
      "quantity" -> product.quantity,
      "state" -> product.state,
      "subCategory" -> product.subCategory,
      "subCategoryTwo" -> product.subCategoryTwo,
      "totalQtySold" -> product.totalQtySold
    )
  }

}
