package cn.wecloud.storage.cloud.model

import com.google.gson.annotations.SerializedName

data class CustomVideoInfo(
        //比特率
        @SerializedName("bitRate")
        var bitRate:String?,
        //帧率（可为数字
        @SerializedName("frameRate")
        var frameRate:String?,
        //分辨率大小//（[width] x [height]
        @SerializedName("size")
        var size:String,
        //视频质量//（0-18为视觉上无损压缩，默认值为23，最大值为51）
        @SerializedName("crf")
        var crf:String
)