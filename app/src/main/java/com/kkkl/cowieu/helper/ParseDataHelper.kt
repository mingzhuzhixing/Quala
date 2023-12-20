package com.kkkl.cowieu.helper

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kkkl.cowieu.bean.ConfigBean
import com.kkkl.cowieu.util.SPUtils

/**
 * ClassName: ParseDataHelper
 * Description: 解析数据类
 *
 * @author jxc
 * @package_name  com.kkkl.cowieu.helper
 * @date 2023/12/19 10:45
 */
object ParseDataHelper {

    /**
     * 获取config_json解析成对象数据
     */
    fun getConfigJsonData(): ConfigBean? {
        val context = InitHelper.INSTANCE.getApplicationContext() ?: return null
        val configJson = SPUtils.getConfigJson(context)
        return if (!TextUtils.isEmpty(configJson)) {
            Gson().fromJson(configJson, object : TypeToken<ConfigBean?>() {}.type)
        } else null
    }

}