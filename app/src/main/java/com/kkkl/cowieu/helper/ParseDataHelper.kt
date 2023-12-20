package com.kkkl.cowieu.helper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kkkl.cowieu.bean.ConfigBean
import com.kkkl.cowieu.bean.ListBean
import com.kkkl.cowieu.util.SPUtils

/**
 * ClassName: ParseDataHelper
 * Description: 解析数据类
 *
 * @author jxc
 * @package_name  com.kkkl.cowieu.helper
 * @date 2023/12/20 10:45
 */
object ParseDataHelper {

    /**
     * 获取config_json解析成对象数据
     */
    fun getConfigJsonData(): ConfigBean? {
        val context = InitHelper.INSTANCE.getApplicationContext() ?: return null
        val configJson = SPUtils.getConfigJson()
        return if (!configJson.isNullOrEmpty()) {
            Gson().fromJson(configJson, object : TypeToken<ConfigBean?>() {}.type)
        } else null
    }

    /**
     * 获取job_list_Data 对象数据信息
     */
    fun getListJsonData(): MutableList<ListBean> {
        val listJson = SPUtils.getJobListJson()
        return if (!listJson.isNullOrEmpty()) {
            Gson().fromJson(listJson, object : TypeToken<MutableList<ListBean?>?>() {}.type)
        } else mutableListOf()
    }

}