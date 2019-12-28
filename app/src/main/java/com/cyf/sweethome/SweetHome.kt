package com.cyf.sweethome

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.cyf.sweethome.entity.HouseInfo
import com.cyf.sweethome.entity.UserInfo
import kotlin.properties.Delegates

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/17/017.
 */
class SweetHome : Application() {

    companion object {
        var userInfo: UserInfo? = null
        var houseInfo: HouseInfo? = null
        var instance: SweetHome by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }
}