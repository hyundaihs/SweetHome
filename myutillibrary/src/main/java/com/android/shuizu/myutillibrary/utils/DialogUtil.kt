package com.android.shuizu.myutillibrary.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.dou361.dialogui.DialogUIUtils
import com.dou361.dialogui.listener.DialogUIListener


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/3/31/031.
 */

fun Any.getLoadigDialog(context: Context): Dialog {
    return DialogUIUtils.showLoadingVertical(context, "加载中...", false, false, true).show()
}

fun Any.getMessageDialog(
    context: Context,
    meesage: String,
    listener: DialogUIListener? = null
): Dialog {
    return DialogUIUtils.showAlert(context as Activity, "提示", meesage, listener).show()
}

fun Any.getSuccessDialog(
    context: Context,
    meesage: String,
    listener: DialogUIListener? = null
): Dialog {
    return DialogUIUtils.showAlert(context as Activity, "成功", meesage, listener).show()
}

fun Any.getErrorDialog(
    context: Context,
    error: String,
    listener: DialogUIListener? = null
): Dialog {
    return DialogUIUtils.showAlert(context as Activity, "出错", error, listener).show()
}

fun Any.getLoginErrDialog(context: Context, listener: DialogUIListener? = null): Dialog {
    return DialogUIUtils.showAlert(context as Activity, "出错", "需要重新验证登陆", listener).show()
}