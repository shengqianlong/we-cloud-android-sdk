package cn.wecloud.storage.net.exception

import android.util.SparseArray

/**
 * Created by Chauncey on 2019-05-14 10:44.
 *
 */
object ExceptionManager {

    private val sparseArray = SparseArray<ExceptionHandler>()

    fun register(code: Int, exceptionHandler: ExceptionHandler) {
        sparseArray.put(code, exceptionHandler)
    }

    fun getMessage(code: Int): String? = sparseArray[code]?.getMessage(code)

    fun unRegister(code: Int) {
        sparseArray.remove(code)
    }

}