package cn.wecloud.storage.net.exception

/**
 * Created by Chauncey on 2019-05-14 10:11.
 */
interface ExceptionHandler {
    fun getMessage(code: Int): String?
}
