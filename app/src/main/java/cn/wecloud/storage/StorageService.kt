package cn.wecloud.storage

import cn.wecloud.storage.cloud.response.FileUploadResponse
import cn.wecloud.storage.cloud.response.MediaUploadResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*


internal interface StorageService{

//    @Multipart
    @POST("uploadSingleFile")
    fun uploadSingleFile(
            @Body fileBody: MultipartBody
    ): Observable<FileUploadResponse>

    @POST("fastUpload")
    fun uploadSingleFileFast(
            @Body fileBody: RequestBody
    ): Observable<FileUploadResponse>
    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String?): Observable<ResponseBody>

    @POST("uploadImageFile")
    fun uploadSinglePicture(
            @Body fileBody: MultipartBody
    ): Observable<FileUploadResponse>

    @POST("uploadMediaFile")
    fun uploadSingleMedia(
            @Body fileBody: MultipartBody
    ): Observable<MediaUploadResponse>

    @POST("createThumbnail")
    fun getVideoThumbnail(
            @Body fileBody: MultipartBody
    ): Observable<FileUploadResponse>


}