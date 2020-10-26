package cn.wecloud.storage

import android.content.Context
import android.content.ContextWrapper

/**
 * Created by Chauncey on 2018/12/25 11:40.
 */
class AppContextWrapper private constructor(base: Context) : ContextWrapper(base) {

    companion object {

        private lateinit var instance: AppContextWrapper

        fun init(context: Context): Context {
            instance =
                    AppContextWrapper(context)
            return instance.baseContext
        }

        fun getApplicationContext(): Context = instance.applicationContext

        fun getBaseContext(): Context = instance.baseContext

    }


}
