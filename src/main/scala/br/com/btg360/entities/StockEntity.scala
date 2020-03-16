package br.com.btg360.entities

import br.com.btg360.application.Entity

import scala.collection.Map
import scala.collection.mutable.HashMap

case class StockEntity(
                        products: List[JsonProductEntity] = List(),
                        recommendations: List[JsonProductEntity] = List(),
                        references: Map[String, Any] = Map(),
                        configs: Map[String, Any] = Map(),
                        email: String = "",
                        client: String = "",
                        pixel: String = "",
                        virtual_mta: String = ""
                      ) extends Entity {


  /**
    * Convert to data entity class
    *
    * @param ConsolidatedEntity consolidated
    * @param ProductEntity      product
    * @return JsonProductEntity
    */
  def toJsonProduct(
                     consolidated: ConsolidatedEntity,
                     product: ProductEntity
                   ): JsonProductEntity = {
    new JsonProductEntity(
      userId = consolidated.userId,
      userSent = consolidated.userSent,
      isSent = consolidated.isSent,
      isRecommendation = consolidated.isRecommendation,
      percentage = consolidated.percentage,
      platformId = consolidated.platformId,
      createdAt = consolidated.createdAt,
      //
      attribute1 = product.attribute1,
      attribute2 = product.attribute2,
      attribute3 = product.attribute3,
      attribute4 = product.attribute4,
      attribute5 = product.attribute5,
      attribute6 = product.attribute6,
      attribute7 = product.attribute7,
      attribute8 = product.attribute8,
      attribute9 = product.attribute9,
      attribute10 = product.attribute10,
      attribute11 = product.attribute11,
      attribute12 = product.attribute12,
      attribute13 = product.attribute13,
      attribute14 = product.attribute14,
      attribute15 = product.attribute15,
      availability = product.availability,
      barCode = product.barCode,
      brand = product.brand,
      category = product.category,
      city = product.city,
      condition = product.condition,
      country = product.country,
      establishment = product.establishment,
      establishmentLink = product.establishmentLink,
      finalDate = product.finalDate,
      image = product.image,
      initDate = product.initDate,
      installment = product.installment,
      installmentDesc = product.installmentDesc,
      installmentValue = product.installmentValue,
      otherOffersLink = product.otherOffersLink,
      productId = product.productId,
      price = product.price,
      priceBy = product.priceBy,
      priceNum = product.priceNum,
      priceByNum = product.priceByNum,
      productDesc = product.productName, //Hack .: productDesc
      productGroupId = product.productGroupId,
      productLink = product.productLink,
      productName = product.productName,
      quantity = product.quantity,
      state = product.state,
      subCategory = product.subCategory,
      subCategoryTwo = product.subCategoryTwo,
      totalQtySold = product.totalQtySold
    )
  }

  /**
    * Convert to data hash map
    *
    * @param QueueEntity        queue
    * @param ConsolidatedEntity consolidated
    * @param ProductEntity      product
    * @return HashMap
    */
//  def toMap(
//             consolidated: ConsolidatedEntity,
//             product: ProductEntity
//           ): HashMap[String, Any] = {
//    HashMap(
//      "userId" -> consolidated.userId,
//      "userSent" -> consolidated.userSent,
//      "isSent" -> consolidated.isSent,
//      "isRecommendation" -> consolidated.isRecommendation,
//      "percentage" -> consolidated.percentage,
//      "platformId" -> consolidated.platformId,
//      "createdAt" -> consolidated.createdAt,
//      //
//      "attribute1" -> product.attribute1,
//      "attribute2" -> product.attribute2,
//      "attribute3" -> product.attribute3,
//      "attribute4" -> product.attribute4,
//      "attribute5" -> product.attribute5,
//      "attribute6" -> product.attribute6,
//      "attribute7" -> product.attribute7,
//      "attribute8" -> product.attribute8,
//      "attribute9" -> product.attribute9,
//      "attribute10" -> product.attribute10,
//      "attribute11" -> product.attribute11,
//      "attribute12" -> product.attribute12,
//      "attribute13" -> product.attribute13,
//      "attribute14" -> product.attribute14,
//      "attribute15" -> product.attribute15,
//      "availability" -> product.availability,
//      "barCode" -> product.barCode,
//      "brand" -> product.brand,
//      "category" -> product.category,
//      "city" -> product.city,
//      "condition" -> product.condition,
//      "country" -> product.country,
//      "establishment" -> product.establishment,
//      "establishmentLink" -> product.establishmentLink,
//      "finalDate" -> product.finalDate,
//      "image" -> product.image,
//      "initDate" -> product.initDate,
//      "installment" -> product.installment,
//      "installmentDesc" -> product.installmentDesc,
//      "installmentValue" -> product.installmentValue,
//      "otherOffersLink" -> product.otherOffersLink,
//      "productId" -> product.productId,
//      "price" -> product.price,
//      "priceBy" -> product.priceBy,
//      "priceNum" -> product.priceNum,
//      "priceByNum" -> product.priceByNum,
//      "productDesc" -> product.productName, //Hack .: productDesc
//      "productGroupId" -> product.productGroupId,
//      "productLink" -> product.productLink,
//      "productName" -> product.productName,
//      "quantity" -> product.quantity,
//      "state" -> product.state,
//      "subCategory" -> product.subCategory,
//      "subCategoryTwo" -> product.subCategoryTwo,
//      "totalQtySold" -> product.totalQtySold
//    )
//  }

}