package br.com.btg360.entities

import br.com.btg360.constants.Channel
import scala.collection.mutable.Map
import br.com.btg360.constants.{TypeConverter => TC}

object RuleDataRaw {

  case class Configs(
                      ruleName: Any,
                      subject: Any,
                      list: Any,
                      senderName: Any,
                      senderEmail: Any,
                      maxProducts: Any,
                      hour: Any,
                      dayWeek: Any,
                      dayMonth: Any,
                      replyEmail: Any,
                      emailResponsible: Any,
                      interval: Any,
                      frequency: Any
                    )

  case class ModuleSelected(
                             id: Any,
                             name: Any,
                             alias: Any,
                             desc: Any,
                             icon: Any
                           )

  case class Module(
                     selected: RuleDataRaw.ModuleSelected,
                     quantity: Any,
                     to: Any,
                     from: Any,
                     percentMin: Any,
                     percentMax: Any
                   )

  case class Html(
                   template: Any,
                   theme: Any,
                   layout: Any,
                   content: Any
                 )

  case class ChannelUtms(
                          key: Any,
                          value: Any
                        )

  case class Channel(
                      status: Any,
                      utms: List[RuleDataRaw.ChannelUtms],
                      subject: Any,
                      message: Any,
                      urlScheme: Any
                    )

  case class Channels(
                       email: RuleDataRaw.Channel,
                       sms: RuleDataRaw.Channel,
                       facebook: RuleDataRaw.Channel,
                       webpush: RuleDataRaw.Channel,
                       push_android: RuleDataRaw.Channel,
                       push_ios: RuleDataRaw.Channel
                     )

  case class Automatics(
                         list: Any,
                         exclusion: Any,
                         filter: Any,
                         field: Any,
                         format: Any
                       )

  case class Rule(
                   ruleId: Any,
                   typeId: Any,
                   typeName: Any,
                   typeAlias: Any,
                   name: Any,
                   alias: Any,
                   peal: Any,
                   module: Any,
                   description: Any,
                   minDescription: Any,
                   techDescription: Any,
                   period: Any,
                   icon: Any,
                   priority: Any,
                   groupId: Any
                 )

  case class Account(
                      id: Any,
                      btgId: Any,
                      allinId: Any,
                      transId: Any,
                      token: Any
                    )

}

/**
  * @param configs
  * @param module
  * @param html
  * @param channels
  * @param automatics
  * @param rule
  * @param account
  */
case class RuleDataEntity(
                           configs: RuleDataRaw.Configs,
                           module: RuleDataRaw.Module,
                           html: RuleDataRaw.Html,
                           channels: RuleDataRaw.Channels,
                           automatics: RuleDataRaw.Automatics,
                           rule: RuleDataRaw.Rule,
                           account: RuleDataRaw.Account
                         ) {

  var name = TC.toString(rule.alias)
  var latinName = TC.toString(configs.ruleName)
  var subject = TC.toString(configs.subject)
  var hour = TC.toString(configs.hour)
  var senderEmail = TC.toString(configs.senderEmail)
  var senderName = TC.toString(configs.senderName)
  var replyEmail = this.toReplyEmail()
  var referenceListId = TC.toInt(configs.list)
  var interval = TC.toInt(configs.interval)
  var frequency = TC.toInt(configs.frequency)
  var dayWeek = TC.toInt(configs.dayWeek)
  var dayMonth = TC.toInt(configs.dayMonth)

  //Automatics
  var listId = TC.toInt(automatics.list)
  var listExclusionId = TC.toInt(automatics.exclusion)
  var field = TC.toString(automatics.field)
  var formatField = TC.toString(automatics.format)
  var filterId = TC.toInt(automatics.filter)

  //HTML
  var templateId = TC.toInt(html.template)
  var themeId = TC.toInt(html.theme)
  var layoutId = TC.toInt(html.layout)
  var content = TC.toString(html.content)

  //Account
  var btgId = TC.toInt(account.btgId)
  var allinId = TC.toInt(account.allinId)
  var transactionalId = TC.toInt(account.transId)
  var token = TC.toString(account.token)

  //Module
  var moduleId: Int = TC.toInt(module.selected.id)
  var moduleName: String = TC.toString(module.selected.name)
  var moduleLimit: Int = TC.toInt(module.quantity)
  var modulePercentMin: Int = TC.toInt(module.percentMin)
  var modulePercentMax: Int = TC.toInt(module.percentMax)

  //Channel
  var channelMap: Map[String, RuleDataRaw.Channel] = toChannelMap()

  /**
    * @return String
    */
  private def toReplyEmail(): String = {
    var replyEmail = TC.toString(configs.replyEmail)
    if (replyEmail.isEmpty) {
      replyEmail = this.senderEmail
    }
    replyEmail
  }

  /**
    * @return Map
    */
  private def toChannelMap(): Map[String, RuleDataRaw.Channel] = {
    val channelMap: Map[String, RuleDataRaw.Channel] = Map()
    if (TC.toBoolean(channels.email.status)) channelMap(Channel.EMAIL) = channels.email
    if (TC.toBoolean(channels.sms.status)) channelMap(Channel.SMS) = channels.sms
    if (TC.toBoolean(channels.facebook.status)) channelMap(Channel.FACEBOOK) = channels.facebook
    if (TC.toBoolean(channels.webpush.status)) channelMap(Channel.WEBPUSH) = channels.webpush
    if (TC.toBoolean(channels.push_android.status)) channelMap(Channel.PUSH_ANDROID) = channels.push_android
    if (TC.toBoolean(channels.push_ios.status)) channelMap(Channel.PUSH_IOS) = channels.push_ios
    channelMap
  }

}

