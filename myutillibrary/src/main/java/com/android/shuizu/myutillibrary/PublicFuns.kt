package com.android.shuizu.myutillibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.android.shuizu.myutillibrary.utils.CalendarUtil


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

fun Context.sp2px(sp:Float):Float{
    return TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}

fun Any.getTime(time: Long): String {
    val ca = CalendarUtil(time * 1000)
    return ca.format(CalendarUtil.STANDARD)
}

fun Context.gotoActivity(cls: Class<*>){
    startActivity(Intent(this, cls))
}

fun Activity.hideInput(){
    val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    val v = window.peekDecorView()
    if (null != v) {
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}