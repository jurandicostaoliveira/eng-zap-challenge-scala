package br.com.btg360.entities

object RuleConfigurationRaw {

  case class Configs(
                      ruleName: Any,
                      subject: Any,
                      list: Any,
                      senderName: Any,
                      senderEmail: Any,
                      hour: Any,
                      maxProducts: Any
                    )

  /**
    *
    */
  case class ModuleSelected(
                             id: Any,
                             name: Any,
                             alias: Any,
                             desc: Any,
                             icon: Any
                           )

  case class Module(
                     selected: RuleConfigurationRaw.ModuleSelected,
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
                      status: Boolean,
                      utms: List[ChannelUtms]
                    )

  case class Channels(
                       email: Channel,
                       facebook: Channel,
                       push: Channel

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
                   icon: Any,
                   groupId: Any
                 )

  case class Account(
                      id: Any,
                      btgId: Any,
                      allinId: Any,
                      token: Any
                    )

}


case class RuleConfigurationEntity(
                                    configs: RuleConfigurationRaw.Configs,
                                    module: RuleConfigurationRaw.Module,
                                    html: RuleConfigurationRaw.Html,
                                    channels: RuleConfigurationRaw.Channels,
                                    rule: RuleConfigurationRaw.Rule,
                                    account: RuleConfigurationRaw.Account
                                  )
