package com.kkkl.cowieu.quala_api

import com.kkkl.cowieu.bean.QualaConfigBean
import com.kkkl.cowieu.bean.QualaEventBean
import com.kkkl.cowieu.bean.QualaListBean
import com.quala.network.http.HttpResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * ClassName: com.kkkl.cowieu.quala_api.QualaApi
 * Description: 网络需要的 api
 *
 * @author jiaxiaochen
 * @package_name com.kkkl.cowieu.quala_api
 * @date 2023/12/20 23:35
 */
interface QualaApi {
    /**
     * 配置信息
     */
    @POST("quala/c1c")
    fun getQualaConfig(@Body body: RequestBody?): Observable<HttpResponse<QualaConfigBean?>?>

    /**
     * 工作列表
     */
    @POST("quala/j2j")
    fun getQualaJobList(@Body body: RequestBody?): Observable<HttpResponse<List<QualaListBean?>?>?>

    /**
     * 配置信息
     */
    @POST("quala/e3e")
    fun getQualaEvent(@Body body: RequestBody?): Observable<HttpResponse<QualaEventBean?>?>
}