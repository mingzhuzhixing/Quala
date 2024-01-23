package com.kkkl.cowieu.quala_network

import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * ClassName: com.quala.network.http.QualaObserver
 * Description: 网络观察者
 *
 * @author zhaowei
 * @package_name  com.quala.network
 * @date 2023/12/20 23:30
 */
abstract class QualaObserver<T> : Observer<QualaResponse<T>> {
    override fun onSubscribe(d: Disposable) {}
    override fun onNext(t: QualaResponse<T>) {
        if (t.data == null) {
            onFailure(t.msg)
        } else {
            onSuccess(t.data)
        }
    }

    override fun onError(e: Throwable) {
        onFailure(e.message)
    }

    override fun onComplete() {}

    /**
     * 成功
     */
    abstract fun onSuccess(data: T?)

    /**
     * 失败
     */
    fun onFailure(errorMsg: String?) {
        Log.i("HttpObserver", "接口请求失败：$errorMsg")
    }
}