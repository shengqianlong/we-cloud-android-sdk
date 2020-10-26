package cn.wecloud.storage.net.exception

import android.net.ParseException

import com.google.gson.JsonParseException
import org.json.JSONException

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

import retrofit2.HttpException
import javax.net.ssl.SSLHandshakeException


/**
 * 网络访问异常处理
 */

class NetworkExceptionHandler {

    /**
     * 约定异常
     */
    object ERROR {

        /**
         * 未知错误
         */
        val UNKNOWN = 10000
        /**
         * 解析错误 数据异常
         */
        internal val PARSE_ERROR = 10001
        /**
         * 网络错误
         */
        val NETWORK_ERROR = 10002
        /**
         * 协议出错
         */
        internal val HTTP_ERROR = 10003

        /**
         * 证书出错
         */
        internal val SSL_ERROR = 10004

        /**
         * 连接超时
         */
        val TIMEOUT_ERROR = 10005
    }

    companion object {
        //        private const val BAD_REQUEST = -1

//        private const val SYSTEM_ERROR = -9999

        internal fun handleException(e: Throwable): ResponseThrowable {

            val code: Int
            val message: String?

            when (e) {

                is HttpException -> {
                    HttpExceptionHandler.handle(e).let {
                        code = it.first
                        message = it.second
                    }
                }

                is RuntimeException -> {
                    code = 0
                    message = e.message
                }

                is UnknownHostException -> {
                    code = -1
                    message = ExceptionManager.getMessage(code) ?: "Network error"
                }

                is JsonParseException, is JSONException, is ParseException -> {
                    code = ERROR.PARSE_ERROR
                    message = "Abnormal data parsing"
                }

                is ConnectException -> {
                    code = ERROR.NETWORK_ERROR
                    message = "Connection failed"
                }

                is SSLHandshakeException -> {
                    code = ERROR.SSL_ERROR
                    message = "证书验证失败"
                }

                is TimeoutException, is SocketTimeoutException -> {
                    code = ERROR.TIMEOUT_ERROR
                    message = "The request timeout"
                }

                else -> {
                    code = ERROR.UNKNOWN
                    message = "Unknown exception"
                }
            }
            return ResponseThrowable(e, code, message)
        }
    }
}
