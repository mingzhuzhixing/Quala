package com.quala.network.http;

import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * ClassName: com.quala.network.http.HttpRequest
 * Description: 网络返回值处理
 *
 * @author jiaxiaochen
 * @package_name  com.quala.network
 * @date 2023/12/20 23:32
 */
public class HttpRequest {

    /**
     * 获取RequestBody转化
     */
    public static RequestBody getRequestBody(JsonObject jsonObject) {
        String json = jsonObject != null ? jsonObject.toString() : "";
        return RequestBody.create(MediaType.parse("application/json"), json);
    }
}
