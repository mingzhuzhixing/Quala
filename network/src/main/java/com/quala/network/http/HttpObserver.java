package com.quala.network.http;


import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * ClassName: com.quala.network.http.HttpObserver
 * Description: 网络观察者
 *
 * @author jiaxiaochen
 * @package_name  com.quala.network
 * @date 2023/12/20 23:30
 */
public abstract class HttpObserver<T> implements Observer<HttpResponse<T>> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(HttpResponse<T> t) {
        if (t.data == null) {
            onFailure(t.msg);
        } else {
            onSuccess(t.data);
        }
    }

    @Override
    public void onError(Throwable e) {
        onFailure(e.getMessage());
    }


    @Override
    public void onComplete() {

    }

    /**
     * 成功
     */
    public abstract void onSuccess(T data);

    /**
     * 失败
     */
    public void onFailure(String errorMsg) {
        Log.i("HttpObserver", "接口请求失败：" + errorMsg);
    }
}
