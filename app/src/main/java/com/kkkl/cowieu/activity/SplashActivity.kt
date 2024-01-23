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
import com.kkkl.cowieu.bean.QualaConfigBean
import com.kkkl.cowieu.event.LoadAdjustEvent
import com.kkkl.cowieu.helper.QualaConstants
import com.kkkl.cowieu.quala_api.QualaApi
import com.kkkl.cowieu.quala_network.QualaObserver
import com.kkkl.cowieu.quala_network.QualaRequest
import com.kkkl.cowieu.quala_network.QualaRetrofit
import com.kkkl.cowieu.util.QualaLogUtils
import com.kkkl.cowieu.util.SPreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * ClassName: SplashActivity
 * Description: 开屏页
 *
 * @author zhaowei
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
        notifyAdjustResult(LoadAdjustEvent(2))
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
            val grid = SPreferenceUtils.getGaid()
            val adjust = SPreferenceUtils.getAdjustResult()
            QualaLogUtils.v("开始请求config接口 source:${event.type} gaid>>>>$grid<<<<adjuest>>>>$adjust")
            if (TextUtils.isEmpty(grid) || TextUtils.isEmpty(adjust)) {
                return
            }

            //判断config数据
            val configJson = SPreferenceUtils.getConfigJson()
            if (configJson.isNullOrEmpty()) {
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
        json.addProperty(QualaConstants.KEY_ATTRIBUTES, SPreferenceUtils.getAdjustResult())
        json.addProperty(QualaConstants.KEY_GAID, SPreferenceUtils.getGaid())
        QualaRetrofit.getInstance().create(QualaApi::class.java)
            .getQualaConfig(QualaRequest.getRequestBody(json))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : QualaObserver<QualaConfigBean?>() {
                override fun onSuccess(data: QualaConfigBean?) {
                    QualaLogUtils.w("onSuccess config:" + Gson().toJson(data))
                    SPreferenceUtils.setConfigJson(context, Gson().toJson(data))
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