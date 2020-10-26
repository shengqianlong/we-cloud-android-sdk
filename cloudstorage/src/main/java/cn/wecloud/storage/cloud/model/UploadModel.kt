package cn.wecloud.storage.cloud.model


import com.google.gson.annotations.SerializedName
import java.io.File

data class UploadModel(
        @SerializedName("file")
        var file: File?=null,
        @SerializedName("bucketId")//空间Id
        var bucketId: Long = -1,

        @SerializedName("cover")
        var cover: Boolean? = null,
        @SerializedName("customId")
        var customId: String? = null,
        /**
         * 文件过期时间：
         * expired < 0 或 expired == null 不过期，
         * expired == 0 次日0点过期，
         * expired > 0 指定天数后0点过期
         */
        @SerializedName("expired")
        var expired: Long?=-1,
        @SerializedName("fileHash")
        var fileHash: String? = null,
        @SerializedName("uploadToken")
        var uploadToken:String,


        //上传图片
        @SerializedName("customImageInfo")
        var customImageInfo: CustomImageInfo?=null,

        //上传音频
        @SerializedName("customAudioInfo")
        var customAudioInfo:CustomAudioInfo?=null,

        //上传视频
        @SerializedName("notifyUrl")
        var notifyUrl:String?=null,//若asyncProcessing为true时必填 回调url
        @SerializedName("asyncProcessing")
        var asyncProcessing:Boolean?= false,//否异步生成缩略图
        @SerializedName("customVideoInfo")
        var customVideoInfo:CustomVideoInfo?=null,
        @SerializedName("thumbnailInfo")
        var thumbnailInfo:ThumbnailInfo?=null,

        //缩略图
        @SerializedName("userFileId")//缩略图要用到
        var userFileId:Long? =-1,
        @SerializedName("baseName")
        var baseName:String?= null,//文件名（不带拓展名称）
        @SerializedName("extension")
        var extension:String?=null,//文件拓展名（jpg、jpeg、png等图片拓展名）
        @SerializedName("startTime")
        var startTime:String?=null,//缩略图生成时间（默认为0） 可为"00:00:00.000"时间格式 也可为"0.000"时间格式
        @SerializedName("size")
        var size:String?=null//分辨率大小  [width] x [height]


)