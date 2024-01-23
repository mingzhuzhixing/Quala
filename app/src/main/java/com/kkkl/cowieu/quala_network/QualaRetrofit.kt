package com.kkkl.cowieu.quala_network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

/**
 * ClassName: com.quala.network.http.QualaRetrofit
 * Description: 网络请求框架
 *
 * @author zhaowei
 * @package_name  com.quala.network
 * @date 2023/12/20 10:30
 */
class QualaRetrofit {
    companion object {
        fun getInstance(): QualaRetrofit {
            return Holder.holder
        }

        //        private const val BASE_URL = "https://xvtxpcjrka.execute-api.us-east-2.amazonaws.com/Prod/";
        private const val BASE_URL = "https://opwe.lwowlwlq9.com/"
    }

    private object Holder {
        val holder = QualaRetrofit()
    }

    private var mRetrofit: Retrofit? = null

    init {
        init()
    }

    private fun init() {
        val sslParams = QualaSslSocket.getSslSocketFactory(null, null, null)
        val mOkHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.SECONDS)
            .connectTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .hostnameVerifier(HostnameVerifier { hostname, session -> true })
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
            .build()

        mRetrofit = Retrofit.Builder().client(mOkHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(service: Class<T>?): T {
        return mRetrofit!!.create(service)
    }
}