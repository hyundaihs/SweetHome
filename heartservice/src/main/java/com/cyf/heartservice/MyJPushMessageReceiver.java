package com.cyf.heartservice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cyf.heartservice.activities.RepairRoomActivity;
import com.cyf.heartservice.activities.SecretaryActivity;
import com.cyf.heartservice.entity.JpushInfo;
import com.google.gson.Gson;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 */
public class MyJPushMessageReceiver extends JPushMessageReceiver {
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        String msg = notificationMessage.notificationExtras;
        JpushInfo jpushInfo = new Gson().fromJson(msg,JpushInfo.class);
        Intent intent = new Intent();
        switch (jpushInfo.getType()){
            case "gg":
                intent.setClass(context, SecretaryActivity.class);
                context.startActivity(intent);
                break;
            case "gd":
                intent.setClass(context, RepairRoomActivity.class);
                intent.putExtra("type", 1);
                context.startActivity(intent);
                break;
            case "zx":
            case "fx":
                intent.setClass(context, RepairRoomActivity.class);
                intent.putExtra("type", 2);
                context.startActivity(intent);
                break;
        }
    }
    //    @Override
//    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context,jPushMessage);
//        super.onTagOperatorResult(context, jPushMessage);
//    }
//    @Override
//    public void onCheckTagOperatorResult(Context context,JPushMessage jPushMessage){
//        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context,jPushMessage);
//        super.onCheckTagOperatorResult(context, jPushMessage);
//    }
//    @Override
//    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context,jPushMessage);
//        super.onAliasOperatorResult(context, jPushMessage);
//    }
//
//    @Override
//    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context,jPushMessage);
//        super.onMobileNumberOperatorResult(context, jPushMessage);
//    }
}
