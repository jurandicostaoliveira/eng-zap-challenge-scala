package br.com.btg360.entities

import br.com.btg360.application.Entity

class RuleEntity extends Entity{

  var userRuleId: Int = 0
  var priority: Int = 0
  var ruleTypeId: Int = 0
  var periodId : Int = 0
  var groupId : Int = 0
  var ruleName: String = _
  var data: String = _

}
