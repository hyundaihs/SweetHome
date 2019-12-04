package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.loadLocalHtml
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_act_info_details.*
import org.jetbrains.anko.toast

class ActInfoDetailsActivity : MyBaseActivity() {

    private var actInfo = ActInfo()
    private var id = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_act_info_details)
        setTitle("活动详情")
        getActInfoDetails()
    }

    private fun getActInfoDetails() {
        id = intent.getStringExtra("id") ?: "0"
        val map = mapOf(
            Pair("id", id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(HDINFO.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {
                            finish()
                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val actInfoRes = Gson().fromJson(result, ActInfoRes::class.java)
                    actInfo = actInfoRes.retRes
                    init()
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun init() {
        actInfoTitle.text = actInfo.title
        actInfoTime.text = "活动时间：${CalendarUtil(actInfo.create_time, true).format(
            CalendarUtil.YYYY_MM_DD
        )}"
        actInfoType.text = "活动类型：${actInfo.hd_status_title}"
        actInfoContent.loadLocalHtml(actInfo.app_contents)
        if (actInfo.is_bm == 1) {
            layoutBtn.visibility = View.GONE
        } else {
            layoutBtn.visibility = View.VISIBLE
        }
        submit.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val map = mapOf(
            Pair("id", id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(HDBM.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {

                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    toast("报名成功")
                    layoutBtn.visibility = View.GONE
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }
}