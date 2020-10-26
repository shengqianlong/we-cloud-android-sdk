package com.yumeng.cloud

import android.app.Application
import cn.wecloud.storage.Cloud


class MyApplication:Application(){

    override fun onCreate() {
        super.onCreate()
        Cloud.initContext(this)
    }

}