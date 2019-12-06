package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome
import com.cyf.sweethome.entity.TJBYX
import com.cyf.sweethome.entity.TJYJFK
import com.cyf.sweethome.entity.getInterface
import com.dou361.dialogui.listener.DialogUIListener
import kotlinx.android.synthetic.main.activity_praise.*

class PraiseActivity : MyBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_praise)
        setTitle("表扬物业")
        init()
    }

    private fun init() {
        address.text = SweetHome.houseInfo?.fw_title
        time.text = CalendarUtil().format(CalendarUtil.YYYY_MM_DD)
        submit.setOnClickListener {
            submit()
        }
    }

    private fun checkEdits(): Boolean {
        return when {
            question.text.isEmpty() -> {
                question.error = "请填写内容"
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
            Pair("contents", question.text.toString())
        )
        KevinRequest.build(this).apply {
            setRequestUrl(TJBYX.getInterface(map))
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