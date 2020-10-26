package cn.wecloud.storage.cloud.response

import cn.wecloud.storage.cloud.model.FileInfo
import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("data")
    var data: FileInfo
) : BaseStorageResponse()