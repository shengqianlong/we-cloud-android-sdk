package cn.wecloud.storage.cloud.model


import com.google.gson.annotations.SerializedName
import java.io.File

data class UploadFastModel(
        @SerializedName("bucketId")//空间Id
        var bucketId: Long = -1,
        @SerializedName("customId")
        var customId: String? = null,
        /**
         * 文件过期时间：
         * expired < 0 或 expired == null 不过期，
         * expired == 0 次日0点过期，
         * expired > 0 指定天数后0点过期
         */
        @SerializedName("expired")
        var expired: Long? = -1,
        @SerializedName("fileHash")
        var fileHash: String? = null,

        @SerializedName("fileName")
        var fileName: String? = null,
        @SerializedName("fileSize")
        var fileSize: String? = null,
        @SerializedName("mimeType")
        var mimeType: String? = null,

        @SerializedName("uploadToken")
        var uploadToken:String

)