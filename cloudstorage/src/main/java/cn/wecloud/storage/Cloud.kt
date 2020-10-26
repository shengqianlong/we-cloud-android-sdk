package cn.wecloud.storage

import android.content.Context
import android.util.Log
import cn.wecloud.storage.cloud.FileSHAUtils
import cn.wecloud.storage.cloud.FileUtil
import cn.wecloud.storage.cloud.UploadUtils
import cn.wecloud.storage.cloud.callback.OnFileUploadCallback
import cn.wecloud.storage.cloud.callback.OnMediaFileUploadCallback
import cn.wecloud.storage.cloud.down.DownloadManager
import cn.wecloud.storage.cloud.interceptor.StorageInterceptor
import cn.wecloud.storage.cloud.model.*
import cn.wecloud.storage.net.http.Net
import cn.wecloud.storage.net.utils.ApplicationUtil
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.TimeUnit

class Cloud constructor(builder: Builder) {


    companion object{
        private lateinit var cloud: Cloud

        fun init(){
            cloud = Cloud(Builder())
        }
        fun getNet():Cloud{
            return cloud
        }
        fun initContext(context: Context){
            AppContextWrapper.init(context)
        }
    }
    internal val net: Net

    init {
        net = Net.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addInterceptor(
                        StorageInterceptor(
                                ApplicationUtil.getMetaData(UploadUtils.CLOUD_USER_ID_VALUE)
                                        ?: ""
                        )
                )
                .connectTimeout(builder.connectTimeout, builder.connectTimeUnit)
                .readTimeout(builder.readTimeout, builder.readTimeUnit)
                .writeTimeout(builder.writeTimeout, builder.writeTimeUnit)
                .create()
    }

    class Builder {
        companion object {
            private const val CONNECT_TIMEOUT = 10L
            private val CONNECT_TIME_UNIT = TimeUnit.SECONDS

            private const val READ_TIMEOUT = 10L
            private val READ_TIME_UNIT = TimeUnit.SECONDS

            private const val WRITE_TIMEOUT = 10L
            private val WRITE_TIME_UNIT = TimeUnit.SECONDS
        }

        internal var connectTimeout: Long =
                CONNECT_TIMEOUT
        internal var connectTimeUnit =
                CONNECT_TIME_UNIT

        internal var readTimeout =
                READ_TIMEOUT
        internal var readTimeUnit =
                READ_TIME_UNIT

        internal var writeTimeout =
                WRITE_TIMEOUT
        internal var writeTimeUnit =
                WRITE_TIME_UNIT

        fun connectTimeout(connectTimeout: Long) = this.apply {
            this.connectTimeout = connectTimeout
        }

        fun connectTimeUnit(unit: TimeUnit) = this.apply {
            connectTimeUnit = unit
        }

        fun readTimeout(readTimeout: Long) = this.apply {
            this.readTimeout = readTimeout
        }

        fun readTimeUnit(unit: TimeUnit) = this.apply {
            readTimeUnit = unit
        }

        fun writeTimeout(writeTimeout: Long) = this.apply {
            this.writeTimeout = writeTimeout
        }

        fun writeTimeUnit(unit: TimeUnit) = this.apply {
            writeTimeUnit = unit
        }


    }
//-------------------------------------------------------------------------------------------------------

    /**
     * 上传单个文件
     * 如果只是单纯的传  onFileUploadResponse 后的参数都用默认就行
     * param expire -1 ：不过期   0 ：次日0点过期   >0 多少日后0点过期
     * customId  自定义的文件唯一标识  不用的话 设置为null就可以
     * cover true：如果customId存在直接覆盖，false 不覆盖
     * kotlin  不用到可以不传，java  设置成 -1L null false  就行了
     */
    fun doUploadFile(
            file: File,
            onFileUploadResponse: OnFileUploadCallback,
            expired: Long? = -1,
            customId: String? = null,
            cover: Boolean? = false
    ) {
        if (file == null || !file.isFile || !file.exists() || file.length() == 0L) {//为null ，不是文件，不存在，大小为0
            return
        }
        val bucketId = UploadUtils.getCloudUserIdValue() ?: "-1"
        val uploadModel = UploadModel(
                file = file, bucketId = bucketId.toLong(),
                expired = expired, customId = customId,
                cover = cover, uploadToken = "empty")
        val uploadModelEnd = UploadModel(
                file = uploadModel.file, bucketId = uploadModel.bucketId,
                expired = uploadModel.expired, customId = uploadModel.customId,
                cover = uploadModel.cover, uploadToken = UploadUtils.getUploadToken(uploadModel))

        StorageServiceImplement.uploadSingleFile(
                this,
                uploadModelEnd,
                onStart = {
                    onFileUploadResponse.onStart()
                },
                onSuccess = {
                    onFileUploadResponse.onSuccess(it)
                },
                onFailure = { code, msg ->
                    onFileUploadResponse.onFailure(code, msg)
                },
                onComplete = {
                    onFileUploadResponse.onComplete()
                }
        )
    }


