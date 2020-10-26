package cn.wecloud.storage.cloud.response

import com.google.gson.annotations.SerializedName
import cn.wecloud.storage.net.entity.IResponse

/**
 * Created by Chauncey on 2019-04-23 18:03.
 */
open class BaseStorageResponse : IResponse {

    companion object {
        private const val SUCCESS_CODE = 0
    }

    @SerializedName("code")
    var code: Int = -1

    @SerializedName("msg")
    var msg: String = ""

    override fun getResponseCode(): Int {
        return code
    }

    override fun getResponseMessage(): String {
        return msg
    }

    override fun getSuccessCode(): Int {
        return SUCCESS_CODE
    }
}
