package com.kkkl.cowieu.helper

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.BuildConfig
import com.adjust.sdk.LogLevel
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.kkkl.cowieu.event.LoadAdjustEvent
import com.kkkl.cowieu.util.LogUtils
import com.kkkl.cowieu.util.SPUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

/**
 * ClassName: HomeActivity
 * Description: 初始化管理类
 *
 * @author jxc
 * @package_name  com.kkkl.cowieu.helper
 * @date 2023/12/19 10:45
 */
class InitHelper {
    //归因设备 token值
    private val APP_TOKEN = "ebym7gif7474"

    companion object {
        val INSTANCE = Holder.holder
    }

    private object Holder {
        val holder = InitHelper()
    }

    private var mApplicationContext: Context? = null

    fun initContext(context: Application) {
        mApplicationContext = context
        initData(context)
    }

    fun getApplicationContext(): Context? {
        return if (mApplicationContext == null) {
            throw RuntimeException("mApplicationContext == null")
        } else {
            mApplicationContext
        }
    }

    /**
     * 初始化数据
     */
    @SuppressLint("CheckResult")
    private fun initData(context: Context) {
        Observable.zip<String, String, String>(getGaid(context), getAdjustCode(context)) { s, s2 ->
            LogUtils.i("jxc", "apply>> s:$s s2:$s2")
            "success"
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o ->
                LogUtils.i("jxc", "最终设备id>>$o")
                EventBus.getDefault().post(LoadAdjustEvent(1))
            }
    }

    /**
     * 获取设备的 id
     */
    private fun getGaid(context: Context): Observable<String> {
        return Observable.create { emitter ->
            val result: String = SPUtils.getGaid() ?: ""
            if (!TextUtils.isEmpty(result)) {
                emitter.onNext(result)
            }
            Thread {
                try {
                    val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
                    val gaid = adInfo.id
                    LogUtils.i("jxc", "gaid:$gaid")
                    SPUtils.setGaid(gaid ?: "")
                    emitter.onNext(gaid!!)
                } catch (e: Exception) {
                    LogUtils.e("jxc", "Error occurred when trying to get GAID.", e)
                }
            }.start()
        }
    }

    /**
     * 获取归因code
     */
    private fun getAdjustCode(context: Context): Observable<String> {
        return Observable.create<String> { emitter ->
            val result: String = SPUtils.getAdjustResult() ?: ""
            if (!TextUtils.isEmpty(result)) {
                emitter.onNext(result)
            }
            var environment = AdjustConfig.ENVIRONMENT_SANDBOX
            if (!BuildConfig.DEBUG) {
                environment = AdjustConfig.ENVIRONMENT_PRODUCTION
            }
            val config = AdjustConfig(context, APP_TOKEN, environment)
            config.setOnEventTrackingSucceededListener { eventSuccessResponseData ->
                LogUtils.e("jxc", "归因 事件上报成功：$eventSuccessResponseData")
            }
            config.setOnEventTrackingFailedListener { eventFailureResponseData ->
                LogUtils.e("jxc", "归因 事件上报失败：$eventFailureResponseData")
            }
            config.setSendInBackground(true)
            config.setLogLevel(LogLevel.INFO)
            config.setOnAttributionChangedListener { attribution ->
                LogUtils.i("jxc", "onAttributionChanged: $attribution")
                SPUtils.setAdjustResult(context, attribution.toString())
                emitter.onNext(attribution.toString())
            }
            Adjust.onCreate(config)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}