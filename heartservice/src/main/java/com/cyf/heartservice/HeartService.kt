package com.cyf.heartservice

import android.app.Application
import cn.jpush.android.api.JPushInterface

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/17/017.
 */
class HeartService : Application() {
    override fun onCreate() {
        super.onCreate()
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }
}