    /**
     * 单个文件秒传
     * 如果只是单纯的传  onFileUploadResponse 后的参数都用默认就行
     * param expire -1 ：不过期   0 ：次日0点过期   >0 多少日后0点过期
     * customId  自定义的文件唯一标识  不用的话 设置为null就可以
     * cover true：如果customId存在直接覆盖，false 不覆盖
     * kotlin  不用到可以不传，java  设置成 -1L null false  就行了
     */
    fun doUploadFileFast(
            file: File,
            onFileUploadResponse: OnFileUploadCallback,
            expired: Long? = -1,
            customId: String? = null,
            cover:Boolean?=false
    ) {
        if (file == null || !file.isFile || !file.exists() || file.length() == 0L) {//为null ，不是文件，不存在，大小为0
            return
        }
        val bucketId = UploadUtils.getCloudUserIdValue() ?: "-1"
         val uploadModel = UploadFastModel(
                bucketId = bucketId.toLong(),
                expired = expired, customId = customId,
                fileHash = FileSHAUtils.md5HashCode32(FileInputStream(file)),
                fileName = file?.name, fileSize = file?.length().toString(),
                mimeType = FileUtil.getMimeType(file),
                uploadToken = "empty")
        val uploadModelEnd = UploadFastModel(
                bucketId = uploadModel.bucketId,
                expired = uploadModel.expired, customId = uploadModel.customId,
                fileHash = uploadModel.fileHash,
                fileName = uploadModel.fileName, fileSize = uploadModel.fileSize,
                mimeType = uploadModel.mimeType,
                uploadToken = UploadUtils.getUploadFastToken(file, uploadModel))

        StorageServiceImplement.uploadSingleFileFast(
                this,
                uploadModelEnd,
                onStart = {
                    onFileUploadResponse.onStart()
                },
                onSuccess = {
                    if(it!=null) {
                        onFileUploadResponse.onSuccess(it)
                    }else{
                        doUploadFile(file,onFileUploadResponse,expired,customId,cover)
                    }
                },
                onFailure = { code, msg ->
                    onFileUploadResponse.onFailure(code, msg)
                },
                onComplete = {
                    onFileUploadResponse.onComplete()
                }
        )
    }


    /**
     * 上传单个图片
     * 如果只是单纯的传  onFileUploadResponse 后的参数都用默认就行
     * param expire -1 ：不过期   0 ：次日0点过期   >0 多少日后0点过期
     * customId  自定义的文件唯一标识  不用的话 设置为null就可以
     * cover true：如果customId存在直接覆盖，false 不覆盖
     * customImageInfo: 图片的一些参数,不要可以传 null
     * kotlin  不用到可以不传，java  设置成 -1L null false  就行了
     */
    fun doUploadPicture(
            file: File,
            onFileUploadResponse: OnFileUploadCallback,
            optionImage: CustomImageInfo? = null,
            expired: Long? = -1,
            customId: String? = null,
            cover: Boolean? = false
    ) {
        if (file == null || !file.isFile || !file.exists() || file.length() == 0L) {//为null ，不是文件，不存在，大小为0
            return
        }
        val bucketId = UploadUtils.getCloudUserIdValue() ?: "-1"
        val uploadModel = UploadModel(
                file = file, bucketId = bucketId.toLong(),
                expired = expired, customId = customId,
                customImageInfo = optionImage,
                cover = cover, uploadToken = "empty")
        val uploadModelEnd = UploadModel(
                file = uploadModel.file, bucketId = uploadModel.bucketId,
                expired = uploadModel.expired, customId = uploadModel.customId,
                cover = uploadModel.cover,
                customImageInfo = uploadModel.customImageInfo,
                uploadToken = UploadUtils.getUploadToken(uploadModel))

        StorageServiceImplement.uploadSinglePicture(
                this,
                uploadModelEnd,
                onStart = {
                    onFileUploadResponse.onStart()
                },
                onSuccess = {
                    onFileUploadResponse.onSuccess(it)
                },
                onFailure = { code, msg ->
                    onFileUploadResponse.onFailure(code, msg)
                },
                onComplete = {
                    onFileUploadResponse.onComplete()
                }
        )
    }

