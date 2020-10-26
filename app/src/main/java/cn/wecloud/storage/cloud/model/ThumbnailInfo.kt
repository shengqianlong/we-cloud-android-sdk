package cn.wecloud.storage.cloud.model

import com.google.gson.annotations.SerializedName

data class ThumbnailInfo(
        //文件名（不带拓展名称）
        @SerializedName("baseName")
        var baseName:String?,
        //文件拓展名（jpg、jpeg、png等图片拓展名）
        @SerializedName("extension")
        var extension:String?,
        //缩略图生成时间（默认为0）//可为"00:00:00.000"时间格式 //也可为"0.000"时间格式
        @SerializedName("startTime")
        var startTime:String?,
        //分辨率大小//（[width] x [height]
        @SerializedName("size")
        var size:String?
)