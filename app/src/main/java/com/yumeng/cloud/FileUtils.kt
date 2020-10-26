package com.yumeng.cloud

import android.os.Environment
import java.io.File
import java.io.IOException

object FileUtils {
    /**
     * 在SD卡上创建文件夹
     *
     * @throws IOException
     */
    @Throws(IOException::class)
    fun createSDDirectory(fileName: String): File {
        val file = File(fileName)
        if (!file.exists())
            file.mkdirs()
        return file
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    fun getSDPath(): String {
        val sdPath: String
        // 判断sd卡是否存在
        val sdCardExit = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        sdPath = if (sdCardExit) {
            // 获取根目录
            Environment.getExternalStorageDirectory().toString()
        } else {
            Environment.getDataDirectory().toString()
        }
        return sdPath
    }
}