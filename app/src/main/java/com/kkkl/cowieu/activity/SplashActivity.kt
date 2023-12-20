package com.kkkl.cowieu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kkkl.cowieu.R
import com.kkkl.cowieu.bean.ConfigBean
import com.kkkl.cowieu.event.LoadAdjustEvent
import com.kkkl.cowieu.helper.Constants
import com.kkkl.cowieu.quala_api.QualaApi
import com.kkkl.cowieu.util.LogUtils
import com.kkkl.cowieu.util.SPUtils
import com.quala.network.http.HttpObserver
import com.quala.network.http.HttpRequest
import com.quala.network.http.HttpRetrofit
import com.quala.network.http.HttpSchedulers
import kotlinx.android.synthetic.main.activity_splash.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * ClassName: SplashActivity
 * Description: 开屏页
 *
 * @author jiaxiaochen
 * @package_name  com.kkkl.cowieu.activity
 * @date 2023/12/20 10:21
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    /**
     * 通知归因结果刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun notifyAdjustResult(event: LoadAdjustEvent) {
        try {
            if (isDestroyed) {
                return
            }
            val gaid = SPUtils.getGaid()
            val adjuest = SPUtils.getAdjustResult()
            LogUtils.v("jxc", "开始请求config接口 source:${event.type} gaid>>>>$gaid<<<<adjuest>>>>$adjuest")
            if (TextUtils.isEmpty(gaid) || TextUtils.isEmpty(adjuest)) {
                return
            }

            //判断config数据
            val configJson = SPUtils.getConfigJson()
            if (TextUtils.isEmpty(configJson)) {
                getConfigData(this)
            } else {
                startMainActivity(2000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 获取配置信息
     */
    private fun getConfigData(context: Context) {
        val json = JsonObject()
        json.addProperty(Constants.KEY_ATTRIBUTES, SPUtils.getAdjustResult())
        json.addProperty(Constants.KEY_GAID, SPUtils.getGaid())
        HttpRetrofit.getInstance().create(QualaApi::class.java)
            .getQualaConfig(HttpRequest.getRequestBody(json))
            .compose(HttpSchedulers.applySchedulers())
            .subscribe(object : HttpObserver<ConfigBean?>() {
                override fun onSuccess(data: ConfigBean?) {
                    LogUtils.w("jxc", "onSuccess config:" + Gson().toJson(data))
                    SPUtils.setConfigJson(context, Gson().toJson(data))
                    startMainActivity(500)
                }
            })
    }

    /**
     * 启动主页面
     */
    private fun startMainActivity(time: Int) {
        Handler().postDelayed({
            if (progressBar != null) {
                progressBar.visibility = View.GONE
            }
            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(intent)
            this@SplashActivity.finish()
        }, time.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}