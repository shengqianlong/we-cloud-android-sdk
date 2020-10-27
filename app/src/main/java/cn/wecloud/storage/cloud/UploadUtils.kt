package cn.wecloud.storage.cloud

import com.google.gson.Gson
import cn.wecloud.storage.cloud.model.UploadFastModel
import cn.wecloud.storage.cloud.model.UploadModel
import cn.wecloud.storage.net.utils.ApplicationUtil
import cn.wecloud.storage.net.utils.MD5Util
import org.json.JSONObject
import java.io.File
import java.net.URLEncoder

object UploadUtils {
    const val CLOUD_ACCESS_KEY = "weCloud_AccessKey"
    const val CLOUD_SECRET_KEY = "weCloud_SecretKey"
    const val CLOUD_USER_ID_VALUE = "weCloud_BucketId"

    fun createParams(uploadModel: UploadModel):HashMap<String,String?>{
        val params = HashMap<String,String?>()
        params["bucketId"] = uploadModel.bucketId.toString()
        if(!uploadModel.customId.isNullOrEmpty()){
            params["customId"] = uploadModel.customId
        }
        if(uploadModel.expired!=-1L){
            params["expired"] = uploadModel.expired.toString()
        }

        //图片
        if(uploadModel.customImageInfo!=null){
            params["customImageInfo"] =Gson().toJson(uploadModel.customImageInfo)
        }

        //视频
        if(uploadModel.customVideoInfo!=null){
            params["customVideoInfo"] =Gson().toJson(uploadModel.customVideoInfo)
        }
        if(uploadModel.thumbnailInfo!=null){
            params["thumbnailInfo"] =Gson().toJson(uploadModel.thumbnailInfo)
        }
        if(uploadModel.asyncProcessing!=null){
            if(uploadModel.asyncProcessing!!){
                params["asyncProcessing"] = uploadModel.asyncProcessing!!.toString()
                params["notifyUrl"] = uploadModel.notifyUrl
            }else{
                params["asyncProcessing"] = uploadModel.asyncProcessing!!.toString()
            }
        }

        //音频
        if(uploadModel.customAudioInfo!=null){
            params["customAudioInfo"] =Gson().toJson(uploadModel.customAudioInfo)
        }

        //缩略图
        if(uploadModel.userFileId!=null && uploadModel.userFileId!=-1L){
            params["userFileId"] = uploadModel.userFileId.toString()
        }
        if(uploadModel.baseName!=null){
            params["baseName"] = uploadModel.baseName.toString()
        }
        if(uploadModel.extension!=null){
            params["extension"] = uploadModel.extension.toString()
        }
        if(uploadModel.startTime!=null){
            params["startTime"] = uploadModel.startTime.toString()
        }
        if(uploadModel.size!=null){
            params["size"] = uploadModel.size.toString()
        }
        if(uploadModel.cover!=null && uploadModel.cover!!){
            params["cover"] = uploadModel.cover.toString()
        }
        params["uploadToken"] = uploadModel.uploadToken
        return params
    }


    fun createParamsFast(uploadModel: UploadModel):HashMap<String,String?>{
        val params = HashMap<String,String?>()
        params["bucketId"] = uploadModel.bucketId.toString()
        if(!uploadModel.customId.isNullOrEmpty()){
            params["customId"] = uploadModel.customId
        }
        if(uploadModel.expired!=-1L){
            params["expired"] = uploadModel.expired.toString()
        }
        if(!uploadModel.fileHash.isNullOrEmpty()){
            params["fileHash"] = uploadModel.fileHash.toString()
        }
        if(uploadModel.file!=null){
            params["fileName"] = uploadModel.file?.name
            params["fileSize"] = uploadModel.file?.length().toString()
            params["mimeType"] = FileUtil.getMimeType(uploadModel.file!!)
        }
        params["uploadToken"] = uploadModel.uploadToken
        return params
    }

    private fun getCloudAccess(): String? {
        return ApplicationUtil.getMetaData( CLOUD_ACCESS_KEY)
    }

    private fun getCloudSecretKey(): String? {
        return ApplicationUtil.getMetaData( CLOUD_SECRET_KEY)
    }

     fun getCloudUserIdValue(): String? {
         val userId= ApplicationUtil.getMetaData(CLOUD_USER_ID_VALUE)
        return userId?.substring(4,userId.length)
    }

    /**
     * 普通文件
     * 普通图片
     */
    fun getUploadToken(uploadModel: UploadModel):String{
        val originalSign = StringBuffer()
        originalSign.append(getCloudSecretKey()) // secretKey
        originalSign.append(".").append(uploadModel.bucketId)// bucketId
        if (uploadModel.customId != null) {
            originalSign.append(".").append(uploadModel.customId)// customId
        }
        if (uploadModel.expired != -1L) {
            originalSign.append(".").append(uploadModel.expired)//expired
        }
        originalSign.append(".").append(URLEncoder.encode(uploadModel.file?.name))//fileName
        originalSign.append(".").append(uploadModel.file?.length())//fileSize
        if(uploadModel.customImageInfo!=null){
            val imageInfoGson = Gson().toJson(uploadModel.customImageInfo)
            originalSign.append(".").append(imageInfoGson)//customImageInfo
        }
        return "${getCloudAccess()}.${MD5Util.md5Encode(originalSign.toString())}"
    }

