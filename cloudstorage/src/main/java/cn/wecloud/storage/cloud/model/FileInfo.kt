package cn.wecloud.storage.cloud.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Chauncey on 2019-04-24 10:23.
 *
 */
data class FileInfo(
    @SerializedName("id")
    var id: Long,
    @SerializedName("userId")
    var userId: Long,
    @SerializedName("url")
    var url: String,
    @SerializedName("fileFormat")
    var fileFormat: String,
    @SerializedName("fileId")
    var fileId: Long,
    @SerializedName("fileName")
    var fileName: String,
    @SerializedName("isDelete")
    var isDelete: Boolean,
    @SerializedName("isPublic")
    var isPublic: Boolean,
    @SerializedName("createTime")
    var createTime: Long,
    @SerializedName("modifyTime")
    var modifyTime: Long?,
    @SerializedName("customKey")
    var customKey: String?
)