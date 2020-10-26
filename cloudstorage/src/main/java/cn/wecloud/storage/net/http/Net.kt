package cn.wecloud.storage.net.http

import android.app.Application
import cn.wecloud.storage.AppContextWrapper
import cn.wecloud.storage.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import cn.wecloud.storage.net.exception.ExceptionHandler
import cn.wecloud.storage.net.exception.ExceptionManager
import cn.wecloud.storage.net.utils.ApplicationUtil
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 * Created by Chauncey on 2018/4/26 10:31.
 */
class Net private constructor(builder: Builder) {

    companion object {

        private const val DEFAULT_INTERCEPTOR_KEY = "default_interceptor"
        private var defaultInstance: Net? = null

        private fun init(builder: Builder) = Net(builder)



        fun getDefault(): Net =
            if (defaultInstance == null) {
                Builder()
                    .baseUrl(BuildConfig.BASE_URL ?: "")
                    .apply {
                        ApplicationUtil.getMetaData(DEFAULT_INTERCEPTOR_KEY)
                            ?.let { className ->
                                try {
                                    Class.forName(className).newInstance().let {
                                        if (it is Interceptor) {
                                            addInterceptor(it)
                                        }
                                    }
                                } catch (e: Exception) {

                                }
                            }
                    }
                    .create()
                    .apply { defaultInstance = this }
            } else {
                defaultInstance!!
            }
    }

    private val retrofit: Retrofit

    init {
        val okBuilder = OkHttpClient.Builder()
        builder.interceptorList?.forEach {
            okBuilder.addInterceptor(it)
        }

        retrofit = Retrofit.Builder().baseUrl(builder.baseUrl)
            .client(
                okBuilder
                    .connectTimeout(builder.connectTimeout, builder.connectTimeoutUnit)
                    .readTimeout(builder.readTimeout, builder.readTimeoutUnit)
                    .writeTimeout(builder.writeTimeout, builder.writeTimeoutUnit)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    internal var onHttpRequestCallback: OnHttpRequestCallback? = null

    fun setOnHttpRequestCallback(onHttpRequestCallback: OnHttpRequestCallback) {
        this.onHttpRequestCallback = onHttpRequestCallback
    }

    fun registerHttpExceptionMessage(code: Int, getMessage: () -> String?) {
        ExceptionManager.register(code, object : ExceptionHandler {
            override fun getMessage(code: Int): String? = getMessage()
        })
    }

    internal fun getString(resId: Int) = AppContextWrapper.getApplicationContext().getString(resId)

    private var mClass: Any? = null
    private var service: Any? = null

    @Suppress("UNCHECKED_CAST")
    fun <T> getService(clazz: Class<T>): T {
        this.let {
            if (it.mClass == null || (clazz != it.mClass)) {
                it.mClass = clazz
                it.service = it.retrofit.create(clazz)
            }
            return it.service as T
        }
    }

    class Builder {

        internal var baseUrl: String = ""
        internal var connectTimeout: Long = 20
        internal var connectTimeoutUnit = TimeUnit.SECONDS
        internal var readTimeout: Long = 20
        internal var readTimeoutUnit = TimeUnit.SECONDS
        internal var writeTimeout: Long = 10
        internal var writeTimeoutUnit = TimeUnit.SECONDS
        internal var interceptorList: ArrayList<Interceptor>? = null

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun connectTimeout(timeout: Long, unit: TimeUnit): Builder {
            connectTimeout = timeout
            connectTimeoutUnit = unit
            return this
        }

        fun readTimeout(timeout: Long, unit: TimeUnit): Builder {
            readTimeout = timeout
            readTimeoutUnit = unit
            return this

        }

        fun writeTimeout(timeout: Long, unit: TimeUnit): Builder {
            writeTimeout = timeout
            writeTimeoutUnit = unit
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            if (interceptorList == null) {
                interceptorList = ArrayList()
            }
            interceptorList!!.add(interceptor)
            return this
        }

        fun create(): Net = init(this)
    }

    interface OnHttpRequestCallback {
        fun onSuccess(any: Any, requestCode: Int? = null) {

        }

        fun onFailure(code: Int, message: String?, requestCode: Int? = null)
    }
}
