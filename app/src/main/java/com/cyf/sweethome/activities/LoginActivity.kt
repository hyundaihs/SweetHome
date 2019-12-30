package com.cyf.sweethome.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import cn.jpush.android.api.JPushInterface
import com.android.shuizu.myutillibrary.D
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.PreferenceUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome
import com.cyf.sweethome.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess
import java.util.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class LoginActivity : MyBaseActivity() {

    var account: String by PreferenceUtil(SweetHome.instance, "account", "")

    private var mTimer: Timer? = null
    private var mTimerTask: MyTimerTask? = null
    private val COUNT = 60
    private var time = COUNT
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
        setContentView(R.layout.activity_login)
        loginBtn.isEnabled = (inputPhone.text.isNotEmpty() and inputMsg.text.isNotEmpty())
        getYzm.isEnabled = inputPhone.text.isNotEmpty()
        inputPhone.addTextChangedListener {
            getYzm.isEnabled = inputPhone.text.isNotEmpty()
            loginBtn.isEnabled = (inputPhone.text.isNotEmpty() and inputMsg.text.isNotEmpty())
        }
        inputMsg.addTextChangedListener {
            loginBtn.isEnabled = (inputPhone.text.isNotEmpty() and inputMsg.text.isNotEmpty())
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
                    val loginInfoRes = Gson().fromJson(result, LoginInfoRes::class.java)
                    account = inputPhone.text.toString()
                    if(loginInfoRes.retRes.is_rz == 1){
                        startActivity(Intent(context, HomepageActivity::class.java))
                        finish()
                    }else{
                        if(loginInfoRes.retRes.sh_status == 0){
                            startActivity(Intent(context, JumingRenzhengActivity::class.java))
                        }else{
                            val intent = Intent(context, IsAuthActivity::class.java)
                            intent.putExtra("status",loginInfoRes.retRes.sh_status)
                            startActivity(intent)
                        }
                    }
                    inputMsg.setText("")
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