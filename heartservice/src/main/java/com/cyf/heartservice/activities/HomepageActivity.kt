package com.cyf.heartservice.activities

import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.fragment.app.Fragment
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.cyf.heartservice.R
import com.cyf.heartservice.fragments.ContactFragment
import com.cyf.heartservice.fragments.MessageFragment
import com.cyf.heartservice.fragments.MineFragment
import com.cyf.heartservice.fragments.WorkerFragment
import kotlinx.android.synthetic.main.activity_homepage.*
import org.jetbrains.anko.toast

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
class HomepageActivity : MyBaseActivity() {

    private val fragments = ArrayList<Fragment>(4)
    private var last = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        fragments.add(MessageFragment())
        fragments.add(ContactFragment())
        fragments.add(WorkerFragment())
        fragments.add(MineFragment())
        bottomTab.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.tabMessage -> {
                    loadFragment(0)
                }
                R.id.tabContact -> {
                    loadFragment(1)
                }
                R.id.tabWorker -> {
                    loadFragment(2)
                }
                else -> {
                    loadFragment(3)
                }
            }
        }
        loadFragment(0)
    }

    private var isExit = false  // 标识是否退出

    private val mHandler = object : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            isExit = false
        }
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