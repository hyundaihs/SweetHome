package com.cyf.heartservice.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getMessageDialog
import com.cyf.heartservice.HeartService
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.SETINFO
import com.cyf.heartservice.entity.getInterface
import com.dou361.dialogui.listener.DialogUIListener
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : MyBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_setting)
        setTitle("设置")
        init()
    }

    private fun init() {
        HeartService.userInfo?.let {
            switch1.isChecked = it.jpush_status == 1
        }
        switch1.setOnClickListener {
            setSwitch()
        }
        exitLogin.setOnClickListener {
            getMessageDialog(this, "确定要退出登陆吗？", object : DialogUIListener() {
                override fun onPositive() {
                    startActivity(Intent(it.context, LoginActivity::class.java))
                    setResult(105)
                    finish()
                }

                override fun onNegative() {
                }

            })
        }
    }

    private fun setSwitch() {
        val map = mapOf(
            Pair("jpush_status", if (switch1.isChecked) 1 else 0)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(SETINFO.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    switch1.isChecked = !switch1.isChecked
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {

                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }
}