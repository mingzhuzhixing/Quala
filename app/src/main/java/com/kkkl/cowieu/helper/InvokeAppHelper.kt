package com.kkkl.cowieu.helper

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.gson.JsonObject
import com.kkkl.cowieu.bean.QualaEventBean
import com.kkkl.cowieu.bean.QualaListBean
import com.kkkl.cowieu.event.InvokeAppStartTimeEvent
import com.kkkl.cowieu.event.RefreshCardDataEvent
import com.kkkl.cowieu.event.RefreshListEvent
import com.kkkl.cowieu.quala_api.QualaApi
import com.kkkl.cowieu.util.LogUtils
import com.kkkl.cowieu.util.SPUtils
import com.quala.network.http.HttpObserver
import com.quala.network.http.HttpRequest
import com.quala.network.http.HttpResponse
import com.quala.network.http.HttpRetrofit
import com.quala.network.http.HttpSchedulers
import org.greenrobot.eventbus.EventBus

/**
 * ClassName: InvokeAppHelper
 * Description: invokeApp帮助类
 *
 * @author jiaxiaochen
 * @package_name com.kkkl.cowieu.helper
 * @date 2023/12/20 08:45
 */
object InvokeAppHelper {
    /**
     * 开始invokeApp
     */
    fun invokeAppOpen(activity: Activity?, QualaListBean: QualaListBean?) {
        //addtocartpv 上报
        ReportEventHelper.eventReport(QualaConstants.ADDTOCARTPV)
        if (!SPUtils.addToCartLt) {
            //addtocartlt 上报
            ReportEventHelper.eventReport(QualaConstants.ADDTOCARTLT)
            SPUtils.addToCartLt = true
        }

        //接口上报
        specialEventReport(QualaListBean)
        val path = getStartIntentUrl(QualaListBean)
        try {
            if (activity == null) {
                return
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse(path)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
            LogUtils.d("invokeApp whatsapp：$intent")
            setInvokeAppCount()
            setFirstInvokeAppTime()

            //addtocart_ws 上报
            ReportEventHelper.eventReport(QualaConstants.ADDTOCART_WS)
        } catch (e: Exception) {
            startIntentUrl(activity, path)
        }
    }

    /**
     * 特殊事件上报
     */
    private fun specialEventReport(qualaListBean: QualaListBean?) {
        try {
            var id = 0
            if (qualaListBean?.contacts?.isNotEmpty() == true) {
                id = qualaListBean.contacts!![0].id
            }
            LogUtils.d("接口上报reportEvent id:$id")
            networkQualaReport(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 网络接口上报
     */
    private fun networkQualaReport(id: Int) {
        if (SPUtils.userEvent) {
            return
        }
        val json = JsonObject()
        json.addProperty(QualaConstants.KEY_ATTRIBUTES, SPUtils.getAdjustResult())
        json.addProperty(QualaConstants.KEY_GAID, SPUtils.getGaid())
        json.addProperty("action", "contact_lva")
        json.addProperty("id", id)
        HttpRetrofit.getInstance().create(QualaApi::class.java)
            .getQualaEvent(HttpRequest.getRequestBody(json))
            .compose(HttpSchedulers.applySchedulers<HttpResponse<QualaEventBean?>?>())
            .subscribe(object : HttpObserver<QualaEventBean?>() {
                override fun onSuccess(data: QualaEventBean?) {
                    LogUtils.i("onSuccess EventBean:$data")
                    SPUtils.userEvent = true
                    if (data != null && data.refresh) {
                        EventBus.getDefault().post(RefreshListEvent())
                    }
                }
            })
    }

    /**
     * 获取url
     */
    private fun getStartIntentUrl(qualaListBean: QualaListBean?): String {
        try {
            if (qualaListBean?.contacts?.isNotEmpty() == true) {
                val bean = qualaListBean.contacts!![0] ?: return ""
                return bean.url + "?text=" + bean.text
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 开始打开url
     */
    private fun startIntentUrl(activity: Activity?, url: String) {
        try {
            LogUtils.d("openUrl url:$url")
            if (activity == null) {
                return
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置invokeApp的次数
     */
    private fun setInvokeAppCount() {
        var count: Int = SPUtils.getInvokeAppSuccessCount()
        LogUtils.i("setInvokeAppCount: $count")
        val configEntity = ParseDataHelper.getConfigJsonData()
        val limitClick = if (configEntity?.contacts?.limits != null) {
            configEntity.contacts?.limits?.click ?: 0
        } else {
            0
        }
        if (limitClick <= 0) {
            return
        }
        if (count < limitClick) {
            count++
        } else {
            EventBus.getDefault().post(RefreshCardDataEvent())
        }
        SPUtils.setInvokeAppSuccessCount(count)
    }

    /**
     * 记录首次启动invokeApp 时间
     */
    private fun setFirstInvokeAppTime() {
        val time: Long = SPUtils.getFirstInvokeAppSuccessTime()
        LogUtils.w("setFirstInvokeAppTime 记录首次启动invokeApp 时间:$time")
        if (time > 0) {
            return
        }
        val configBean = ParseDataHelper.getConfigJsonData()
        val limitHour = if (configBean?.contacts?.limits != null) {
            configBean.contacts?.limits?.hour2 ?: 0
        } else {
            0
        }
        if (limitHour <= 0) {
            return
        }
        LogUtils.w("记录首次启动invokeApp 时间:$limitHour")
        val l = System.currentTimeMillis() + limitHour * 60 * 60 * 1000L
        SPUtils.setFirstInvokeAppSuccessTime(l)
        EventBus.getDefault().post(InvokeAppStartTimeEvent())
    }
}