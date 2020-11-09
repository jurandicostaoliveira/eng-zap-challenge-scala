package br.com.btg360.run

import br.com.btg360.entities.QueueEntity
import br.com.btg360.repositories.SocialMinerRepository
import br.com.btg360.services.UrlService

object SmidRun extends App {

  val json = "{\"configs\":{\"ruleName\":\"Navegou no Produto 4 Dias\",\"subject\":\"&#x1F4E2; AVISO IMPORTANTE\",\"list\":1281722,\"senderName\":\"Ricardo Eletro\",\"senderEmail\":\"email@ricardoeletro.com.br\",\"maxProducts\":3,\"hour\":\"11:00:00\",\"interval\":4,\"replyEmail\":\"postmaster@ricardoeletro.com.br\"},\"module\":{\"selected\":{\"id\":2,\"name\":\"most-purchased\",\"alias\":\"Mais vendidos\",\"desc\":\"Recomende produtos mais vendidos da sua loja, independente da categoria navegada por seu cliente\",\"icon\":\"flaticon-first-prize-medal\"},\"quantity\":9,\"percentMin\":30,\"percentMax\":70},\"html\":{\"template\":472,\"theme\":76,\"layout\":1,\"content\":\"\"},\"channels\":{\"email\":{\"status\":true,\"utms\":[{\"key\":\"utm_source\",\"value\":\"BTG360\"},{\"key\":\"utm_medium\",\"value\":\"Email\"},{\"key\":\"prc\",\"value\":\"24846\"},{\"key\":\"utm_campaign\",\"value\":\"news_navegacao_diaria_4_dias\"}]},\"facebook\":{\"status\":false,\"utms\":[]},\"push\":{\"status\":false,\"utms\":[]},\"push_ios\":{\"status\":false,\"utms\":[{\"key\":\"utm_source\",\"value\":\"BTG360\"},{\"key\":\"utm_medium\",\"value\":\"push_ios\"}]},\"push_android\":{\"status\":false,\"utms\":[{\"key\":\"utm_source\",\"value\":\"BTG360\"},{\"key\":\"utm_medium\",\"value\":\"push_android\"}]},\"token\":{\"status\":false,\"utms\":[]}},\"rule\":{\"ruleId\":1,\"typeId\":2,\"typeName\":\"hourly\",\"typeAlias\":\"De hora em hora\",\"name\":\"Navega\\u00e7\\u00e3o\",\"alias\":\"navigation-daily\",\"peal\":1,\"module\":1,\"description\":\"Envia mensagens para clientes que visitaram determinadas p\\u00e1ginas de produto do seu e-commerce, no entanto, n\\u00e3o realizaram nenhuma compra\",\"minDescription\":\"Analise as p\\u00e1ginas mais visitadas pelo seu cliente e ofere\\u00e7a os produtos\\nNavegados ou similares\",\"techDescription\":\"Para ter essa regra funcionando, seu site precisa informar as tags: cliente, produto e pedido finalizado.\",\"period\":1,\"icon\":\"flaticon-navigation-day-1-2\",\"priority\":0,\"groupId\":1,\"pealFrom\":154},\"account\":{\"id\":9,\"btgId\":15,\"allinId\":109,\"transId\":375,\"token\":\"f1abd670358e036c31296e66b3b66c382ac00812\"}}"

  val entity = new QueueEntity()
  entity.dataStringJson = json
  entity.isSmid = true
  entity.userRuleId = 999999
  val queue = entity.parse


  println(">>>> " + new SocialMinerRepository().allinCrossExists(queue.rule.allinId))

  println("LINK = " + queue.utmLink)


  //(BTGXXXXX)-(Canal)-(Tipo da Regra)
  //select count(id_login) AS total from emailpro_mailsender.cor_login_sm where id_login = 109;

}


