package br.com.btg360.entities

import br.com.btg360.application.Entity

import scala.collection.Map
import scala.collection.mutable.ListBuffer


class ConsolidatedProductEntity extends Entity {

  //Product
  var attribute1: String = ""
  var attribute2: String = ""
  var attribute3: String = ""
  var attribute4: String = ""
  var attribute5: String = ""
  var attribute6: String = ""
  var attribute7: String = ""
  var attribute8: String = ""
  var attribute9: String = ""
  var attribute10: String = ""
  var attribute11: String = ""
  var attribute12: String = ""
  var attribute13: String = ""
  var attribute14: String = ""
  var attribute15: String = ""
  var availability: Int = 0
  var barCode: String = ""
  var brand: String = ""
  var category: String = ""
  var city: String = ""
  var condition: String = ""
  var country: String = ""
  var establishment: String = ""
  var establishmentLink: String = ""
  var finalDate: String = ""
  var image: String = ""
  var initDate: String = ""
  var installment: Int = 0
  var installmentDesc: String = ""
  var installmentValue: Float = 0
  var otherOffersLink: String = ""
  var productId: String = ""
  var price: String = ""
  var priceBy: String = ""
  var priceNum: Float = 0
  var priceByNum: Float = 0
  var productDesc: String = ""
  var productGroupId: String = ""
  var productLink: String = ""
  var productName: String = ""
  var quantity: Int = 0
  var state: String = ""
  var subCategory: String = ""
  var subCategoryTwo: String = ""
  var totalQtySold: Int = 0

  //Consolidated
  var userId: String = ""
  var userSent: String = ""
  var isSent: Int = 0
  var isRecommendation: Int = 0
  var percentage: Double = 0
  var platformId: Int = 0
  var createdAt: String = ""

  def toListMap(consolidatedEntity: ConsolidatedEntity, productEntity: ProductEntity):
  Seq[(String, ListBuffer[Map[String, Any]])] => Map[String, ListBuffer[Map[String, Any]]] = {
    val map: Map[String, Any] = Map(
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
      "productDesc" -> productEntity.productDesc,
      "productGroupId" -> productEntity.productGroupId,
      "productLink" -> productEntity.productLink,
      "productName" -> productEntity.productName,
      "quantity" -> productEntity.quantity,
      "state" -> productEntity.state,
      "subCategory" -> productEntity.subCategory,
      "subCategoryTwo" -> productEntity.subCategoryTwo,
      "totalQtySold" -> productEntity.totalQtySold
    )

    val products: ListBuffer[Map[String, Any]] = ListBuffer()
    val recommendations: ListBuffer[Map[String, Any]] = ListBuffer()
    if (consolidatedEntity.isRecommendation.equals(1)) {
      recommendations.append(map)
    } else {
      products.append(map)
    }
    val data = Map[String, ListBuffer[Map[String, Any]]]
    data("recommendations") = recommendations
    data("products") = products
    data
  }

  //  def setRow(consolidatedEntity: ConsolidatedEntity, productEntity: ProductEntity): ConsolidatedProductEntity = {
  //    this.userId = consolidatedEntity.userId
  //    this.userSent = consolidatedEntity.userSent
  //    this.isSent = consolidatedEntity.isSent
  //    this.isRecommendation = consolidatedEntity.isRecommendation
  //    this.percentage = consolidatedEntity.percentage
  //    this.platformId = consolidatedEntity.platformId
  //    this.createdAt = consolidatedEntity.createdAt
  //    //
  //    this.attribute1 = productEntity.attribute1
  //    this.attribute2 = productEntity.attribute2
  //    this.attribute3 = productEntity.attribute3
  //    this.attribute4 = productEntity.attribute4
  //    this.attribute5 = productEntity.attribute5
  //    this.attribute6 = productEntity.attribute6
  //    this.attribute7 = productEntity.attribute7
  //    this.attribute8 = productEntity.attribute8
  //    this.attribute9 = productEntity.attribute9
  //    this.attribute10 = productEntity.attribute10
  //    this.attribute11 = productEntity.attribute11
  //    this.attribute12 = productEntity.attribute12
  //    this.attribute13 = productEntity.attribute13
  //    this.attribute14 = productEntity.attribute14
  //    this.attribute15 = productEntity.attribute15
  //    this.availability = productEntity.availability
  //    this.barCode = productEntity.barCode
  //    this.brand = productEntity.brand
  //    this.category = productEntity.category
  //    this.city = productEntity.city
  //    this.condition = productEntity.condition
  //    this.country = productEntity.country
  //    this.establishment = productEntity.establishment
  //    this.establishmentLink = productEntity.establishmentLink
  //    this.finalDate = productEntity.finalDate
  //    this.image = productEntity.image
  //    this.initDate = productEntity.initDate
  //    this.installment = productEntity.installment
  //    this.installmentDesc = productEntity.installmentDesc
  //    this.installmentValue = productEntity.installmentValue
  //    this.otherOffersLink = productEntity.otherOffersLink
  //    this.productId = productEntity.productId
  //    this.price = productEntity.price
  //    this.priceBy = productEntity.priceBy
  //    this.priceNum = productEntity.priceNum
  //    this.priceByNum = productEntity.priceByNum
  //    this.productDesc = productEntity.productDesc
  //    this.productGroupId = productEntity.productGroupId
  //    this.productLink = productEntity.productLink
  //    this.productName = productEntity.productName
  //    this.quantity = productEntity.quantity
  //    this.state = productEntity.state
  //    this.subCategory = productEntity.subCategory
  //    this.subCategoryTwo = productEntity.subCategoryTwo
  //    this.totalQtySold = productEntity.totalQtySold
  //    this
  //  }

}



