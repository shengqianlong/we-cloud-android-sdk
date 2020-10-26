package cn.wecloud.storage

import cn.wecloud.storage.cloud.model.FileInfo
import cn.wecloud.storage.cloud.model.FileMediaInfo
import cn.wecloud.storage.cloud.response.FileUploadResponse
import cn.wecloud.storage.cloud.response.MediaUploadResponse
import cn.wecloud.storage.cloud.UploadUtils
import cn.wecloud.storage.cloud.model.UploadFastModel
import cn.wecloud.storage.cloud.model.UploadModel
import cn.wecloud.storage.net.helper.RequestBodyHelper
import cn.wecloud.storage.net.helper.RxSchedulersHelper
import cn.wecloud.storage.net.http.HttpObserver

internal object StorageServiceImplement {

    //上传单文件
    fun uploadSingleFile(
            cloud: Cloud,
            uploadMode: UploadModel,
            onStart: () -> Unit,
            onSuccess: (fileInfo: FileInfo) -> Unit,
            onFailure: (code: Int, msg: String) -> Unit,
            onComplete: () -> Unit
    ) = cloud.net.getService(StorageService::class.java)
            .uploadSingleFile(
                    RequestBodyHelper.createFromFileBody(uploadMode.file!!,UploadUtils.createParams(uploadMode))
            )
            .compose(RxSchedulersHelper.network())
            .subscribe(object : HttpObserver<FileUploadResponse>() {
                override fun onStart() {
                    onStart()
                }
                override fun onSuccess(t: FileUploadResponse) {
                    onSuccess(t.data)
                }
                override fun onFailure(code: Int, message: String?) {
                    onFailure.invoke(code, message ?: "")
                }
                override fun onComplete() {
                    onComplete()
                }
            })

    //上传单文件 秒传
    fun uploadSingleFileFast(
            cloud: Cloud,
            uploadMode: UploadFastModel,
            onStart: () -> Unit,
            onSuccess: (fileInfo: FileInfo?) -> Unit,
            onFailure: (code: Int, msg: String) -> Unit,
            onComplete: () -> Unit
    ) = cloud.net.getService(StorageService::class.java)
            .uploadSingleFileFast(
                    RequestBodyHelper.createJsonBody(uploadMode)
            )
            .compose(RxSchedulersHelper.network())
            .subscribe(object : HttpObserver<FileUploadResponse>() {
                override fun onStart() {
                    onStart()
                }
                override fun onSuccess(t: FileUploadResponse) {
                    onSuccess(t.data)
                }
                override fun onFailure(code: Int, message: String?) {
                    onFailure.invoke(code, message ?: "")
                }
                override fun onComplete() {
                    onComplete()
                }
            })


    /**
     * 图片上传
     */
    fun uploadSinglePicture(
            cloud: Cloud,
            uploadMode: UploadModel,
            onStart: () -> Unit,
            onSuccess: (fileInfo: FileInfo) -> Unit,
            onFailure: (code: Int, msg: String) -> Unit,
            onComplete: () -> Unit
    ) = cloud.net.getService(StorageService::class.java)
            .uploadSinglePicture(
                    RequestBodyHelper.createFromFileBody(uploadMode.file!!,UploadUtils.createParams(uploadMode))
            )
            .compose(RxSchedulersHelper.network())
            .subscribe(object : HttpObserver<FileUploadResponse>() {
                override fun onStart() {
                    onStart()
                }
                override fun onSuccess(t: FileUploadResponse) {
                    onSuccess(t.data)
                }
                override fun onFailure(code: Int, message: String?) {
                    onFailure.invoke(code, message ?: "")
                }
                override fun onComplete() {
                    onComplete()
                }
            })



    /**
     * 获取视频缩略图
     */
    fun getVideoThumbnail(
            cloud: Cloud,
            uploadMode: UploadModel,
            onStart: () -> Unit,
            onSuccess: (fileInfo: FileInfo) -> Unit,
            onFailure: (code: Int, msg: String) -> Unit,
            onComplete: () -> Unit
    ) = cloud.net.getService(StorageService::class.java)
            .getVideoThumbnail(
                    RequestBodyHelper.createFromFileBody(params = UploadUtils.createParams(uploadMode))
            )
            .compose(RxSchedulersHelper.network())
            .subscribe(object : HttpObserver<FileUploadResponse>() {
                override fun onStart() {
                    onStart()
                }
                override fun onSuccess(t: FileUploadResponse) {
                    onSuccess(t.data)
                }
                override fun onFailure(code: Int, message: String?) {
                    onFailure.invoke(code, message ?: "")
                }
                override fun onComplete() {
                    onComplete()
                }
            })




    /**
     * 媒体文件上传
     */
    fun uploadSingleMedia(
            cloud: Cloud,
            uploadMode: UploadModel,
            onStart: () -> Unit,
            onSuccess: (fileInfo: FileMediaInfo) -> Unit,
            onFailure: (code: Int, msg: String) -> Unit,
            onComplete: () -> Unit
    ) = cloud.net.getService(StorageService::class.java)
            .uploadSingleMedia(
                    RequestBodyHelper.createFromFileBody(uploadMode.file!!,UploadUtils.createParams(uploadMode))
            )
            .compose(RxSchedulersHelper.network())
            .subscribe(object : HttpObserver<MediaUploadResponse>() {
                override fun onStart() {
                    onStart()
                }
                override fun onSuccess(t: MediaUploadResponse) {
                    onSuccess(t.data)
                }
                override fun onFailure(code: Int, message: String?) {
                    onFailure.invoke(code, message ?: "")
                }
                override fun onComplete() {
                    onComplete()
                }
            })
}