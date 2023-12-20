package com.kkkl.cowieu.util

import android.content.Context
import android.content.SharedPreferences
import com.kkkl.cowieu.helper.InitHelper
import java.lang.reflect.Method

/**
 * ClassName: com.kkkl.cowieu.util.SPUtils
 * Description: 文件存储
 *
 * @author jxc
 * @package_name com.kkkl.cowieu.util
 * @date 2023/12/20 12:15
 */
object SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    const val FILE_NAME = "quala_data"

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    fun put(context: Context?, key: String?, value: Any) {
        if (context == null) {
            return
        }
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        if (value is String) {
            editor.putString(key, value)
        } else if (value is Int) {
            editor.putInt(key, value)
        } else if (value is Boolean) {
            editor.putBoolean(key, value)
        } else if (value is Float) {
            editor.putFloat(key, value)
        } else if (value is Long) {
            editor.putLong(key, value)
        } else {
            editor.putString(key, value.toString())
        }
        SharedPreferencesCompat.apply(editor)
    }

    fun getString(context: Context?, key: String?): String? {
        if (context == null) {
            return ""
        }
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp.getString(key, "")
    }

    fun getInt(context: Context?, key: String?): Int {
        if (context == null) {
            return 0
        }
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp.getInt(key, 0)
    }

    fun getBoolean(context: Context?, key: String?): Boolean {
        if (context == null) {
            return false
        }
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp.getBoolean(key, false)
    }

    fun getLong(context: Context?, key: String?): Long {
        if (context == null) {
            return 0L
        }
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp.getLong(key, 0L)
    }

    const val KEY_ADJUST_RESULT = "adjust_result"

    /**
     * 设置归因结果
     */
    fun setAdjustResult(context: Context, value: String) {
        put(context, KEY_ADJUST_RESULT, value)
    }

    fun getAdjustResult(): String? {
        val context = InitHelper.INSTANCE.getApplicationContext() ?: return ""
        return getString(context, KEY_ADJUST_RESULT)
    }

    const val KEY_GAID = "gaid"

    /**
     * 获取gaid
     */
    fun setGaid(value: String) {
        val context = InitHelper.INSTANCE.getApplicationContext()
        if (context != null) {
            put(context, KEY_GAID, value)
        }
    }

    fun getGaid(): String? {
        val context = InitHelper.INSTANCE.getApplicationContext() ?: return ""
        return getString(context, KEY_GAID)
    }

    const val KEY_CONFIG_JSON = "config_json"

    /**
     * 设置config_json
     */
    fun setConfigJson(context: Context, value: String) {
        put(context, KEY_CONFIG_JSON, value)
    }

    @JvmStatic
    fun getConfigJson(): String? {
        return getString(InitHelper.INSTANCE.getApplicationContext(), KEY_CONFIG_JSON)
    }

    const val KEY_LIST_JSON = "list_json"

    /**
     * 工作列表数据_json
     */
    fun setJobListJson(value: String) {
        put(InitHelper.INSTANCE.getApplicationContext(), KEY_LIST_JSON, value)
    }

    fun getJobListJson(): String? {
        return getString(InitHelper.INSTANCE.getApplicationContext(), KEY_LIST_JSON)
    }

    const val KEY_INVOKEAPP_SUC_COUNT = "invokeapp_suc_count"

    /**
     * 记录 用户调用invokeApp返回success的5次后，用户不再展示任何C类卡片的内容
     */
    fun getInvokeAppSuccessCount(): Int {
        return getInt(InitHelper.INSTANCE.getApplicationContext(), KEY_INVOKEAPP_SUC_COUNT)
    }

    fun setInvokeAppSuccessCount(value: Int) {
        put(InitHelper.INSTANCE.getApplicationContext(), KEY_INVOKEAPP_SUC_COUNT, value)
    }

    /**
     * 记录 用户首次调用invokeApp返回sucess满1小时，用户不再展示任何C类卡片的内容
     * 毫秒
     */
    const val KEY_INVOKEAPP_SUC_TIME = "invokeapp_suc_time"
    fun getFirstInvokeAppSuccessTime(): Long {
        return getLong(InitHelper.INSTANCE.getApplicationContext(), KEY_INVOKEAPP_SUC_TIME)
    }

    fun setFirstInvokeAppSuccessTime(value: Long) {
        put(InitHelper.INSTANCE.getApplicationContext(), KEY_INVOKEAPP_SUC_TIME, value)
    }

    const val KEY_FIRST_OPEN_TIME = "first_open_time"
    fun getFirstOpenAppTime(): Long {
        return getLong(InitHelper.INSTANCE.getApplicationContext(), KEY_FIRST_OPEN_TIME)
    }

    /**
     * 记录 用户首次打开APP，满24小时，用户不再展示任何C类卡片的内容
     * 毫秒
     */
    fun getFirstOpenAppTime(value: Int) {
        put(InitHelper.INSTANCE.getApplicationContext(), KEY_FIRST_OPEN_TIME, value)
    }

    /**
     * 是否上报过 addtocartlt
     */
    const val KEY_ADDTOCARTLT = "addtocartlt"
    var addToCartLt: Boolean
        get() = getBoolean(InitHelper.INSTANCE.getApplicationContext(), KEY_ADDTOCARTLT)
        set(value) {
            put(InitHelper.INSTANCE.getApplicationContext(), KEY_ADDTOCARTLT, value)
        }

    /**
     * 是否上报过 jobs_show_ptjob
     */
    const val KEY_JOBS_SHOW_PTJOB = "jobs_show_ptjob"
    fun getJobsShowPtJob(): Boolean {
        return getBoolean(InitHelper.INSTANCE.getApplicationContext(), KEY_JOBS_SHOW_PTJOB)
    }

    fun setJobsShowPtJob(value: Boolean) {
        put(InitHelper.INSTANCE.getApplicationContext(), KEY_JOBS_SHOW_PTJOB, value)
    }

    /**
     * 是否上报过 user_event
     */
    const val KEY_USER_EVENT = "user_event"
    var userEvent: Boolean
        get() = getBoolean(InitHelper.INSTANCE.getApplicationContext(), KEY_USER_EVENT)
        set(value) {
            put(InitHelper.INSTANCE.getApplicationContext(), KEY_USER_EVENT, value)
        }

    /**
     * 移除某个key值已经对应的值
     */
    fun remove(context: Context, key: String?) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 清除所有数据
     */
    fun clear(context: Context) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 查询某个key是否已经存在
     */
    fun contains(context: Context, key: String?): Boolean {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp.contains(key)
    }

    /**
     * 返回所有的键值对
     */
    fun getAll(context: Context): Map<String, *> {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp.all
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * 反射查找apply的方法
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz: Class<*> = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            editor.commit()
        }
    }
}