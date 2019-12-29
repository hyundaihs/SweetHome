package com.cyf.sweethome.entity

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
const val INTERFACE_INDEX = "/api.php/Index/"
const val FROM = "/from/android"
const val KEY_STR = "/keystr/"
const val AREA = "$ROOT_URL/static/pca.json"

fun String.getImageUrl(): String {
    return if (this.contains("http")) this else ROOT_URL + "/" + this
}

fun String.getInterface(map: Map<String, Any>? = null): String {
    var jsonStr = ""
    if (map != null) {
        jsonStr = Gson().toJson(map)
    }
    val keyStr = getKeyStr(jsonStr, this)
    Log.d("md", keyStr)
    return ROOT_URL + INTERFACE_INDEX + this + FROM + KEY_STR + keyStr
}

fun getKeyStr(jsonStr: String, inter: String): String {
    return md5(
        jsonStr + "nimdaae" + inter + CalendarUtil(System.currentTimeMillis()).format(
            CalendarUtil.YYYYMMDD
        )
    )
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
const val YFJLLISTS = "yfjllists"//验方记录列表
const val YFJLINFO = "yfjlinfo"//验房记录详情
const val TJYFJL = "tjyfjl"//提交验房记录
const val BSBXFLLISTS = "bsbxfllists"//报事保修分类列表
const val WDLXR = "wdlxr"//我的联系人列表
const val TJBSBX = "tjbsbx"//提交报事保修
const val GDSL = "gdsl"//工单数量
const val BSBXLISTS = "bsbxlists"//报事保修列表
const val BSBXINFO = "bsbxinfo"//报事保修详情
const val USERINFO = "userinfo"//用户详情
const val WDFWLB = "wdfwlb"//我的房屋列表
const val DQFWXX = "dqfwxx"//当前房屋信息
const val SETDQFW = "setdqfw"//设置当前房屋
const val BSBXPJ = "bsbxpj"//报事报修评价
const val DJSQZT = "djsqzt"//党员申请状态
const val DJSQ = "djsq"//党员申请
const val DJSTYPE = "djstype"//党建信息类别
const val DJLISTS = "djlists"//党建信息列表
const val DJINFO = "djinfo"//党建信息详情
const val NRPLLISTS = "nrpllists"//党建信息评论列表
const val NRPLADD = "nrpladd"//发送评论
const val DJBANNER = "djbanner"//党建banner图
const val HDLISTS = "hdlists"//活动列表
const val HDINFO = "hdinfo"//活动 详情
const val HDBM = "hdbm"//活动 报名
const val HDBANNER = "hdbanner"//活动banner
const val TJBGJL = "tjbgjl"//体检列表
const val TJBGINFO = "tjbginfo"//体检详情
const val TJJSBBM = "tjjsbbm"//家事帮帮忙提交
const val TJYJFK = "tjyjfk"//意见反馈
const val TJBYX = "tjbyx"//表扬物业
const val WDTGM = "wdtgm"//我的推广码
const val HDSQ = "hdsq"//志愿者申请
const val HDSTYPE = "hdstype"//活动分类列表
const val GYWM = "gywm"//关于我们
const val SETINFO = "setinfo"//设置通知消息
const val DJWYF = "djwyf"//待缴物业费
const val XQLISTS = "xqlists"//小区列表
const val XQDYLISTS = "xqdylists"//小区单元信息
const val APKV = "apkv"//检查更新
const val YZSFLISTS = "yzsflists"//业主身份认证
const val TJSFRZ = "tjsfrz"//提交身份认证
const val XQINFO = "xqinfo"//小区详情
const val XQPF = "xqpf"//小区评分

