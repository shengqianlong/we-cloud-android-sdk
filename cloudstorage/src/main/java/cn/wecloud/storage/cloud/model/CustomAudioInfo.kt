package cn.wecloud.storage.cloud.model

import com.google.gson.annotations.SerializedName

data class CustomAudioInfo(
        //比特率（可为数字
        @SerializedName("bitRate")
        var bitRate:String?,
        //采样率（可为数字
        @SerializedName("sampleRate")
        var sampleRate:String?
)