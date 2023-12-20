package com.kkkl.cowieu.helper

import android.content.Context
import android.text.TextUtils
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.kkkl.cowieu.bean.ListBean
import com.kkkl.cowieu.helper.ParseDataHelper.getConfigJsonData
import com.kkkl.cowieu.util.LogUtils
import com.kkkl.cowieu.util.SPUtils

/**
 * ClassName: ReportEventHelper
 * Description: 归因事件上报
 *
 * @author jiaxiaochen
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
    fun eventReport(key: String, code: String?) {
        LogUtils.e("jxc", "上报key：$key code：$code")
        if (TextUtils.isEmpty(code)) {
            return
        }
        val adjustEvent = AdjustEvent(code)
        LogUtils.e("jxc", "上报 trackEvent：" + adjustEvent.eventToken)
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
        if (Constants.APP_SHOW_APP == key) {
            return adjustBean.app_show_app?.code ?: ""
        } else if (Constants.ADDTOCARTLT == key) {
            return adjustBean.addtocartlt?.code ?: ""
        } else if (Constants.ADDTOCARTPV == key) {
            return adjustBean.addtocartpv?.code ?: ""
        } else if (Constants.ADDTOCART_WS == key) {
            return adjustBean.addtocart_ws?.code ?: ""
        } else if (Constants.JOBS_SHOW_CARD == key) {
            return adjustBean.jobs_show_card?.code ?: ""
        } else if (Constants.JOBS_SHOW_PTJOB == key) {
            return adjustBean.jobs_show_ptjob?.code ?: ""
        } else if (Constants.CONTACT_SHOW_POPUP == key) {
            return adjustBean.contact_show_popup?.code ?: ""
        }
        return ""
    }

    /**
     * 上班job显示事件
     */
    fun reportJobsShow(listBeans: List<ListBean?>) {
        //上报
        if (listBeans.isNotEmpty()) {
            eventReport(Constants.JOBS_SHOW_CARD)
            if (!SPUtils.getJobsShowPtJob()) {
                for (bean in listBeans) {
                    if ("c" == bean?.type) {
                        eventReport(Constants.JOBS_SHOW_PTJOB)
                        SPUtils.setJobsShowPtJob(true)
                        break
                    }
                }
            }
        }
    }

    private const val APP_SHOW_APP_CODE = "zfh3rg"

    /**
     * 上报 showApp
     */
    fun reportShowApp(context: Context?) {
        val bean = getConfigJsonData()
        val adjust = bean?.report?.adjust
        if (adjust?.app_show_app != null) {
            eventReport(Constants.APP_SHOW_APP, adjust.app_show_app?.code)
            LogUtils.v("jxc", "startMainActivity() 开启主页面 上报app_start_app 获取的")
        } else {
            LogUtils.v("jxc", "startMainActivity() 开启主页面 上报app_start_app 写死的")
            eventReport(Constants.APP_SHOW_APP, APP_SHOW_APP_CODE)
        }
    }
}