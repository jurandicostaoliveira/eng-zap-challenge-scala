package br.com.btg360.entities

case class JsonProductEntity(
                              userId: String = "",
                              userSent: String = "",
                              isSent: Int = 0,
                              isRecommendation: Int = 0,
                              percentage: Double = 0,
                              platformId: Int = 0,
                              createdAt: String = "",
                              //
                              attribute1: String = "",
                              attribute2: String = "",
                              attribute3: String = "",
                              attribute4: String = "",
                              attribute5: String = "",
                              attribute6: String = "",
                              attribute7: String = "",
                              attribute8: String = "",
                              attribute9: String = "",
                              attribute10: String = "",
                              attribute11: String = "",
                              attribute12: String = "",
                              attribute13: String = "",
                              attribute14: String = "",
                              attribute15: String = "",
                              availability: Int = 0,
                              barCode: String = "",
                              brand: String = "",
                              category: String = "",
                              city: String = "",
                              condition: String = "",
                              country: String = "",
                              establishment: String = "",
                              establishmentLink: String = "",
                              finalDate: String = "",
                              image: String = "",
                              initDate: String = "",
                              installment: Int = 0,
                              installmentDesc: String = "",
                              installmentValue: Float = 0,
                              otherOffersLink: String = "",
                              price: String = "",
                              priceBy: String = "",
                              priceNum: Float = 0,
                              priceByNum: Float = 0,
                              productDesc: String = "", //Hack .: productDesc
                              productId: String = "",
                              productGroupId: String = "",
                              productLink: String = "",
                              productName: String = "",
                              quantity: Int = 0,
                              state: String = "",
                              subCategory: String = "",
                              subCategoryTwo: String = "",
                              totalQtySold: Int = 0
                            ) {}
