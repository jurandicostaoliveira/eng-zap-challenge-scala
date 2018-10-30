package br.com.btg360.entities

import br.com.btg360.application.Entity
import br.com.btg360.services.{JsonService}

class RuleEntity extends Entity {

  private val jsonService = new JsonService()

  var id: Int = 0
  var priority: Int = 0
  var typeId: Int = 0
  var periodId: Int = 0
  var groupId: Int = 0
  var dataStringJson: String = _
  var data: RuleDataEntity = _

  def parse: RuleEntity = {
    if (this.data == null) {
      this.data = this.jsonService.decode[RuleDataEntity](this.dataStringJson)
    }

    this
  }

}
