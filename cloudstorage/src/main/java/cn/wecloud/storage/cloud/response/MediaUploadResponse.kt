package cn.wecloud.storage.cloud.response

import cn.wecloud.storage.cloud.model.FileMediaInfo
import com.google.gson.annotations.SerializedName


data class MediaUploadResponse(
    @SerializedName("data")
    var data: FileMediaInfo
) : BaseStorageResponse()