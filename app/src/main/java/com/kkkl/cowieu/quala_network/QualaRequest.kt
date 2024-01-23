package com.kkkl.cowieu.quala_network

import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

/**
 * ClassName: com.quala.network.http.QualaRequest
 * Description: 网络返回值处理
 *
 * @author zhaowei
 * @package_name  com.quala.network
 * @date 2023/12/20 23:32
 */
object QualaRequest {
    /**
     * 获取RequestBody转化
     */
    fun getRequestBody(jsonObject: JsonObject?): RequestBody {
        val json = jsonObject?.toString() ?: ""
        return RequestBody.create("application/json".toMediaTypeOrNull(), json)
    }
}