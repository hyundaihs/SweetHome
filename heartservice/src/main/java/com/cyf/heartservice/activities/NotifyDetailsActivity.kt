package com.cyf.heartservice.activities

import android.content.Context
import android.os.Bundle
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.loadLocalHtml
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_notify_details.*

class NotifyDetailsActivity : MyBaseActivity() {

    var id = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_notify_details)
        setTitle("详情")
        id = intent.getStringExtra("id") ?: "0"
        getData()
    }

    private fun getData() {
        val map = mapOf(
            Pair("id", id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(TZGGINFO.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onNegative() {
                        }

                        override fun onPositive() {
                            finish()
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val notifyRes = Gson().fromJson(result, NotifyRes::class.java)
                    initViews(notifyRes.retRes)
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun initViews(notify: Notify) {
        notifyTitle.text = notify.title
        notifyTime.text = CalendarUtil(
            notify.create_time,
            true
        ).format(CalendarUtil.YYYY_MM_DD_HH_MM)
        notifyDetails.loadLocalHtml(notify.app_contents)
    }
}