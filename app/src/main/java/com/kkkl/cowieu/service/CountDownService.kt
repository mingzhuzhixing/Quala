package com.kkkl.cowieu.service

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import com.kkkl.cowieu.event.InvokeAppStartTimeEvent
import com.kkkl.cowieu.event.RefreshCardDataEvent
import com.kkkl.cowieu.util.QualaLogUtils
import com.kkkl.cowieu.util.SPreferenceUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * ClassName: CountDownService
 * Description: 倒计时service
 *
 * @author zhaowei
 * @package_name  com.kkkl.cowieu
 * @date 2023/12/21 08:43
 */
class CountDownService : Service() {

    private var mInvokeAppSuccessTimer: CountDownTimer? = null

    private var mOpenAppTimer: CountDownTimer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        invokeAppSuccessTimer()
        openAppTimer()
    }

    /**
     * 开始定制器
     */
    private fun invokeAppSuccessTimer() {
        if (mInvokeAppSuccessTimer != null) {
            mInvokeAppSuccessTimer?.cancel()
        }
        val subTime: Long = SPreferenceUtils.getFirstInvokeAppSuccessTime() - System.currentTimeMillis() //毫秒
        QualaLogUtils.d("invokeAppSuccessTimer:$subTime")
        if (subTime <= 0) {
            return
        }
        mInvokeAppSuccessTimer = object : CountDownTimer(subTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 每过一秒回调此方法
                QualaLogUtils.i("invokeAppSuccessTimer onTick: $millisUntilFinished");
            }

            override fun onFinish() {
                // 倒计时结束后回调此方法
                QualaLogUtils.i("invokeAppSuccessTimer 倒计时结束后回调此方法")
                EventBus.getDefault().post(RefreshCardDataEvent())
            }
        }
        mInvokeAppSuccessTimer?.start()
    }

    /**
     * 开启倒计时计时器
     */
    private fun openAppTimer() {
        if (mOpenAppTimer != null) {
            mOpenAppTimer?.cancel()
        }
        val subTime: Long = SPreferenceUtils.getFirstOpenAppTime() - System.currentTimeMillis() //毫秒
        if (subTime <= 0) {
            return
        }
        QualaLogUtils.d("openAppTimer:$subTime")
        mOpenAppTimer = object : CountDownTimer(subTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 每过一秒回调此方法
                QualaLogUtils.i("openAppTimer onTick: $millisUntilFinished");
            }

            override fun onFinish() {
                // 倒计时结束后回调此方法
                QualaLogUtils.i("openAppTimer 倒计时结束后回调此方法")
                EventBus.getDefault().post(RefreshCardDataEvent())
            }
        }
        mOpenAppTimer?.start()
    }

    /**
     * 启动第一次打开invokeApp定时器
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun notifyRefreshAllData(event: InvokeAppStartTimeEvent?) {
        invokeAppSuccessTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        mInvokeAppSuccessTimer?.cancel()
        mOpenAppTimer?.cancel()
    }
}