package com.android.shuizu.myutillibrary

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.HProgressDialogUtils
import com.android.shuizu.myutillibrary.utils.OKHttpUpdateHttpService
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate._XUpdate
import com.xuexiang.xupdate.service.OnFileDownloadListener
import com.xuexiang.xupdate.utils.UpdateUtils
import com.xuexiang.xutil.tip.ToastUtils.toast
import org.jetbrains.anko.toast
import java.io.File


/**
 * LeZu
 * Created by 蔡雨峰 on 2018/1/4.
 */

fun Any.D(text: String) {
    Log.d("${this.javaClass.simpleName} ", text)
}

fun Any.E(text: String) {
    Log.e("${this.javaClass.simpleName} ", text)
}

fun Any.I(text: String) {
    Log.i("${this.javaClass.simpleName} ", text)
}

fun Any.W(text: String) {
    Log.w("${this.javaClass.simpleName} ", text)
}

fun Any.V(text: String) {
    Log.v("${this.javaClass.simpleName} ", text)
}

fun Context.dp2px(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}

fun Context.sp2px(sp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}

fun Any.getTime(time: Long): String {
    val ca = CalendarUtil(time * 1000)
    return ca.format(CalendarUtil.STANDARD)
}

fun Context.gotoActivity(cls: Class<*>) {
    startActivity(Intent(this, cls))
}

fun Activity.hideInput() {
    val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    val v = window.peekDecorView()
    if (null != v) {
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

fun Any.initVersionUpdate(context: Application) {
    XUpdate.get()
        .debug(true)
        .isWifiOnly(true)                                               //默认设置只在wifi下检查版本更新
        .isGet(true)                                                    //默认设置使用get请求检查版本
        .isAutoMode(false)                                              //默认设置非自动模式，可根据具体使用配置
        .param("versionCode", UpdateUtils.getVersionCode(context))         //设置默认公共请求参数
        .supportSilentInstall(true)                                     //设置是否支持静默安装，默认是true
        .setIUpdateHttpService(OKHttpUpdateHttpService())           //这个必须设置！实现网络请求功能。
        .init(context)
}

fun Any.downloadApk(context: Context, url: String) {
    XUpdate.newBuild(context)
        .build()
        .download(url, object : OnFileDownloadListener {   //设置下载的地址和下载的监听
            override fun onStart() {
                HProgressDialogUtils.showHorizontalProgressDialog(
                    context,
                    "下载进度",
                    false
                )
            }

            override fun onProgress(progress: Float, total: Long) {
                HProgressDialogUtils.setProgress(Math.round(progress * 100))
            }

            override fun onCompleted(file: File): Boolean {
                HProgressDialogUtils.cancel()
                _XUpdate.startInstallApk(context, file) //填写文件所在的路径
                return false
            }

            override fun onError(throwable: Throwable) {
                toast("下载失败")
                HProgressDialogUtils.cancel()
            }
        })
}