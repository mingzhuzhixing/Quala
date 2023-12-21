package com.kkkl.cowieu.helper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kkkl.cowieu.bean.QualaConfigBean
import com.kkkl.cowieu.bean.QualaListBean
import com.kkkl.cowieu.util.LogUtils
import com.kkkl.cowieu.util.SPUtils

/**
 * ClassName: ParseDataHelper
 * Description: 解析数据类
 *
 * @author jiaxiaochen
 * @package_name  com.kkkl.cowieu.helper
 * @date 2023/12/20 10:45
 */
object ParseDataHelper {

    /**
     * 获取config_json解析成对象数据
     */
    fun getConfigJsonData(): QualaConfigBean? {
        val configJson = SPUtils.getConfigJson()
        //LogUtils.i("configJson:$configJson")
        return if (!configJson.isNullOrEmpty()) {
            Gson().fromJson(configJson, object : TypeToken<QualaConfigBean?>() {}.type)
        } else null
    }

    /**
     * 获取job_list_Data 对象数据信息
     */
    fun getListJsonData(): MutableList<QualaListBean> {
        val listJson = SPUtils.getListJson()
        return if (!listJson.isNullOrEmpty()) {
            Gson().fromJson(listJson, object : TypeToken<MutableList<QualaListBean?>?>() {}.type)
        } else mutableListOf()
    }

}