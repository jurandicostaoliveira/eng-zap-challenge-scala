package br.com.btg360.entities

object RawRuleObject {

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
                     selected: RawRuleObject.ModuleSelected,
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
                      utms: List[RawRuleObject.ChannelUtms],
                      subject: Any,
                      message: Any,
                      urlScheme: Any
                    )

  case class Channels(
                       email: RawRuleObject.Channel,
                       sms: RawRuleObject.Channel,
                       facebook: RawRuleObject.Channel,
                       webpush: RawRuleObject.Channel,
                       push_android: RawRuleObject.Channel,
                       push_ios: RawRuleObject.Channel
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
case class RawRuleEntity(
                          configs: RawRuleObject.Configs,
                          module: RawRuleObject.Module,
                          html: RawRuleObject.Html,
                          channels: RawRuleObject.Channels,
                          automatics: RawRuleObject.Automatics,
                          rule: RawRuleObject.Rule,
                          account: RawRuleObject.Account
                        )
