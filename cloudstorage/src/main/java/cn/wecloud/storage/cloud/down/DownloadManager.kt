package cn.wecloud.storage.cloud.down

import android.app.Activity
import cn.wecloud.storage.BuildConfig
import cn.wecloud.storage.StorageService
import cn.wecloud.storage.cloud.UploadUtils
import cn.wecloud.storage.cloud.interceptor.DownInterceptor
import cn.wecloud.storage.cloud.interceptor.StorageInterceptor
import cn.wecloud.storage.net.utils.ApplicationUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.*
import java.util.*

/**
 * Created by Chauncey on 2018/6/8 11:46.
 * 下载类
 */
object DownloadManager {

    private fun writeResponseBodyToDisk(body: ResponseBody, dirPath: String, fileName: String, onDownloadListener: OnDownloadListener?): File? {

        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        val file = File(dirPath + File.separator + fileName)
        if (file.exists()) {
            onDownloadListener?.onDownloaded(file)
            return file
        } else {
            val dirFile = File(dirPath)
            dirFile.mkdirs()
            file.createNewFile()
        }

        try {
            val fileReader = ByteArray(4096)

            val fileSize = body.contentLength()
            var fileSizeDownloaded: Long = 0

            inputStream = body.byteStream()
            outputStream = FileOutputStream(file!!)
            var read :Int
            while (inputStream?.read(fileReader).apply {
                        read = this ?: -1
                    } != -1) {

                if (read == -1) {
                    break
                }

                outputStream.write(fileReader, 0, read)

                fileSizeDownloaded += read.toLong()
//                runOnUiThread {
                    onDownloadListener?.onProgress(fileSizeDownloaded, fileSize)
//                }
            }

            outputStream.flush()
            return file
        } catch (e: IOException) {
            return null
        } finally {
            inputStream?.close()
            outputStream?.close()
        }

    }

    fun downLoadFile(downloadUrl: String?, dir: String?, fileName: String?, onDownloadListener: OnDownloadListener? = null): DownloadObserver<File?>? {

        val path = dir + File.separator + fileName
        val file = File(path)
        if (file.exists()) {
            onDownloadListener?.onDownloaded(file)
            return null
        }

        val build = OkHttpClient.Builder()
        build.addInterceptor(DownInterceptor())

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(build.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val netService = retrofit.create(StorageService::class.java)

        val observer = object : DownloadObserver<File?>() {

            override fun onStart() {
                super.onStart()
                onDownloadListener?.onStart()

            }

            override fun onComplete() {

            }

            override fun onNext(t: File) {
                onDownloadListener?.onSuccess(t)
            }

            override fun onError(e: Throwable) {
                file.delete()
                onDownloadListener?.onFailure(e)
            }

            override fun cancel() {
                super.cancel()
                val f = File(path)
                if (f.exists()) {
                    f.delete()
                }
            }
        }

        //这边downloadUrl 在后面的请求中用的@url  全链接
        netService.downloadFile(downloadUrl)
                .subscribeOn(Schedulers.io())
                .map { t ->
                    writeResponseBodyToDisk(t,dir!!
                            , fileName
                            ?: UUID.randomUUID().toString(), onDownloadListener)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)

        return observer
    }

    interface OnDownloadListener {
        fun onStart()

        fun onProgress(fileDownloadedSize: Long, total: Long)

        fun onSuccess(file: File)

        fun onFailure(e: Throwable)

        fun onDownloaded(file: File)
    }
}
