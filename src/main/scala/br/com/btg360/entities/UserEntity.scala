package br.com.btg360.entities

import br.com.btg360.application.Entity

class UserEntity extends Entity {

  var id: Long = 0
  var name: String = _
  var email: String = _
  var username: String = _
  var password: String = _
  var domain: String = _
  var btgId: Int = 0
  var allinId: Int = 0
  var transId: Int = 0
  var remember_token: String = _
  var created_at: Any = _
  var updated_at: Any = _
  var homologation: Int = 0
  var homologation_at: Any = _
  var token: String = _
  var migrated: Int = 0
  var released: Int = 0
  var isMultiChannel: Int = 0

}
