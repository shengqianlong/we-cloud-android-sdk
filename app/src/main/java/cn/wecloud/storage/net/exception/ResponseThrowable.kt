package cn.wecloud.storage.net.exception

/**
 * Created by Chauncey on 2019-05-14 10:21.
 *
 */
class ResponseThrowable(throwable: Throwable, val code: Int, exceptionMessage: String?) :
    Exception(exceptionMessage, throwable)