package com.cyf.sweethome.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.android.shuizu.myutillibrary.E
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
            setRequestUrl(HDINFO.getInterface(map))
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
        actInfoTime.text = "活动时间：${actInfo.date_time}"
        actInfoType.text = "活动类型：${actInfo.stype_title}"
        actInfoContent.loadLocalHtml(actInfo.app_contents)

        if(actInfo.hd_status == "3"){//已过期
            submit.text = actInfo.hd_status_title
            setSubmitEnable(false)
        }else{
            if(actInfo.sh_status == 2){
                if (actInfo.is_bm == 0) {
                    submit.text = "参与"
                    setSubmitEnable(true, View.OnClickListener {
                        submit()
                    })
                } else {
                    submit.text = "已参与"
                    setSubmitEnable(false)
                }
            }else if(actInfo.sh_status == 1){
                submit.text = "审核中"
                setSubmitEnable(false)
            }else{
                submit.text = "申请"
                setSubmitEnable(true, View.OnClickListener {
                    apply(actInfo.id,actInfo.stype_id)
                })
            }
        }
    }

    private fun setSubmitEnable(isEnable:Boolean,clickListener: View.OnClickListener? = null){
        if(isEnable){
            submit.setBackgroundResource(R.drawable.rect_ff4753_corner_5)
        }else{
            submit.setBackgroundResource(R.drawable.react_a0a0a0_corner_5)
        }
        submit.setOnClickListener(clickListener)
    }

    private fun apply(hdId:String,typeId:String) {
        val ids = ArrayList<String>()
        ids.add(typeId)
        val map = mapOf(
            Pair("xqhd_id", hdId),
            Pair("stype_ids", ids)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(HDSQ.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    toast("申请提交成功")
                    getActInfoDetails()
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
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
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    toast("报名成功")
                    submit.text = "已参与"
                    setSubmitEnable(false)
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }
}