package com.quala.network.http;

import com.google.gson.annotations.SerializedName;

/**
 * ClassName: com.quala.network.http.HttpResponse
 * Description: 网络返回值处理
 *
 * @author jiaxiaochen
 * @package_name  com.quala.network
 * @date 2023/12/20 23:30
 */
public class HttpResponse<T> {
    T data;

    @SerializedName(value = "code")
    int code = 0;

    @SerializedName(value = "msg")
    String msg = "";

}
