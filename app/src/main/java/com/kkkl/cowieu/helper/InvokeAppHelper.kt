package com.kkkl.cowieu.helper

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.gson.JsonObject
import com.kkkl.cowieu.bean.EventBean
import com.kkkl.cowieu.bean.ListBean
import com.kkkl.cowieu.event.RefreshCardDataEvent
import com.kkkl.cowieu.event.RefreshListEvent
import com.kkkl.cowieu.quala_api.QualaApi
import com.kkkl.cowieu.util.LogUtils
import com.kkkl.cowieu.util.SPUtils
import com.quala.network.http.HttpObserver
import com.quala.network.http.HttpResponse
import com.quala.network.http.HttpRetrofit
import com.quala.network.http.HttpSchedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
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
    fun invokeAppStart(activity: Activity?, ListBean: ListBean?) {
        //addtocartpv 上报
        ReportEventHelper.eventReport(Constants.ADDTOCARTPV)
        if (!SPUtils.addToCartLt) {
            //addtocartlt 上报
            ReportEventHelper.eventReport(Constants.ADDTOCARTLT)
            SPUtils.addToCartLt = true
        }

        //接口上报
        specialEventReport(ListBean)
        val path = getStartIntentUrl(ListBean)
        try {
            if (activity == null) {
                return
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse(path)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
            LogUtils.d("jxc", "invokeApp whatsapp：$intent")
            setInvokeAppCount()
            setFirstInvokeAppTime()

            //addtocart_ws 上报
            ReportEventHelper.eventReport(Constants.ADDTOCART_WS)
        } catch (e: Exception) {
            startIntentUrl(activity, path)
        }
    }

    /**
     * 特殊事件上报
     */
    private fun specialEventReport(listEntity: ListBean?) {
        try {
            var id = 0
            if (listEntity?.contacts?.isNotEmpty() == true) {
                id = listEntity.contacts!![0].id
            }
            LogUtils.d("jxc", "接口上报reportEvent id:$id")
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
        json.addProperty("attributes", SPUtils.getAdjustResult())
        json.addProperty("gaid", SPUtils.getGaid())
        json.addProperty("action", "contact_lva")
        json.addProperty("id", id)
        val body = RequestBody.create(("application/json").toMediaTypeOrNull(), json.toString())
        HttpRetrofit.getInstance().create(QualaApi::class.java)
            .getQualaEvent(body)
            .compose(HttpSchedulers.applySchedulers<HttpResponse<EventBean?>?>())
            .subscribe(object : HttpObserver<EventBean?>() {
                override fun onSuccess(data: EventBean?) {
                    LogUtils.i("jxc", "onSuccess EventBean:$data")
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
    private fun getStartIntentUrl(listEntity: ListBean?): String {
        try {
            if (listEntity?.contacts?.isNotEmpty() == true) {
                val bean = listEntity.contacts!![0] ?: return ""
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
            LogUtils.d("jxc", "openUrl url:$url")
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
        LogUtils.i("jxc", "setInvokeAppCount: $count")
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
        LogUtils.w("jxc", "setFirstInvokeAppTime 记录首次启动invokeApp 时间:$time")
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
        LogUtils.w("jxc", "记录首次启动invokeApp 时间:$limitHour")
        val l = System.currentTimeMillis() + limitHour * 60 * 60 * 1000L
        SPUtils.setFirstInvokeAppSuccessTime(l)
        //todo
        //EventBus.getDefault().post(InvokeAppStartTimeEvent())
    }
}