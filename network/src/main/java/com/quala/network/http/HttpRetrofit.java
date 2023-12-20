package com.quala.network.http;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ClassName: com.quala.network.http.HttpRetrofit
 * Description: 网络请求框架
 *
 * @author jiaxiaochen
 * @package_name  com.quala.network
 * @date 2023/12/20 10:30
 */
public class HttpRetrofit {
    private static class Holder {
        private final static HttpRetrofit instance = new HttpRetrofit();
    }

    public static HttpRetrofit getInstance() {
        return Holder.instance;
    }

    private static final String BASE_URL = "https://xvtxpcjrka.execute-api.us-east-2.amazonaws.com/Prod/";
//    private static final String BASE_URL = "https://dsaf.f6g7h8i.com/";

    private Retrofit mRetrofit;

    public HttpRetrofit() {
        init();
    }

    private void init() {
        HttpSslSocket.SSLParams sslParams = HttpSslSocket.getSslSocketFactory(null, null, null);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.SECONDS)
                .connectTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();

        mRetrofit = new Retrofit.Builder().client(mOkHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T create(final Class<T> service) {
        return mRetrofit.create(service);
    }
}
