package com.cyf.heartservice.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import cn.jpush.android.api.JPushInterface
import com.android.shuizu.myutillibrary.D
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.PreferenceUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.heartservice.HeartService
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.LOGIN
import com.cyf.heartservice.entity.SENDVERF
import com.cyf.heartservice.entity.getInterface
import kotlinx.android.synthetic.main.activitiy_login.*
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
class LoginActivity : MyBaseActivity() {

    var account: String by PreferenceUtil(HeartService.instance, "account", "")

    private var mTimer: Timer? = null
    private var mTimerTask: MyTimerTask? = null
    private val COUNT = 60
    private var time = COUNT
    private var isPhoneNotEmpty = false
    private var isMsgNotEmpty = false
    private val SUCCESSCODE = 100

    inner class MyTimerTask : TimerTask() {
        override fun run() {
            doAsync {
                uiThread {
                    getYzm.text = "${time--}s后重新获取"
                    if (time < 0) {
                        mTimer?.cancel()
                        mTimer = null
                        mTimerTask?.cancel()
                        mTimerTask = null
                        getYzm.text = "获取验证码"
                        getYzm.isEnabled = true
                        time = COUNT
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitiy_login)
        isPhoneNotEmpty = inputPhone.text.isNotEmpty()
        isMsgNotEmpty = inputMsg.text.isNotEmpty()
        loginBtn.isEnabled = (inputPhone.text.isNotEmpty() and inputMsg.text.isNotEmpty())
        getYzm.isEnabled = inputPhone.text.isNotEmpty()
        inputPhone.addTextChangedListener {
            isPhoneNotEmpty = inputPhone.text.isNotEmpty()
            getYzm.isEnabled = isPhoneNotEmpty
            loginBtn.isEnabled = isPhoneNotEmpty && isMsgNotEmpty
        }
        inputMsg.addTextChangedListener {
            isMsgNotEmpty = inputMsg.text.isNotEmpty()
            loginBtn.isEnabled = isPhoneNotEmpty && isMsgNotEmpty
        }
        inputPhone.setText(account)
        getYzm.setOnClickListener {
            getYzm.isEnabled = false
            mTimer = Timer()
            mTimerTask = MyTimerTask()
            mTimer?.schedule(mTimerTask, 0, 1000)
            sendYZM()
        }
        loginBtn.setOnClickListener {
            login()
        }
        getPermission()
    }

    private fun sendYZM() {
        val map = mapOf(
            Pair("phone", inputPhone.text.toString())
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
    }

    private fun login() {
        val id = JPushInterface.getRegistrationID(this)
        val map = mapOf(
            Pair("phone", inputPhone.text.toString()),
            Pair("msgverf", inputMsg.text.toString()),
            Pair("jpush_id", id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(LOGIN.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    account = inputPhone.text.toString()
                    startActivity(Intent(context, HomepageActivity::class.java))
                    finish()
                }

            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }


    fun getPermission() {
        //处理需要动态申请的权限
        PermissionGen.with(this)
            .addRequestCode(SUCCESSCODE)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).request()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }


    @PermissionSuccess(requestCode = 100)
    fun doSomething() {
        toast("权限获取成功")
    }

    @PermissionFail(requestCode = 100)
    fun doFailSomething() {
        toast("权限获取失败")
    }
}