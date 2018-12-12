package br.com.btg360.entities

import br.com.btg360.application.Entity

class ThemeEntity extends Entity {

  var id: Int = 0
  var userId: Int = 0
  var name: String = _
  var configs: String = _
  var start_at: Any = _
  var end_at: Any = _
  var default: Int = 0
  var status: Int = 0

}
