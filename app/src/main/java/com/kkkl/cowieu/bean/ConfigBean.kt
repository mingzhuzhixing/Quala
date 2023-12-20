package com.kkkl.cowieu.bean
/**
 * ClassName: ConfigBean
 * Description:
 *
 * @author jxc
 * @package_name  com.kkkl.cowieu.bean
 * @date 2023/12/19 10:37
 */
class ConfigBean {
    var contact: ContactBean? = null
    var actions: ActionsBean? = null
    var chatScript: ChatScriptBean? = null
    var report: ReportBean? = null
    var tips: TipsBean? = null
    var apk: ApkBean? = null
    var contacts: ContactsBean? = null
    var rtl = false

    class ContactBean {
        var contents: List<String>? = null
    }

    class ActionsBean {
        var apply: String? = null
    }

    class ChatScriptBean {
        var actions: ActionsBean? = null

        class ActionsBean {
            var contact: String? = null
        }
    }

    class ReportBean {
        var adjust: AdjustBean? = null
    }

    class AdjustBean {
        var addtocart_line: AddtocartLineBean? = null
        var addtocart_ws: AddtocartWsBean? = null
        var contact_show_popup: ContactShowPopupBean? = null
        var chat_show_dialog: ChatShowDialogBean? = null
        var app_start_app: AppStartAppBean? = null
        var addtocart_msg: AddtocartMsgBean? = null
        var ip_repeat_24h: IpRepeat24hBean? = null
        var addtocart_tg: AddtocartTgBean? = null
        var addtocart_zalo: AddtocartZaloBean? = null
        var addtocartpv: AddtocartpvBean? = null
        var addtocartlt: AddtocartltBean? = null
        var guide_lva_app: GuideLvaAppBean? = null
        var jobs_show_card: JobsShowCardBean? = null
        var jobs_show_ptjob: JobsShowPtjobBean? = null

        class AddtocartLineBean {
            var code: String? = null
        }

        class AddtocartWsBean {
            var code: String? = null
        }

        class ContactShowPopupBean {
            var code: String? = null
        }

        class ChatShowDialogBean {
            var code: String? = null
        }

        class AppStartAppBean {
            var code: String? = null
        }

        class AddtocartMsgBean {
            var code: String? = null
        }

        class IpRepeat24hBean {
            var code: String? = null
        }

        class AddtocartTgBean {
            var code: String? = null
        }

        class AddtocartZaloBean {
            var code: String? = null
        }

        class AddtocartpvBean {
            var code: String? = null
        }

        class AddtocartltBean {
            var code: String? = null
        }

        class GuideLvaAppBean {
            var code: String? = null
        }

        class JobsShowCardBean {
            var code: String? = null
        }

        class JobsShowPtjobBean {
            var code: String? = null
        }
    }

    class TipsBean {
        var contents: List<String>? = null
        var confirm: ConfirmBean? = null

        class ConfirmBean {
            var install: String? = null
        }
    }

    class ApkBean {
        var attributes = false
    }

    class ContactsBean {
        var showContact = false
        var limits: LimitsBean? = null

        class LimitsBean {
            var click = 0
            var hour2 = 0
            var hour = 0
        }
    }
}