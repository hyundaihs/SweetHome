package com.cyf.sweethome

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.cyf.sweethome.entity.UserInfo

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/17/017.
 */
class SweetHome : Application() {

    companion object {
        var userInfo: UserInfo? = null
    }

    override fun onCreate() {
        super.onCreate()
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }
}