    /**
     * 下载文件
     *
     */
    fun downFile(customId: String? = null, userFileId: Long, dirPath: String, fileName: String, onDownloadListener: DownloadManager.OnDownloadListener) {
        val bucketId = UploadUtils.getCloudUserIdValue() ?: "-1"
        val uploadModel = UploadModel(
                customId = customId,
                userFileId = userFileId,
                bucketId = bucketId.toLong(),
                uploadToken = "empty"
        )
        val uploadModelEnd = UploadModel(
                customId = uploadModel.customId,
                userFileId = uploadModel.userFileId,
                bucketId = uploadModel.bucketId,
                uploadToken = UploadUtils.getDownParams(uploadModel)
        )
        val downUrl = StringBuffer()
        downUrl.append("${BuildConfig.BASE_URL}download?")
        if (uploadModelEnd.customId.isNullOrEmpty()) {
            downUrl.append("userFileId=${uploadModelEnd.userFileId}")
        } else {
            downUrl.append("customId=${uploadModelEnd.customId}")
            downUrl.append("&userFileId=${uploadModelEnd.userFileId}")
        }

        downUrl.append("&bucketId=${uploadModelEnd.bucketId}")
        downUrl.append("&downloadToken=${uploadModelEnd.uploadToken}")
        DownloadManager.downLoadFile(downUrl.toString(), dirPath, fileName, onDownloadListener)
    }

    /**
     * 下载图片
     */
    fun downImageFile(customId: String? = null, userFileId: Long, dirPath: String, fileName: String, optionImage: CustomImageInfo? = null, onDownloadListener: DownloadManager.OnDownloadListener) {
        val bucketId = UploadUtils.getCloudUserIdValue() ?: "-1"
        val uploadModel = UploadModel(
                customId = customId,
                userFileId = userFileId,
                bucketId = bucketId.toLong(),
                uploadToken = "empty"
        )
        val uploadModelEnd = UploadModel(
                customId = uploadModel.customId,
                userFileId = uploadModel.userFileId,
                bucketId = uploadModel.bucketId,
                uploadToken = UploadUtils.getDownParams(uploadModel)
        )

        val downUrl = StringBuffer()
        downUrl.append("${BuildConfig.BASE_URL}getCustomImage?")
        if (uploadModelEnd.customId.isNullOrEmpty()) {
            downUrl.append("userFileId=${uploadModelEnd.userFileId}")
        } else {
            downUrl.append("customId=${uploadModelEnd.customId}")
            downUrl.append("&userFileId=${uploadModelEnd.userFileId}")
        }
        downUrl.append("&bucketId=${uploadModelEnd.bucketId}")
        downUrl.append("&downloadToken=${uploadModelEnd.uploadToken}")
        if (optionImage != null) {
            if (optionImage.width != null && optionImage.width != 0) {
                downUrl.append("&width=${optionImage.width}")
            }
            if (optionImage.height != null && optionImage.height != 0) {
                downUrl.append("&height=${optionImage.height}")
            }
            if (optionImage.quality != null && optionImage.quality != 0f) {
                downUrl.append("&quality=${optionImage.quality}")
            }
            if (optionImage.rotate != null && optionImage.rotate != 0.0) {
                downUrl.append("&rotate=${optionImage.rotate}")
            }
            if (optionImage.scale != null && optionImage.scale != 0.0) {
                downUrl.append("&scale=${optionImage.scale}")
            }
        }
        DownloadManager.downLoadFile(downUrl.toString(), dirPath, fileName, onDownloadListener)
    }


    //--------------------------以下暂不开放-----------------------------------------------
    private fun getVideoThumbnail(
            userFileId: Long,
            baseName: String,
            extension: String,
            size: String,
            asyncProcessing: Boolean = false,
            notifyUrl: String? = null,
            onFileUploadResponse: OnFileUploadCallback,
            startTime: String? = null,
            expired: Long? = -1,
            customId: String? = null
    ) {
        if (baseName.isNullOrEmpty() || extension.isNullOrEmpty() || size.isNullOrEmpty()) {
            //非空参数 为空
            return
        }
        if (asyncProcessing) {
            if (notifyUrl == null) {
                //回调地址为空
                return
            }
        }
        val bucketId = UploadUtils.getCloudUserIdValue() ?: "-1"
        val uploadModel = UploadModel(
                userFileId = userFileId, bucketId = bucketId.toLong(),
                baseName = baseName, extension = extension,
                size = size, asyncProcessing = asyncProcessing,
                notifyUrl = notifyUrl, startTime = startTime,
                expired = expired, customId = customId,
                uploadToken = "empty")
        val uploadModelEnd = UploadModel(
                userFileId = uploadModel.userFileId, bucketId = uploadModel.bucketId,
                baseName = uploadModel.baseName, extension = uploadModel.extension,
                size = uploadModel.size, asyncProcessing = uploadModel.asyncProcessing,
                notifyUrl = uploadModel.notifyUrl, startTime = uploadModel.startTime,
                expired = uploadModel.expired, customId = uploadModel.customId,
                uploadToken = UploadUtils.getVideoThumbnailToken(uploadModel))

        StorageServiceImplement.getVideoThumbnail(
                this,
                uploadModelEnd,
                onStart = {
                    onFileUploadResponse.onStart()
                },
                onSuccess = {
                    onFileUploadResponse.onSuccess(it)
                },
                onFailure = { code, msg ->
                    onFileUploadResponse.onFailure(code, msg)
                },
                onComplete = {
                    onFileUploadResponse.onComplete()
                }
        )
    }

