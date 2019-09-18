package com.cyf.heartservice.entity

import android.text.TextUtils
import android.util.Log
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.google.gson.Gson
import java.security.MessageDigest

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/8/21/021.
 */
const val TEST_DEVICE_ID = "TR-005D16660016402020E42DE4"

const val ROOT_URL = "http://s.yshdszx.com"
const val INTERFACE_INDEX = "/apiyg.php/Index/"
const val FROM = "/from/android"
const val KEY_STR = "/keystr/"
const val AREA = "$ROOT_URL/static/pca.json"

fun String.getImageUrl(): String {
    return if (this.contains("http")) this else ROOT_URL + "/" + this
}

fun String.getInterface(map: Map<String, Any>? = null): String {
    var jsonStr = ""
    if(map != null){
        jsonStr = Gson().toJson(map)
    }
    val keyStr = getKeyStr(jsonStr, this)
    Log.d("md",keyStr)
    return ROOT_URL + INTERFACE_INDEX + this + FROM + KEY_STR + keyStr
}

fun getKeyStr(jsonStr: String, inter: String): String {
    return md5(jsonStr + "nimdaae" + inter + CalendarUtil(System.currentTimeMillis()).format(CalendarUtil.YYYYMMDD))
}

private fun md5(string: String): String {
    if (TextUtils.isEmpty(string)) {
        return ""
    }
    val md5: MessageDigest = MessageDigest.getInstance("MD5")
    val bytes = md5.digest(string.toByteArray())
    var result = ""
    val c = 0xff
    for (i in bytes.indices) {
        var temp = Integer.toHexString(c and bytes[i].toInt())
        if (temp.length == 1) {
            temp = "0$temp"
        }
        result += temp
    }
    return result
}

const val UPLOADFILE = "uploadfile"//文件上传
const val LOGIN = "login"//登陆
const val SENDVERF = "sendverf"//发送短信验证码
const val GDYFTZLISTS = "gdyftzlists"//报事报修、验房通知列表
const val YFJLINFO = "yfjlinfo"//验房记录详情
const val BSBXINFO = "bsbxinfo"//报事保修详情
const val SETBSBX = "setbsbx"//设置报事报修状态
const val LXRLISTS = "lxrlists"//联系人列表
const val SETYD = "setyd"//设为已读
const val XXZX = "xxzx"//消息中心
const val USERINFO = "userinfo"//用户信息

