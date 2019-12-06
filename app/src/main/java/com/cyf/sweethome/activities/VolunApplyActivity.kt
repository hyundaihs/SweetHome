package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.TJBYX
import com.cyf.sweethome.entity.getInterface
import com.dou361.dialogui.listener.DialogUIListener
import kotlinx.android.synthetic.main.activity_volun_apply.*

class VolunApplyActivity : MyBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_volun_apply)
        setTitle("志愿者认证申请")
        submit.setOnClickListener {
            submit()
        }
    }

    private fun checkEdits(): Boolean {
        return when {
            name.text.isEmpty() -> {
                name.error = "请填写内容"
                false
            }
            idCard.text.isEmpty() -> {
                idCard.error = "请填写内容"
                false
            }
            info.text.isEmpty() -> {
                info.error = "请填写内容"
                false
            }
            else -> true
        }
    }

    private fun submit() {
        if (!checkEdits()) {
            return
        }
        val map = mapOf(
            Pair("contents", name.text.toString()),
            Pair("contents", idCard.text.toString()),
            Pair("contents", info.text.toString())
        )
        KevinRequest.build(this).apply {
            setRequestUrl(TJBYX.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    getSuccessDialog(context, "操作成功", object : DialogUIListener() {
                        override fun onPositive() {
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