package cn.wecloud.storage.cloud.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Chauncey on 2019-06-10 11:28.
 *
 */
data class CustomImageInfo(
        //目标格式： //jpg、jpeg、gif、bmp、wbmp、png
        @SerializedName("targetFormat") var targetFormat: String?="jpg",
        //图片宽度  //（高度与宽度必须同时有值或为空）
        @SerializedName("width") var width: Int? = 0,
        //图片高度  //（高度与宽度必须同时有值或为空）
        @SerializedName("height") var height: Int? = 0,
        //图片质量（0<quality<=1）
        @SerializedName("quality") var quality: Float? = 0f,
        //旋转角度
        @SerializedName("rotate") var rotate: Double? = 0.0,
        //图片缩放大小
        @SerializedName("scale") var scale: Double? = 0.0
)