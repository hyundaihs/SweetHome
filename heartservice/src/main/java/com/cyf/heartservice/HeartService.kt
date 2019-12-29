package com.cyf.heartservice

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.android.shuizu.myutillibrary.initVersionUpdate
import com.cyf.heartservice.entity.UserInfo
import kotlin.properties.Delegates

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/17/017.
 */
class HeartService : Application() {

    companion object {
        var userInfo: UserInfo? = null
        var instance: HeartService by Delegates.notNull()
        val CHOOSE_COMMUNITY_RESULT = 140
        val CHOOSE_BUILDING_RESULT = 141
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        initVersionUpdate(this)
    }
}