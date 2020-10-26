package cn.wecloud.storage.net.exception

import android.util.Log
import org.json.JSONObject
import retrofit2.HttpException

/**
 * Created by Chauncey on 2019-05-14 11:51.
 * http 错误code 判断
 */
object HttpExceptionHandler  {

    fun handle(
        e: HttpException
    ): Pair<Int, String?> {
        val response = e.response()
        val errorBody = response.errorBody()
        var code = response.code()
        var errorString = errorBody?.string()
        var errorContent = ""

        if(!errorString.isNullOrEmpty()){
            val jsonObject = JSONObject(errorString)
            val errorCode = if(jsonObject.has("code"))jsonObject.get("code") as Int else -1
            val errorMsg = jsonObject.get("msg")?.toString()
            if(errorCode!=-1){
                code = errorCode
            }
            errorContent = errorMsg?:"未知错误"
        }
        Log.e("weCloud_storage","errorString:$errorString")
        return Pair(
            code,ExceptionManager.getMessage(
                code
        ) ?: errorContent
        )
    }

}
