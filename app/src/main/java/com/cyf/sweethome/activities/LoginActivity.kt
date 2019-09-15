package com.cyf.sweethome.activities

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.cyf.sweethome.R
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import org.jetbrains.anko.doAsync
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
    }

    private fun login() {
        startActivity(Intent(this, HomepageActivity::class.java))
    }


}