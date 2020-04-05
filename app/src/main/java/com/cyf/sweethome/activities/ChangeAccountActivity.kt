package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome
import com.cyf.sweethome.entity.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_change_account.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.w3c.dom.Text
import java.util.*

class ChangeAccountActivity : MyBaseActivity() {

    private val COUNT = 60
    private var mTimerOld: Timer? = null
    private var mTimerTaskOld: MyTimerTask? = null
    private var timeOld = COUNT
    private var mTimerNew: Timer? = null
    private var mTimerTaskNew: MyTimerTask? = null
    private var timeNew = COUNT

    inner class MyTimerTask(val view:TextView) : TimerTask() {
        override fun run() {
            doAsync {
                uiThread {
                    if(view.id == R.id.getYzmOld){
                        view.text = "${timeOld--}s后重新获取"
                        if (timeOld < 0) {
                            mTimerOld?.cancel()
                            mTimerOld = null
                            mTimerTaskOld?.cancel()
                            mTimerTaskOld = null
                            view.text = "获取验证码"
                            view.isEnabled = true
                            timeOld = COUNT
                        }
                    }else{
                        view.text = "${timeNew--}s后重新获取"
                        if (timeNew < 0) {
                            mTimerNew?.cancel()
                            mTimerNew = null
                            mTimerTaskNew?.cancel()
                            mTimerTaskNew = null
                            view.text = "获取验证码"
                            view.isEnabled = true
                            timeNew = COUNT
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_change_account)
        setTitle("修改手机号码")
        SweetHome.userInfo?.let {
            init(it.phone)
        }
    }

    private fun init(p: String) {
        inputPhoneOld.setText(p)
        getYzmOld.isEnabled = inputPhoneOld.text.isNotEmpty()
        inputPhoneOld.addTextChangedListener {
            getYzmOld.isEnabled = inputPhoneOld.text.isNotEmpty()
        }
        getYzmNew.isEnabled = inputPhoneNew.text.isNotEmpty()
        inputPhoneNew.addTextChangedListener {
            getYzmNew.isEnabled = inputPhoneNew.text.isNotEmpty()
        }
        getYzmOld.setOnClickListener {
            getYzmOld.isEnabled = false
            mTimerOld = Timer()
            mTimerTaskOld = MyTimerTask(getYzmOld)
            mTimerOld?.schedule(mTimerTaskOld, 0, 1000)
            sendYZM(inputPhoneOld.text.toString())
        }
        getYzmNew.setOnClickListener {
            getYzmNew.isEnabled = false
            mTimerNew = Timer()
            mTimerTaskNew = MyTimerTask(getYzmNew)
            mTimerNew?.schedule(mTimerTaskNew, 0, 1000)
            sendYZM(inputPhoneNew.text.toString())
        }
    }

    private fun sendYZM(phone:String) {
        val map = mapOf(
            Pair("phone", phone)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(SENDVERF.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    toast("发送成功")
                }

            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
        submit.setOnClickListener {
            submit()
        }
    }

    private fun checkInput():Boolean{
        if(inputPhoneOld.text.isBlank()){
            inputPhoneOld.error = "手机号码不能为空"
            return false
        }else if(inputMsgOld.text.isBlank()){
            inputMsgOld.error = "验证码不能为空"
            return false
        }else if(inputPhoneNew.text.isBlank()){
            inputPhoneNew.error = "手机号码不能为空"
            return false
        }else if(inputMsgNew.text.isBlank()){
            inputMsgNew.error = "验证码不能为空"
            return false
        }else{
            return true
        }
    }

    private fun submit(){
        if(!checkInput()){
            return
        }
        val map = mapOf(
            Pair("phone", inputPhoneOld.text.toString()),
            Pair("msgverf", inputMsgOld.text.toString()),
            Pair("new_phone", inputPhoneNew.text.toString()),
            Pair("new_msgverf", inputMsgNew.text.toString())
        )
        KevinRequest.build(this).apply {
            setRequestUrl(SETPHONE.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    toast("修改成功，请重新登录")
                    setResult(107)
                    finish()
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }

    }
}