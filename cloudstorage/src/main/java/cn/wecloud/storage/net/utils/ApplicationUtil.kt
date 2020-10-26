package cn.wecloud.storage.net.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import cn.wecloud.storage.AppContextWrapper

/**
 * Created by Chauncey on 2019/3/3 10:17.
 * 获取使用者在 AndroidManifest 中填写的 Meta 信息
 */
object ApplicationUtil {
    /**
     * 获取使用者在 AndroidManifest 中填写的 Meta 信息
     *
     *
     * @param context [Context]
     * @param key [String]
     */
    fun getMetaData(key: String): String? {
        val packageManager = AppContextWrapper.getApplicationContext().packageManager
        val applicationInfo: ApplicationInfo?
        try {
            applicationInfo = packageManager.getApplicationInfo(
                    AppContextWrapper.getApplicationContext().packageName, PackageManager.GET_META_DATA
            )
            applicationInfo?.metaData?.let {
                if (it.containsKey(key)) {
                    return it.getString(key)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
