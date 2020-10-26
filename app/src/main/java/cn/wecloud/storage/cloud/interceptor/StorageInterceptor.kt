package cn.wecloud.storage.cloud.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Chauncey on 2019/03/02 09:30.
 * Key拦截器
 */

class StorageInterceptor(private val userId: String) : Interceptor {

    companion object {
        private const val KEY = "key"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        // 添加新的参数
        val authorizedUrlBuilder = oldRequest.url()
            .newBuilder()
            .scheme(oldRequest.url().scheme())
            .host(oldRequest.url().host())

        // 新的请求
        val builder = oldRequest.newBuilder()
            .method(oldRequest.method(), oldRequest.body())
            .url(authorizedUrlBuilder.build())
            .header("Accept", "*/*")
            .header("User-Id", userId)

//                .url(authorizedUrlBuilder.build())
//                .build()

        return chain.proceed(builder.build().apply {
            Log.d("weCloud_storage", this.url().toString())
        })
    }
}
