package com.cyf.sweethome.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.LOGIN
import com.cyf.sweethome.entity.getInterface
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

    private var mTimer: Timer? = null
    private var mTimerTask: MyTimerTask? = null
    private val COUNT = 6
    private var time = COUNT
    private var isPhoneNotEmpty = false
    private var isMsgNotEmpty = false
    private val SUCCESSCODE = 100

    inner class MyTimerTask : TimerTask() {
        override fun run() {
            doAsync {
                uiThread {
                    getYzm.text = "${time--}S"
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
            isPhoneNotEmpty = inputPhone.text.isNotEmpty()
            getYzm.isEnabled = isPhoneNotEmpty
            loginBtn.isEnabled = isPhoneNotEmpty && isMsgNotEmpty
        }
        inputMsg.addTextChangedListener {
            isMsgNotEmpty = inputMsg.text.isNotEmpty()
            loginBtn.isEnabled = isPhoneNotEmpty && isMsgNotEmpty
        }
        getYzm.setOnClickListener {
            getYzm.isEnabled = false
            mTimer = Timer()
            mTimerTask = MyTimerTask()
            mTimer?.schedule(mTimerTask, 0, 1000)
        }
        loginBtn.setOnClickListener {
            login()
        }
        getPermission()
    }

    private fun login() {
        val map = mapOf(
            Pair("phone", inputPhone.text.toString()),
            Pair("msgverf", inputMsg.text.toString())
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
//                    val loginInfoRes = Gson().fromJson(result, LoginInfoRes::class.java)
//                    val loginInfo = loginInfoRes.retRes
//                    login_verf = loginInfo.login_verf
                    startActivity(Intent(context, HomepageActivity::class.java))
                    finish()
                }

            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }


    fun getPermission(){
        //处理需要动态申请的权限
        PermissionGen.with(this)
            .addRequestCode(SUCCESSCODE)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ) .request()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }


    @PermissionSuccess(requestCode = 100)
    fun doSomething(){
        toast("权限获取成功")
    }

    @PermissionFail(requestCode = 100)
    fun doFailSomething(){
        toast("权限获取失败")
    }

}