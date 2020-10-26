package cn.wecloud.storage.cloud.callback

import cn.wecloud.storage.cloud.model.FileInfo


/**
 * Created by Chauncey on 2019-04-24 14:40.
 *
 */
interface OnFileUploadCallback {

    fun onStart() {}

    fun onSuccess(fileInfo: FileInfo?)

    fun onFailure(code: Int, message: String) {}

    fun onComplete() {}

}