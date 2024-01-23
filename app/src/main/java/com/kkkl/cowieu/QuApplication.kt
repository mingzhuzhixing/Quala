package com.kkkl.cowieu

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.adjust.sdk.Adjust
import com.kkkl.cowieu.helper.InitHelper

/**
 * ClassName: QuApplication
 * Description: 主application
 *
 * @author zhaowei
 * @package_name  com.kkkl.cowieu
 * @date 2023/12/19 10:43
 */
class QuApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        InitHelper.INSTANCE.initContext(this)
        registerActivityLifecycleCallbacks(AdjustLifecycleCallbacks())
    }

    private class AdjustLifecycleCallbacks : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityResumed(activity: Activity) {
            Adjust.onResume()
        }

        override fun onActivityPaused(activity: Activity) {
            Adjust.onPause()
        }

        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {}
    }
}