package br.com.btg360.entities

class ProductEntity extends Serializable {

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

  /*
    private var _attribute1: String = _
    private var _attribute2: String = _
    private var _attribute3: String = _
    private var _attribute4: String = _
    private var _attribute5: String = _
    private var _attribute6: String = _
    private var _attribute7: String = _
    private var _attribute8: String = _
    private var _attribute9: String = _
    private var _attribute10: String = _
    private var _attribute11: String = _
    private var _attribute12: String = _
    private var _attribute13: String = _
    private var _attribute14: String = _
    private var _attribute15: String = _
    private var _availability: Int = 0
    private var _barCode: String = _
    private var _brand: String = _
    private var _category: String = _
    private var _city: String = _
    private var _condition: String = _
    private var _country: String = _
    private var _establishment: String = _
    private var _establishmentLink: String = _
    private var _finalDate: String = _
    private var _image: String = _
    private var _initDate: String = _
    private var _installment: Int = 0
    private var _installmentDesc: String = _
    private var _installmentValue: Float = 0
    private var _otherOffersLink: String = _
    private var _productId: String = _
    private var _price: String = _
    private var _priceBy: String = _
    private var _priceNum: Float = 0
    private var _priceByNum: Float = 0
    private var _productDesc: String = _
    private var _productGroupId: String = _
    private var _productLink: String = _
    private var _productName: String = _
    private var _quantity: Int = 0
    private var _state: String = _
    private var _subCategory: String = _
    private var _subCategoryTwo: String = _
    private var _totalQtySold: Int = _

    /**
      * Getter
      *
      * @return String
      */
    def attribute1: String = this._attribute1

