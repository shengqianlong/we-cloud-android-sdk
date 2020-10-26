package cn.wecloud.storage.net.entity

/**
 * Created by Chauncey on 2018/11/16 14:44.
 */
interface IResponse {
    fun getResponseCode(): Int

    fun getResponseMessage(): String

    fun getSuccessCode(): Int
}
