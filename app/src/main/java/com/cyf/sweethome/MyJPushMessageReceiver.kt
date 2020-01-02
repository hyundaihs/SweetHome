package com.cyf.sweethome

import android.content.Context
import android.content.Intent
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.android.shuizu.myutillibrary.E
import com.cyf.sweethome.activities.CheckRoomLogActivity
import com.cyf.sweethome.activities.SecretaryActivity
import com.cyf.sweethome.activities.WorkOrderListActivity
import com.cyf.sweethome.entity.JpushInfo
import com.google.gson.Gson
import org.jetbrains.anko.toast

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/15/015.
 */
class MyJPushMessageReceiver : JPushMessageReceiver() {
    override fun onNotifyMessageOpened(
        context: Context,
        notificationMessage: NotificationMessage
    ) {
        super.onNotifyMessageOpened(context, notificationMessage)
        val msg = notificationMessage.notificationExtras
        val jpushInfo = Gson().fromJson(msg, JpushInfo::class.java)
        val intent = Intent()
        when (jpushInfo.type) {
            "yf" -> {
                intent.setClass(context, CheckRoomLogActivity::class.java)
                context.startActivity(intent)
            }
            "gg" -> {

                intent.setClass(context, SecretaryActivity::class.java)
                context.startActivity(intent)
            }
            "zyz" -> {

            }
            "dj" -> {

            }
            "gd" -> {
                intent.setClass(context, WorkOrderListActivity::class.java)
                intent.putExtra("page", 0)
                context.startActivity(intent)
            }
        }
    }
}