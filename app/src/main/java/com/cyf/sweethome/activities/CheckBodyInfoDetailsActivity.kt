package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_check_body_info_details.*

class CheckBodyInfoDetailsActivity : MyBaseActivity() {

    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_check_body_info_details)
        setTitle("体检报告")
        id = intent.getStringExtra("id") ?: ""
        getCheckBodyInfo()
    }

    private fun getCheckBodyInfo() {
        val map = mapOf(
            Pair("id", id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(TJBGINFO.getInterface())
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
                    val checkReportRes = Gson().fromJson(result, CheckReportRes::class.java)
                    init(checkReportRes.retRes)
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun init(checkReport: CheckReport) {
        tmp.text = checkReport.datas.temperature
        tmp_status.text = checkReport.datas.temperature_str
        bmi.text = checkReport.datas.bmi
        bmi_status.text = checkReport.datas.bmi_str
        height.text = "${checkReport.datas.height}cm"
        weight.text = "${checkReport.datas.weight}Kg"
        shuzhang.text = checkReport.datas.diastolic
        sz_status.text = checkReport.datas.diastolic_str
        shousuo.text = checkReport.datas.systolic
        ss_status.text = checkReport.datas.systolic_str
    }
}