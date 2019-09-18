package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.fragment.app.Fragment
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome
import com.cyf.sweethome.entity.USERINFO
import com.cyf.sweethome.entity.UserInfoRes
import com.cyf.sweethome.entity.getInterface
import com.cyf.sweethome.fragment.CommunityFragment
import com.cyf.sweethome.fragment.HomeFragment
import com.cyf.sweethome.fragment.MineFragment
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_homepage.*
import org.jetbrains.anko.toast

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class HomepageActivity : MyBaseActivity() {

    private val fragments = ArrayList<Fragment>(3)
    private var last = -1

    private var isExit = false  // 标识是否退出

    private val mHandler = object : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            isExit = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        getUserInfo()
    }

    private fun getUserInfo() {
        KevinRequest.build(this).apply {
            setRequestUrl(USERINFO.getInterface())
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
                    val userInfoRes = Gson().fromJson(result, UserInfoRes::class.java)
                    SweetHome.userInfo = userInfoRes.retRes
                    init()
                }
            })
            postRequest()
        }
    }

    private fun init(){
        fragments.add(HomeFragment())
        fragments.add(CommunityFragment())
        fragments.add(MineFragment())
        bottomTab.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.tabHome -> {
                    loadFragment(0)
                }
                R.id.tabCommunity -> {
                    loadFragment(1)
                }
                else -> {
                    loadFragment(2)
                }
            }
        }
        loadFragment(0)
    }

    override fun onBackPressed() {
        if (!isExit) {
            isExit = true
            toast("再按一次后退键退出程序")
            mHandler.sendEmptyMessageDelayed(0, 2000)  // 利用handler延迟发送更改状态信息
        } else {
            super.onBackPressed()
        }
    }

    private fun loadFragment(position: Int) {
        val ft = supportFragmentManager.beginTransaction()
        if (!fragments[position].isAdded) {
            ft.add(R.id.content, fragments[position])
        }
        if (last != -1) {
            ft.hide(fragments[last])
        }
        ft.show(fragments[position])
        last = position
        ft.commit()
    }

}