package com.kkkl.cowieu.util

import android.util.Log
import com.kkkl.cowieu.BuildConfig

/**
 * ClassName: com.kkkl.cowieu.util.LogUtil
 * Description: log日志工具
 *
 * @author jxc
 * @package_name  com.kkkl.cowieu.util
 * @date 2023/12/20 12:47
 */
object LogUtils {
    private const val TAG = "jxc"
    fun i(message: String?) {
        i(TAG, message)
    }

    fun i(tag: String?, message: String?) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message ?: "")
        }
    }

    fun d(message: String?) {
        d(TAG, message)
    }

    fun d(tag: String?, message: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message ?: "")
        }
    }

    fun w(message: String?) {
        w(TAG, message)
    }

    fun w(tag: String?, message: String?) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message ?: "")
        }
    }

    fun e(message: String?) {
        e(TAG, message)
    }

    fun e(tag: String?, message: String?) {
        e(tag, message, null)
    }

    fun e(tag: String?, message: String?, throwable: Throwable?) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, throwable)
        }
    }

    fun v(message: String?) {
        v(TAG, message)
    }

    fun v(tag: String?, message: String?) {
        v(tag, message, null)
    }

    fun v(tag: String?, message: String?, throwable: Throwable?) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message, throwable)
        }
    }
}