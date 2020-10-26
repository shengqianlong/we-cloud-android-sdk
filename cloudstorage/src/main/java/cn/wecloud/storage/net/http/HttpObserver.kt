package cn.wecloud.storage.net.http

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import cn.wecloud.storage.net.entity.IResponse
import cn.wecloud.storage.net.exception.NetworkExceptionHandler

/**
 * Created by Chauncey on 2017/12/12 10:21.
 * 接口请求观察者
 */

abstract class HttpObserver<T : IResponse>(private val requestCode: Int? = null) : Observer<T> {

    final override fun onSubscribe(d: Disposable) {
        onStart()
    }

    final override fun onNext(t: T) {
        when (val status = t.getResponseCode()) {
            t.getSuccessCode() -> success(t)

            else -> failure(status, t.getResponseMessage())
        }
    }

    final override fun onError(e: Throwable) {
        val throwable = NetworkExceptionHandler.handleException(e)
        failure(throwable.code, throwable.message)
        onComplete()
    }

    override fun onComplete() {

    }

    private fun success(t: T) {
        Net.getDefault().onHttpRequestCallback?.onSuccess(t, requestCode)
        onSuccess(t)
//        onComplete()
    }

    private fun failure(code: Int, message: String?) {
        Net.getDefault().onHttpRequestCallback?.onFailure(code, message, requestCode)
        onFailure(code, message)
   
//        onComplete()
    }

    protected open fun onStart() {

    }

    protected abstract fun onSuccess(t: T)

    protected open fun onFailure(code: Int, message: String?) {

    }
}
