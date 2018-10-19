package br.com.btg360.run

import br.com.btg360.services.JsonService

//case class Children(name: String, age: Int, birthdate: Option[java.util.Date])
//case class Address(street: String, city: String)
//case class Person(name: String, address: Address, children: List[Children])


import br.com.btg360.entities.RuleConfigurationEntity


object JsonRun extends App {


  val jsonString =
    """
                     {
                       "configs": {
                         "ruleName": "Abandono de carrinho",
                         "subject": "Sua viagem está esperando você!",
                         "list": 956680,
                         "senderName": "Hotel Urbano Recomenda",
                         "senderEmail": "contato@hotelurbano.com.br",
                         "hour": "16:00:00",
                         "maxProducts": 1
                       },
                       "module": {
                         "selected": {
                           "id": "5",
                           "name": "visited-by-users",
                           "alias": "Quem viu, viu também",
                           "desc": "DESC QUEM VIU, VIU TAMBÉM - lorem ipsum dolor sit amet, consectetur adipisicing elit. Quas, dicta, alias accusantium est unde autem ipsam libero tempora tempore nesciunt eveniet eaque laudantium qui facilis aut deserunt sequi suscipit perspiciatis.",
                           "icon": "flaticon-eye"
                         },
                         "quantity": "6",
                         "to": "30",
                         "from": "70",
                         "percentMin": "30",
                         "percentMax": "70"
                       },
                       "html": {
                         "template": 26,
                         "theme": 27,
                         "layout": 1,
                         "content": ""
                       },
                       "channels": {
                         "email": {
                           "status": true,
                           "utms": [
                             {
                               "key": "cmp",
                               "value": "1120"
                             },
                             {
                               "key": "utm_campaign",
                               "value": "abandonodecarrinho"
                             },
                             {
                               "key": "utm_content",
                               "value": "nome_produto"
                             },
                             {
                               "key": "utm_medium",
                               "value": "e-mkt"
                             },
                             {
                               "key": "utm_source",
                               "value": "allinhu_abandonodecarrinho"
                             },
                             {
                               "key": "utm_term",
                               "value": "ofertaBTGabandonoBeta"
                             }
                           ]
                         },
                         "facebook": {
                           "status": false,
                           "utms": []
                         },
                         "push": {
                           "status": false,
                           "utms": []
                         }
                       },
                       "rule": {
                         "ruleId": 14,
                         "typeId": 2,
                         "typeName": "hourly",
                         "typeAlias": "De hora em hora",
                         "name": "Abandono de carrinho",
                         "alias": "cart-abandonment",
                         "peal": 1,
                         "module": 1,
                         "icon": "flaticon-commerce",
                         "groupId": 12
                       },
                       "account": {
                         "id": 2,
                         "btgId": 9,
                         "allinId": 768,
                         "token": "2e6f9b0d5885b6010f9167787445617f553a735f"
                       }
                     }
       """

  val data = new JsonService().decode[RuleConfigurationEntity](jsonString)
  println(data.channels)


}
