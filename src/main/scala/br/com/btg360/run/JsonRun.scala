package br.com.btg360.run

import br.com.btg360.entities.RuleDataEntity
import br.com.btg360.services.JsonService

//case class Children(name: String, age: Int, birthdate: Option[java.util.Date])
//case class Address(street: String, city: String)
//case class Person(name: String, address: Address, children: List[Children])

object JsonRun extends App {


  val jsonString =
    """
      {
   "configs": {
     "ruleName": "Navegação do usuário - semanal",
     "subject": "{{references.nome}} olha o que eu separei pra você!",
     "list": 949245,
     "senderName": "magazineluiza.com",
     "senderEmail": "emailcenter@novidades.magazineluiza.com.br",
     "maxProducts": 6,
     "hour": "10:00:00",
     "dayWeek": "4",
     "replyEmail": "promo@magazineluiza.com.br"
   },
   "module": {
     "selected": {
       "id": 2,
       "name": "most-purchased",
       "alias": "Mais vendidos",
       "desc": "Recomende produtos mais vendidos da sua loja, independente da categoria navegada por seu cliente",
       "icon": "flaticon-first-prize-medal"
     },
     "quantity": 12,
     "percentMin": 30,
     "percentMax": 70
   },
   "html": {
     "template": 30,
     "theme": 131,
     "layout": 1,
     "content": ""
   },
   "channels": {
     "email": {
       "status": true,
       "utms": [
         {
           "key": "partner_id",
           "value": "14061"
         },
         {
           "key": "utm_medium",
           "value": "email"
         },
         {
           "key": "utm_campaign",
           "value": "14061"
         },
         {
           "key": "utm_source",
           "value": "BTG"
         }
       ]
     },
     "facebook": {
       "status": true,
       "utms": []
     },
     "push": {
       "status": false,
       "utms": []
     }
   },
   "rule": {
     "ruleId": 2,
     "typeId": 1,
     "typeName": "default",
     "typeAlias": "Padrões",
     "name": "Navegação Semanal",
     "alias": "navigation-weekly",
     "peal": 1,
     "module": 1,
     "description": "Envia mensagens para clientes que visitaram determinadas páginas de produto do seu e-commerce, no entanto, não realizaram nenhuma compra",
     "minDescription": "Analise as páginas mais visitadas pelo seu cliente e ofereça os produtos\nNavegados ou similares",
     "techDescription": "Para ter essa regra funcionando, seu site precisa informar as tags: cliente, produto e pedido finalizado.",
     "period": 2,
     "icon": "flaticon-navigation-week",
     "priority": 1,
     "groupId": 1
   },
   "account": {
     "id": 14,
     "btgId": 353,
     "allinId": 7018,
     "transId": 5884,
     "token": "8ada660e06b787f245667943dc948dafab997e25"
   }
 }
       """

  val data = new JsonService().decode[RuleDataEntity](jsonString)
  //val data = new JsonService().decode[List[String]]("""["email", "facebook"]""")


//  for(i <- 0 to 5) {
//    println(data.channels)
//  }


}
