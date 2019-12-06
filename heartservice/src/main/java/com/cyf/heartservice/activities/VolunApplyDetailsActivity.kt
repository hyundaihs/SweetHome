package com.cyf.heartservice.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_volun_apply_details.*

class VolunApplyDetailsActivity : MyBaseActivity() {

    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_volun_apply_details)
        setTitle("志愿者认证审核")
        id = intent.getStringExtra("id")?:""
        getVolunApplyInfo()
    }

    private fun getVolunApplyInfo() {
        val map = mapOf(
            Pair("id", id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(HDSQINFO.getInterface(map))
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
                    val volunApplyInfoRes = Gson().fromJson(result, VolunApplyInfoRes::class.java)
                    init(volunApplyInfoRes.retRes)
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun init(info: VolunApplyInfo) {
        applyName.text = info.title
        idCardNum.text = info.card_num
        phoneNum.text = info.phone
        applyContent.text = info.contents
        applyTime.text = CalendarUtil(info.create_time, true).format(
            CalendarUtil.STANDARD
        )
        line.visibility = if (info.sh_status == 1) View.VISIBLE else View.GONE
        handleLayout.visibility = if (info.sh_status == 1) View.VISIBLE else View.GONE
        agree.setOnClickListener {
            sendApplyStatus(2)
        }
        refuse.setOnClickListener {
            sendApplyStatus(3)
        }
    }

    private fun sendApplyStatus(status: Int) {
        val map = mapOf(
            Pair("id", id),
            Pair("sh_status", status)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(HDSQSET.getInterface(map))
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
                    getSuccessDialog(context, "操作成功！", object : DialogUIListener() {
                        override fun onPositive() {
                            setResult(1002)
                            finish()
                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }
}