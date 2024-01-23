package com.kkkl.cowieu.helper

import android.text.TextUtils
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.kkkl.cowieu.bean.QualaListBean
import com.kkkl.cowieu.helper.ParseDataHelper.getConfigJsonData
import com.kkkl.cowieu.util.QualaLogUtils
import com.kkkl.cowieu.util.SPreferenceUtils

/**
 * ClassName: ReportEventHelper
 * Description: 归因事件上报
 *
 * @author zhaowei
 * @package_name com.kkkl.cowieu.report
 * @date 2023/12/20 08:45
 */
object ReportEventHelper {
    /**
     * 事件上报
     */
    fun eventReport(key: String) {
        if (TextUtils.isEmpty(key)) {
            return
        }
        val code = getAdjustReportCode(key)
        eventReport(key, code)
    }

    /**
     * 事件上报 两个参数
     */
    private fun eventReport(key: String, code: String?) {
        QualaLogUtils.e("上报key：$key code：$code")
        if (TextUtils.isEmpty(code)) {
            return
        }
        val adjustEvent = AdjustEvent(code)
        QualaLogUtils.e("上报 trackEvent：" + adjustEvent.eventToken)
        Adjust.trackEvent(adjustEvent)
    }

    /**
     * 获取归因 code
     *
     * @param key
     * @return 获取归因值
     */
    private fun getAdjustReportCode(key: String): String {
        val adjustBean = getConfigJsonData()?.report?.adjust
        if (TextUtils.isEmpty(key) || adjustBean == null) {
            return ""
        }
        if (QualaConstants.APP_SHOW_APP == key) {
            return adjustBean.app_show_app?.code ?: ""
        } else if (QualaConstants.ADDTOCARTLT == key) {
            return adjustBean.addtocartlt?.code ?: ""
        } else if (QualaConstants.ADDTOCARTPV == key) {
            return adjustBean.addtocartpv?.code ?: ""
        } else if (QualaConstants.ADDTOCART_WS == key) {
            return adjustBean.addtocart_ws?.code ?: ""
        } else if (QualaConstants.JOBS_SHOW_CARD == key) {
            return adjustBean.jobs_show_card?.code ?: ""
        } else if (QualaConstants.JOBS_SHOW_PTJOB == key) {
            return adjustBean.jobs_show_ptjob?.code ?: ""
        } else if (QualaConstants.CONTACT_SHOW_POPUP == key) {
            return adjustBean.contact_show_popup?.code ?: ""
        }
        return ""
    }

    /**
     * 上班job显示事件
     */
    fun reportJobsShow(qualaListBeans: List<QualaListBean?>) {
        //上报
        if (qualaListBeans.isNotEmpty()) {
            eventReport(QualaConstants.JOBS_SHOW_CARD)
            if (!SPreferenceUtils.getJobsShowPtJob()) {
                for (bean in qualaListBeans) {
                    if ("c" == bean?.type) {
                        eventReport(QualaConstants.JOBS_SHOW_PTJOB)
                        SPreferenceUtils.setJobsShowPtJob(true)
                        break
                    }
                }
            }
        }
    }

    private const val APP_SHOW_APP_CODE = "7olgzc"

    /**
     * 上报 showApp
     */
    fun reportShowApp() {
        val bean = getConfigJsonData()
        val adjust = bean?.report?.adjust
        if (adjust?.app_show_app != null) {
            eventReport(QualaConstants.APP_SHOW_APP, adjust.app_show_app?.code)
        } else {
            eventReport(QualaConstants.APP_SHOW_APP, APP_SHOW_APP_CODE)
        }
    }
}