    /**
     * 快速上传文件
     */
    fun getUploadFastToken(file: File,uploadModel: UploadFastModel):String{
        val originalSign = StringBuffer()
        originalSign.append(getCloudSecretKey()) // secretKey
        originalSign.append(".").append(uploadModel.bucketId)// bucketId
        if (uploadModel.customId != null) {
            originalSign.append(".").append(uploadModel.customId)// customId
        }

        originalSign.append(".").append(uploadModel.expired)//expired

        originalSign.append(".").append(URLEncoder.encode(file?.name))//fileName
        originalSign.append(".").append(file?.length())//fileSize

        originalSign.append(".").append(FileUtil.getMimeType(file!!))//mimeType
        originalSign.append(".").append(uploadModel.fileHash)//fileHash
        return "${getCloudAccess()}.${MD5Util.md5Encode(originalSign.toString())}"
    }

    /**
     * 下载文件的token
     */
    fun getDownParams(uploadModel: UploadModel):String{
        val originalSign = StringBuffer()
        originalSign.append(getCloudSecretKey()) // secretKey
        if (uploadModel.userFileId != -1L) {
            originalSign.append(".").append(uploadModel.userFileId)// userFileId
        }
        if (uploadModel.customId != null) {
            originalSign.append(".").append(uploadModel.customId)// customId
        }
        originalSign.append(".").append(uploadModel.bucketId)// bucketId

        return "${getCloudAccess()}.${MD5Util.md5Encode(originalSign.toString())}"
    }
    /**
     * 获取缩略图token
     */
    fun getVideoThumbnailToken(uploadModel: UploadModel):String{
        val originalSign = StringBuffer()
        originalSign.append(getCloudSecretKey()) // secretKey
        originalSign.append(".").append(uploadModel.bucketId)// bucketId
        if (uploadModel.customId != null) {
            originalSign.append(".").append(uploadModel.customId)// customId
        }
        if (uploadModel.expired != -1L) {
            originalSign.append(".").append(uploadModel.expired)//expired
        }
        if(uploadModel.userFileId != -1L){
            originalSign.append(".").append(uploadModel.userFileId)//userFileId
        }
        if(!uploadModel.baseName.isNullOrEmpty()){
            originalSign.append(".").append(uploadModel.baseName)//baseName
        }
        if(!uploadModel.extension.isNullOrEmpty()){
            originalSign.append(".").append(uploadModel.extension)//extension
        }
        if(!uploadModel.startTime.isNullOrEmpty()){
            originalSign.append(".").append(uploadModel.startTime)//startTime
        }
        if(!uploadModel.size.isNullOrEmpty()){
            originalSign.append(".").append(uploadModel.size)//size
        }

        if(uploadModel.asyncProcessing!=null){
            if(uploadModel.asyncProcessing!!){
                originalSign.append(".").append(uploadModel.notifyUrl)//notifyUrl
                originalSign.append(".").append(uploadModel.asyncProcessing.toString())//asyncProcessing
            }else{
                originalSign.append(".").append(uploadModel.asyncProcessing.toString())//asyncProcessing
            }
        }

        return "${getCloudAccess()}.${MD5Util.md5Encode(originalSign.toString())}"
    }

    /**
     * 获取视频上传的token
     */
    fun getUploadVideoToken(uploadModel: UploadModel):String{
        val originalSign = StringBuffer()
        originalSign.append(getCloudSecretKey()) // secretKey
        originalSign.append(".").append(uploadModel.bucketId)// bucketId
        if (uploadModel.customId != null) {
            originalSign.append(".").append(uploadModel.customId)// customId
        }
        if (uploadModel.expired != -1L) {
            originalSign.append(".").append(uploadModel.expired)//expired
        }
        originalSign.append(".").append(URLEncoder.encode(uploadModel.file?.name))//fileName
        originalSign.append(".").append(uploadModel.file?.length())//fileSize

        if(uploadModel.customVideoInfo!=null){
            originalSign.append(".").append(Gson().toJson(uploadModel.customVideoInfo))//customVideoInfo
        }
        if(uploadModel.thumbnailInfo!=null){
            originalSign.append(".").append(Gson().toJson(uploadModel.thumbnailInfo))//thumbnailInfo
        }
        if(uploadModel.asyncProcessing!=null){
            if(uploadModel.asyncProcessing!!){
                originalSign.append(".").append(uploadModel.asyncProcessing.toString())//asyncProcessing
                originalSign.append(".").append(uploadModel.notifyUrl)//notifyUrl
            }else{
                originalSign.append(".").append(uploadModel.asyncProcessing.toString())//asyncProcessing
            }

        }
        return "${getCloudAccess()}.${MD5Util.md5Encode(originalSign.toString())}"
    }

    /**
     * 获取音频上传的token
     */
    fun getUploadAudioToken(uploadModel: UploadModel):String{
        val originalSign = StringBuffer()
        originalSign.append(getCloudSecretKey()) // secretKey
        originalSign.append(".").append(uploadModel.bucketId)// bucketId
        if (uploadModel.customId != null) {
            originalSign.append(".").append(uploadModel.customId)// customId
        }
        if (uploadModel.expired != -1L) {
            originalSign.append(".").append(uploadModel.expired)//expired
        }
        originalSign.append(".").append(URLEncoder.encode(uploadModel.file?.name))//fileName
        originalSign.append(".").append(uploadModel.file?.length())//fileSize

        if(uploadModel.customAudioInfo!=null){
            originalSign.append(".").append(Gson().toJson(uploadModel.customAudioInfo))//customAudioInfo
        }
        return "${getCloudAccess()}.${MD5Util.md5Encode(originalSign.toString())}"
    }
}