    /**
      * Setter
      *
      * @param String value
      */
    def attribute1_=(value: String): Unit = {
      this._attribute1 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute2: String = this._attribute2

    /**
      * Setter
      *
      * @param String value
      */
    def attribute2_=(value: String): Unit = {
      this._attribute2 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute3: String = this._attribute3

    /**
      * Setter
      *
      * @param String value
      */
    def attribute3_=(value: String): Unit = {
      this._attribute3 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute4: String = this._attribute4

    /**
      * Setter
      *
      * @param String value
      */
    def attribute4_=(value: String): Unit = {
      this._attribute4 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute5: String = this._attribute5

    /**
      * Setter
      *
      * @param String value
      */
    def attribute5_=(value: String): Unit = {
      this._attribute5 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute6: String = this._attribute6

    /**
      * Setter
      *
      * @param String value
      */
    def attribute6_=(value: String): Unit = {
      this._attribute6 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute7: String = this._attribute7

    /**
      * Setter
      *
      * @param String value
      */
    def attribute7_=(value: String): Unit = {
      this._attribute7 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute8: String = this._attribute8

    /**
      * Setter
      *
      * @param String value
      */
    def attribute8_=(value: String): Unit = {
      this._attribute8 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute9: String = this._attribute9

    /**
      * Setter
      *
      * @param String value
      */
    def attribute9_=(value: String): Unit = {
      this._attribute9 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute10: String = this._attribute10

    /**
      * Setter
      *
      * @param String value
      */
    def attribute10_=(value: String): Unit = {
      this._attribute10 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute11: String = this._attribute11

    /**
      * Setter
      *
      * @param String value
      */
    def attribute11_=(value: String): Unit = {
      this._attribute11 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute12: String = this._attribute12

    /**
      * Setter
      *
      * @param String value
      */
    def attribute12_=(value: String): Unit = {
      this._attribute12 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute13: String = this._attribute13

    /**
      * Setter
      *
      * @param String value
      */
    def attribute13_=(value: String): Unit = {
      this._attribute13 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute14: String = this._attribute14

    /**
      * Setter
      *
      * @param String value
      */
    def attribute14_=(value: String): Unit = {
      this._attribute14 = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def attribute15: String = this._attribute15

    /**
      * Setter
      *
      * @param String value
      */
    def attribute15_=(value: String): Unit = {
      this._attribute15 = value
    }

    /**
      * Getter
      *
      * @return Int
      */
    def availability: Int = this._availability

    /**
      * Setter
      *
      * @param Int value
      */
    def availability_=(value: Int): Unit = {
      this._availability = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def barCode: String = this._barCode

    /**
      * Setter
      *
      * @param String value
      */
    def barCode_=(value: String): Unit = {
      this._barCode = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def brand: String = this._brand

    /**
      * Setter
      *
      * @param String value
      */
    def brand_=(value: String): Unit = {
      this._brand = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def category: String = this._category

    /**
      * Setter
      *
      * @param String value
      */
    def category_=(value: String): Unit = {
      this._category = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def city: String = this._city

    /**
      * Setter
      *
      * @param String value
      */
    def city_=(value: String): Unit = {
      this._city = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def condition: String = this._condition

    /**
      * Setter
      *
      * @param String value
      */
    def condition_=(value: String): Unit = {
      this._condition = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def country: String = this._country

    /**
      * Setter
      *
      * @param String value
      */
    def country_=(value: String): Unit = {
      this._country = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def establishment: String = this._establishment

    /**
      * Setter
      *
      * @param String value
      */
    def establishment_=(value: String): Unit = {
      this._establishment = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def establishmentLink: String = this._establishmentLink

    /**
      * Setter
      *
      * @param String value
      */
    def establishmentLink_=(value: String): Unit = {
      this._establishmentLink = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def finalDate: String = this._finalDate

    /**
      * Setter
      *
      * @param String value
      */
    def finalDate_=(value: String): Unit = {
      this._finalDate = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def image: String = this._image

    /**
      * Setter
      *
      * @param String value
      */
    def image_=(value: String): Unit = {
      this._image = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def initDate: String = this._initDate

    /**
      * Setter
      *
      * @param String value
      */
    def initDate_=(value: String): Unit = {
      this._initDate = value
    }

    /**
      * Getter
      *
      * @return Int
      */
    def installment: Int = this._installment

    /**
      * Setter
      *
      * @param Int value
      */
    def installment_=(value: Int): Unit = {
      this._installment = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def installmentDesc: String = this._installmentDesc

    /**
      * Setter
      *
      * @param String value
      */
    def installmentDesc_=(value: String): Unit = {
      this._installmentDesc = value
    }

    /**
      * Getter
      *
      * @return Float
      */
    def installmentValue: Float = this._installmentValue

    /**
      * Setter
      *
      * @param Float value
      */
    def installmentValue_=(value: Float): Unit = {
      this._installmentValue = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def otherOffersLink: String = this._otherOffersLink

    /**
      * Setter
      *
      * @param String value
      */
    def otherOffersLink_=(value: String): Unit = {
      this._otherOffersLink = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def productId: String = this._productId

    /**
      * Setter
      *
      * @param String value
      */
    def productId_=(value: String): Unit = {
      this._productId = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def price: String = this._price

    /**
      * Setter
      *
      * @param String value
      */
    def price_=(value: String): Unit = {
      this._price = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def priceBy: String = this._priceBy

    /**
      * Setter
      *
      * @param String value
      */
    def priceBy_=(value: String): Unit = {
      this._priceBy = value
    }

    /**
      * Getter
      *
      * @return Float
      */
    def priceByNum: Float = this._priceByNum

    /**
      * Setter
      *
      * @param Float value
      */
    def priceByNum_=(value: Float): Unit = {
      this._priceByNum = value
    }

    /**
      * Getter
      *
      * @return Float
      */
    def priceNum: Float = this._priceNum

    /**
      * Setter
      *
      * @param Float value
      */
    def priceNum_=(value: Float): Unit = {
      this._priceNum = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def productDesc: String = this._productDesc

    /**
      * Setter
      *
      * @param String value
      */
    def productDesc_=(value: String): Unit = {
      this._productDesc = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def productGroupId: String = this._productGroupId

    /**
      * Setter
      *
      * @param String value
      */
    def productGroupId_=(value: String): Unit = {
      this._productGroupId = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def productLink: String = this._productLink

    /**
      * Setter
      *
      * @param String value
      */
    def productLink_=(value: String): Unit = {
      this._productLink = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def productName: String = this._productName

    /**
      * Setter
      *
      * @param String value
      */
    def productName_=(value: String): Unit = {
      this._productName = value
    }

    /**
      * Getter
      *
      * @return Int
      */
    def quantity: Int = this._quantity

    /**
      * Setter
      *
      * @param Int value
      */
    def quantity_=(value: Int): Unit = {
      this._quantity = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def state: String = this._state

    /**
      * Setter
      *
      * @param String value
      */
    def state_=(value: String): Unit = {
      this._state = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def subCategory: String = this._subCategory

    /**
      * Setter
      *
      * @param String value
      */
    def subCategory_=(value: String): Unit = {
      this._subCategory = value
    }

    /**
      * Getter
      *
      * @return String
      */
    def subCategoryTwo: String = this._subCategoryTwo

    /**
      * Setter
      *
      * @param String value
      */
    def subCategoryTwo_=(value: String): Unit = {
      this._subCategoryTwo = value
    }

    /**
      * Getter
      *
      * @return Int
      */
    def totalQtySold: Int = this._totalQtySold

    /**
      * Setter
      *
      * @param Int value
      */
    def totalQtySold_=(value: Int): Unit = {
      this._totalQtySold = value
    }
  */
}
