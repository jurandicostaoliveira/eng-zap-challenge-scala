package br.com.btg360.entities

import br.com.btg360.application.Entity

import scala.collection.Map

case class StockEntity(
                        products: List[JsonProductEntity] = List(),
                        recommendations: List[JsonProductEntity] = List(),
                        references: Map[String, Any] = Map(),
                        referencesToApp: Map[String, Any] = Map(),
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
      attribute1 = product.attribute1.getOrElse(""),
      attribute2 = product.attribute2.getOrElse(""),
      attribute3 = product.attribute3.getOrElse(""),
      attribute4 = product.attribute4.getOrElse(""),
      attribute5 = product.attribute5.getOrElse(""),
      attribute6 = product.attribute6.getOrElse(""),
      attribute7 = product.attribute7.getOrElse(""),
      attribute8 = product.attribute8.getOrElse(""),
      attribute9 = product.attribute9.getOrElse(""),
      attribute10 = product.attribute10.getOrElse(""),
      attribute11 = product.attribute11.getOrElse(""),
      attribute12 = product.attribute12.getOrElse(""),
      attribute13 = product.attribute13.getOrElse(""),
      attribute14 = product.attribute14.getOrElse(""),
      attribute15 = product.attribute15.getOrElse(""),
      availability = product.availability.getOrElse(0),
      barCode = product.barCode.getOrElse(""),
      brand = product.brand.getOrElse(""),
      category = product.category.getOrElse(""),
      city = product.city.getOrElse(""),
      condition = product.condition.getOrElse(""),
      country = product.country.getOrElse(""),
      establishment = product.establishment.getOrElse(""),
      establishmentLink = product.establishmentLink.getOrElse(""),
      finalDate = product.finalDate.getOrElse(""),
      image = product.image.getOrElse(""),
      initDate = product.initDate.getOrElse(""),
      installment = product.installment.getOrElse(0),
      installmentDesc = product.installmentDesc.getOrElse(""),
      installmentValue = product.installmentValue.getOrElse(0),
      otherOffersLink = product.otherOffersLink.getOrElse(""),
      productId = product.productId.getOrElse(""),
      price = product.price.getOrElse(""),
      priceBy = product.priceBy.getOrElse(""),
      priceNum = product.priceNum.getOrElse(0),
      priceByNum = product.priceByNum.getOrElse(0),
      productDesc = product.productName.getOrElse(""), //Hack .: productDesc
      productGroupId = product.productGroupId.getOrElse(""),
      productLink = product.productLink.getOrElse(""),
      productName = product.productName.getOrElse(""),
      quantity = product.quantity.getOrElse(0),
      state = product.state.getOrElse(""),
      subCategory = product.subCategory.getOrElse(""),
      subCategoryTwo = product.subCategoryTwo.getOrElse(""),
      totalQtySold = product.totalQtySold.getOrElse(0)
    )
  }

}