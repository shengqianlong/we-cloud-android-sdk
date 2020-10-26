package cn.wecloud.storage.cloud.response

import com.google.gson.annotations.SerializedName
import cn.wecloud.storage.cloud.model.FileInfo

data class FilesUploadResponse(
    @SerializedName("data")
    var data: Data?
) : BaseStorageResponse() {
    data class Data(
        @SerializedName("success")
        var success: List<FileInfo>,
        @SerializedName("fail")
        var fail: List<FileInfo>?
    )
}