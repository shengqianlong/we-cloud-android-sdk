package cn.wecloud.storage.net.helper

import com.google.gson.Gson
import cn.wecloud.storage.net.utils.FileUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object RequestBodyHelper {

    fun createJsonBody(
        any: Any?,
        contentType: String = "application/json; charset=utf-8"
    ): RequestBody =
        RequestBody.create(MediaType.parse(contentType), Gson().toJson(any))

    fun createFileBody(file: File, contentType: String = FileUtil.getMimeType(file)): RequestBody =
        RequestBody.create(MediaType.parse(contentType), file)


    //创建表单
    fun createFromFileBody(file:File? = null,params:Map<String,String?>):MultipartBody {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        if(file!=null) {
            builder.addPart(createFileBodyPart("file", file, "multipart/form-data"))
        }
        params.keys.forEach {
            builder.addPart(createPart(it,params[it]!!))
        }
        return builder.build()
    }



    //--------------创建表单 中的一个part单元-----------------------------------------------------
    fun createFileBodyPart(
        key: String,
        file: File,
        string: String = FileUtil.getMimeType(file)
    ): MultipartBody.Part =
        MultipartBody.Part.createFormData(
            key,
            file.name,
            createFileBody(file, string)
        )

    fun createPart(name: String, value: Long): MultipartBody.Part =
        MultipartBody.Part.createFormData(name, value.toString())

    fun createPart(name: String, value: Int): MultipartBody.Part =
        MultipartBody.Part.createFormData(name, value.toString())

    fun createPart(name: String, value: String): MultipartBody.Part =
            MultipartBody.Part.createFormData(name, value)
}