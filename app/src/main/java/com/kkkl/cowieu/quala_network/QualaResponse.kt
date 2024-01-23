package com.kkkl.cowieu.quala_network

import com.google.gson.annotations.SerializedName

/**
 * ClassName: com.quala.network.http.QualaResponse
 * Description: 网络返回值处理
 *
 * @author zhaowei
 * @package_name  com.quala.network
 * @date 2023/12/20 23:30
 */
class QualaResponse<T> {
    @JvmField
    var data: T? = null

    @SerializedName(value = "code")
    var code = 0

    @JvmField
    @SerializedName(value = "msg")
    var msg = ""
}