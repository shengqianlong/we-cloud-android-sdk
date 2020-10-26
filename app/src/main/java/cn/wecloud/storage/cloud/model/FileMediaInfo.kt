package cn.wecloud.storage.cloud.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Chauncey on 2019-04-24 10:23.
 *
 */
data class FileMediaInfo(
    @SerializedName("media")
    var media: FileInfo,
    @SerializedName("thumbnail")
    var thumbnail: FileInfo

)