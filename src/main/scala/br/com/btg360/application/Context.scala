package br.com.btg360.application

import br.com.btg360.entities.AccountEntity

@SerialVersionUID(1L)
class Context(_accountEntity: AccountEntity) extends Application {
  var accountEntity: AccountEntity = _accountEntity
}
