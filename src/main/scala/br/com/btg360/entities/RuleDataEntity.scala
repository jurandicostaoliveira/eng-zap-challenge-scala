package br.com.btg360.entities

import br.com.btg360.constants.Channel
import br.com.btg360.services.{TypeConverterService => TCS}
import scala.collection.mutable.Map

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

  var name = TCS.toString(rule.alias)
  var latinName = TCS.toString(configs.ruleName)
  var subject = TCS.toString(configs.subject)
  var hour = TCS.toString(configs.hour)
  var senderEmail = TCS.toString(configs.senderEmail)
  var senderName = TCS.toString(configs.senderName)
  var replyEmail = this.toReplyEmail()
  var referenceListId = TCS.toInt(configs.list)
  var interval = TCS.toInt(configs.interval)
  var frequency = TCS.toInt(configs.frequency)
  var dayWeek = TCS.toInt(configs.dayWeek)
  var dayMonth = TCS.toInt(configs.dayMonth)

  //Automatics
  var listId = TCS.toInt(automatics.list)
  var listExclusionId = TCS.toInt(automatics.exclusion)
  var field = TCS.toString(automatics.field)
  var formatField = TCS.toString(automatics.format)
  var filterId = TCS.toInt(automatics.filter)

  //HTML
  var templateId = TCS.toInt(html.template)
  var themeId = TCS.toInt(html.theme)
  var layoutId = TCS.toInt(html.layout)
  var content = TCS.toString(html.content)

  //Account
  var btgId = TCS.toInt(account.btgId)
  var allinId = TCS.toInt(account.allinId)
  var transactionalId = TCS.toInt(account.transId)
  var token = TCS.toString(account.token)

  //Channel
  var channelMap: Map[String, RuleDataRaw.Channel] = toChannelMap()

  /**
    * @return String
    */
  private def toReplyEmail(): String = {
    var replyEmail = TCS.toString(configs.replyEmail)
    if (replyEmail.isEmpty) {
      replyEmail = this.senderEmail
    }
    replyEmail
  }

  /**
    *
    * @return
    */
  private def toChannelMap(): Map[String, RuleDataRaw.Channel] = {
    val channelMap: Map[String, RuleDataRaw.Channel] = Map()
    if (TCS.toBoolean(channels.email.status)) channelMap(Channel.EMAIL) = channels.email
    if (TCS.toBoolean(channels.sms.status)) channelMap(Channel.SMS) = channels.sms
    if (TCS.toBoolean(channels.facebook.status)) channelMap(Channel.FACEBOOK) = channels.facebook
    if (TCS.toBoolean(channels.webpush.status)) channelMap(Channel.WEBPUSH) = channels.webpush
    if (TCS.toBoolean(channels.push_android.status)) channelMap(Channel.PUSH_ANDROID) = channels.push_android
    if (TCS.toBoolean(channels.push_ios.status)) channelMap(Channel.PUSH_IOS) = channels.push_ios

    channelMap
  }

}

