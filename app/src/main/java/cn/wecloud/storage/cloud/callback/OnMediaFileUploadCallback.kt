package cn.wecloud.storage.cloud.callback

import cn.wecloud.storage.cloud.model.FileInfo
import cn.wecloud.storage.cloud.model.FileMediaInfo


/**
 * Created by Chauncey on 2019-04-24 14:40.
 *
 */
interface OnMediaFileUploadCallback {

    fun onStart() {}

    fun onSuccess(fileInfo: FileMediaInfo)

    fun onFailure(code: Int, message: String) {}

    fun onComplete() {}

}