    /**
     * 音频上传
     * 如果只是单纯的传  onFileUploadResponse 后的参数都用默认就行
     * param expire -1 ：不过期   0 ：次日0点过期   >0 多少日后0点过期
     * customId  自定义的文件唯一标识  不用的话 设置为null就可以
     * cover true：如果customId存在直接覆盖，false 不覆盖
     * customAudioInfo: 音频的一些参数,不要可以传 null
     * kotlin  不用到可以不传，java  设置成 -1L null false  就行了
     */
    private fun doUploadAudio(
            file: File,
            onFileUploadResponse: OnMediaFileUploadCallback,
            customAudioInfo: CustomAudioInfo? = null,
            expired: Long? = -1,
            customId: String? = null,
            cover: Boolean? = false
    ) {
        val bucketId = UploadUtils.getCloudUserIdValue() ?: "-1"
        val uploadModel = UploadModel(
                file = file, bucketId = bucketId.toLong(),
                expired = expired, customId = customId,
                customAudioInfo = customAudioInfo,
                cover = cover, uploadToken = "empty")
        val uploadModelEnd = UploadModel(
                file = uploadModel.file,
                bucketId = uploadModel.bucketId,
                expired = uploadModel.expired,
                customId = uploadModel.customId,
                cover = uploadModel.cover,
                customAudioInfo = uploadModel.customAudioInfo,
                uploadToken = UploadUtils.getUploadAudioToken(uploadModel))

        StorageServiceImplement.uploadSingleMedia(
                this,
                uploadModelEnd,
                onStart = {
                    onFileUploadResponse.onStart()
                },
                onSuccess = {
                    onFileUploadResponse.onSuccess(it)
                },
                onFailure = { code, msg ->
                    onFileUploadResponse.onFailure(code, msg)
                },
                onComplete = {
                    onFileUploadResponse.onComplete()
                }
        )
    }


    /**
     * 视频上传
     * 如果只是单纯的传  onFileUploadResponse 后的参数都用默认就行
     * param expire -1 ：不过期   0 ：次日0点过期   >0 多少日后0点过期
     * customId  自定义的文件唯一标识  不用的话 设置为null就可以
     * cover true：如果customId存在直接覆盖，false 不覆盖
     * customVideoInfo: 视频的一些参数,不要可以传 null
     * thumbnailInfo:视频生成缩略图信息
     *
     * asyncProcessing：是否异步处理视频
     * notifyUrl ：如果asyncProcessing 为true，为必填
     * kotlin  不用到可以不传，java  设置成 -1L null false  就行了
     */
    private fun doUploadVideo(
            file: File,
            onFileUploadResponse: OnMediaFileUploadCallback,
            customVideoInfo: CustomVideoInfo? = null,
            thumbnailInfo: ThumbnailInfo? = null,
            asyncProcessing: Boolean? = false,
            notifyUrl: String? = null,
            expired: Long? = -1,
            customId: String? = null,
            cover: Boolean? = false
    ) {
        if (asyncProcessing!!) {
            if (notifyUrl.isNullOrEmpty()) {
                return
            }
        }
        val bucketId = UploadUtils.getCloudUserIdValue() ?: "-1"
        val uploadModel = UploadModel(
                file = file, bucketId = bucketId.toLong(),
                expired = expired, customId = customId,
                customVideoInfo = customVideoInfo,
                thumbnailInfo = thumbnailInfo,
                asyncProcessing = asyncProcessing,
                notifyUrl = notifyUrl,
                cover = cover, uploadToken = "empty")
        val uploadModelEnd = UploadModel(
                file = uploadModel.file,
                bucketId = uploadModel.bucketId,
                expired = uploadModel.expired,
                customId = uploadModel.customId,
                cover = uploadModel.cover,
                customVideoInfo = uploadModel.customVideoInfo,
                thumbnailInfo = uploadModel.thumbnailInfo,
                asyncProcessing = uploadModel.asyncProcessing,
                notifyUrl = uploadModel.notifyUrl,
                uploadToken = UploadUtils.getUploadVideoToken(uploadModel))

        StorageServiceImplement.uploadSingleMedia(
                this,
                uploadModelEnd,
                onStart = {
                    onFileUploadResponse.onStart()
                },
                onSuccess = {
                    onFileUploadResponse.onSuccess(it)
                },
                onFailure = { code, msg ->
                    onFileUploadResponse.onFailure(code, msg)
                },
                onComplete = {
                    onFileUploadResponse.onComplete()
                }
        )
    }

}