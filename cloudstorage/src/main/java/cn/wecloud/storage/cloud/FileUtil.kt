package cn.wecloud.storage.cloud

import android.webkit.MimeTypeMap
import java.io.File
import java.util.*

/**
 * Created by Chauncey on 2019-04-24 11:31.
 * 文件fileUtils
 * 获取文件类型
 */
object FileUtil {

    private const val DATA_TYPE_ALL = "*/*"

    fun getFileType(file: File?): String? {
        if (file == null || !file.exists() || file.isDirectory) {
            return null
        }
        return getFileType(file.name)
    }

    fun getFileType(fileName: String?): String? {
        if (fileName.isNullOrBlank() || fileName.endsWith(".")) {
            return null
        }
        val index = fileName.lastIndexOf(".")
        return if (index != -1) {
            fileName.substring(index + 1).toLowerCase(Locale.US)
        } else {
            null
        }

    }

    fun getMimeType(file: File): String {
        val suffix = getFileType(file) ?: return DATA_TYPE_ALL
        val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix)
        return if (type.isNullOrBlank()) {
            DATA_TYPE_ALL
        } else {
            type
        }
    }



}