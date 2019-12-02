package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.DJSQ
import com.cyf.sweethome.entity.getInterface
import com.cyf.sweethome.utils.PickerUtil
import com.dou361.dialogui.listener.DialogUIListener
import kotlinx.android.synthetic.main.activity_member_apply.*

class MemberApplyActivity : MyBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_member_apply)
        setTitle("党员认证申请")
        init()
    }

    private fun init() {
        memberTime.setOnClickListener {
            hideInput()
            chooseTime()
        }
        submit.setOnClickListener {
            if(checkEdits()){
                submit()
            }
        }
    }

    /**
     * 隐藏键盘
     */
    private fun hideInput() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val v = window.peekDecorView()
        if (null != v) {
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    private fun chooseTime(){
        PickerUtil.showTimerPickerYM(this
        ) { date, _ ->
            val chooseTime = CalendarUtil(date.time).format(CalendarUtil.YYYY_MM)
            memberTime.setText(chooseTime)
        }
    }

    private fun checkEdits(): Boolean {
        when {
            memberName.text.trim().isEmpty() -> memberName.error = "姓名不能为空"
            memberIdCard.text.trim().isEmpty() -> memberIdCard.error = "身份证号不能为空"
            memberGroupName.text.trim().isEmpty() -> memberGroupName.error = "党支部名称不能为空"
            memberTime.text.trim().isEmpty() -> memberTime.error = "入党年月不能为空"
            memberGroupAddr.text.trim().isEmpty() -> memberGroupAddr.error = "党支部地址不能为空"
            else -> return true
        }
        return false
    }

    private fun submit() {
        val map = mapOf(
            Pair("title", memberName.text.toString()),
            Pair("card_num", memberIdCard.text.toString()),
            Pair("rdsj", memberTime.text.toString()),
            Pair("dzbmc", memberGroupName.text.toString()),
            Pair("dzbdz", memberGroupAddr.text.toString())
        )
        KevinRequest.build(this).apply {
            setRequestUrl(DJSQ.getInterface())
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
                    getSuccessDialog(context,"申请提交成功！", object : DialogUIListener() {
                        override fun onPositive() {
                            finish()
                        }

                        override fun onNegative() {

                        }
                    })
                }
            })
            setDialog()
            setDataMap(map)
            postRequest()
        }
    }

}