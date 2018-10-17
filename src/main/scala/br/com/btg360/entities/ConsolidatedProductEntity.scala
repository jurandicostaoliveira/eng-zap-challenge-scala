package br.com.btg360.entities

import br.com.btg360.application.Entity

class ConsolidatedProductEntity extends Entity {

  //Product
  var attribute1: String = _
  var attribute2: String = _
  var attribute3: String = _
  var attribute4: String = _
  var attribute5: String = _
  var attribute6: String = _
  var attribute7: String = _
  var attribute8: String = _
  var attribute9: String = _
  var attribute10: String = _
  var attribute11: String = _
  var attribute12: String = _
  var attribute13: String = _
  var attribute14: String = _
  var attribute15: String = _
  var availability: Int = 0
  var barCode: String = _
  var brand: String = _
  var category: String = _
  var city: String = _
  var condition: String = _
  var country: String = _
  var establishment: String = _
  var establishmentLink: String = _
  var finalDate: String = _
  var image: String = _
  var initDate: String = _
  var installment: Int = 0
  var installmentDesc: String = _
  var installmentValue: Float = 0
  var otherOffersLink: String = _
  var productId: String = _
  var price: String = _
  var priceBy: String = _
  var priceNum: Float = 0
  var priceByNum: Float = 0
  var productDesc: String = _
  var productGroupId: String = _
  var productLink: String = _
  var productName: String = _
  var quantity: Int = 0
  var state: String = _
  var subCategory: String = _
  var subCategoryTwo: String = _
  var totalQtySold: Int = _

  //Consolidated
  var userId: String = _
  var userSent: String = _
  var isSent: Int = 0
  var isRecommendation: Int = 0
  var percentage: Double = 0
  var platformId: Int = 0
  var createdAt: String = _

  def setRow(consolidatedEntity: ConsolidatedEntity, productEntity: ProductEntity): ConsolidatedProductEntity = {
    this.userId = consolidatedEntity.userId
    this.userSent = consolidatedEntity.userSent
    this.isSent = consolidatedEntity.isSent
    this.isRecommendation = consolidatedEntity.isRecommendation
    this.percentage = consolidatedEntity.percentage
    this.platformId = consolidatedEntity.platformId
    this.createdAt = consolidatedEntity.createdAt
    //
    this.attribute1 = productEntity.attribute1
    this.attribute2 = productEntity.attribute2
    this.attribute3 = productEntity.attribute3
    this.attribute4 = productEntity.attribute4
    this.attribute5 = productEntity.attribute5
    this.attribute6 = productEntity.attribute6
    this.attribute7 = productEntity.attribute7
    this.attribute8 = productEntity.attribute8
    this.attribute9 = productEntity.attribute9
    this.attribute10 = productEntity.attribute10
    this.attribute11 = productEntity.attribute11
    this.attribute12 = productEntity.attribute12
    this.attribute13 = productEntity.attribute13
    this.attribute14 = productEntity.attribute14
    this.attribute15 = productEntity.attribute15
    this.availability = productEntity.availability
    this.barCode = productEntity.barCode
    this.brand = productEntity.brand
    this.category = productEntity.category
    this.city = productEntity.city
    this.condition = productEntity.condition
    this.country = productEntity.country
    this.establishment = productEntity.establishment
    this.establishmentLink = productEntity.establishmentLink
    this.finalDate = productEntity.finalDate
    this.image = productEntity.image
    this.initDate = productEntity.initDate
    this.installment = productEntity.installment
    this.installmentDesc = productEntity.installmentDesc
    this.installmentValue = productEntity.installmentValue
    this.otherOffersLink = productEntity.otherOffersLink
    this.productId = productEntity.productId
    this.price = productEntity.price
    this.priceBy = productEntity.priceBy
    this.priceNum = productEntity.priceNum
    this.priceByNum = productEntity.priceByNum
    this.productDesc = productEntity.productDesc
    this.productGroupId = productEntity.productGroupId
    this.productLink = productEntity.productLink
    this.productName = productEntity.productName
    this.quantity = productEntity.quantity
    this.state = productEntity.state
    this.subCategory = productEntity.subCategory
    this.subCategoryTwo = productEntity.subCategoryTwo
    this.totalQtySold = productEntity.totalQtySold
    this
  }

}
