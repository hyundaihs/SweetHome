package com.cyf.sweethome.activities

import android.os.Bundle
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.cyf.sweethome.R
import com.cyf.sweethome.fragment.EventsCenterFragmentMy

class EventsCenterActivity : MyBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_events_center)
        setTitle("我的活动")
        supportFragmentManager    //
            .beginTransaction()
            .add(
                R.id.fragment_container,
                EventsCenterFragmentMy("1")
            )   // 此处的R.id.fragment_container是要盛放fragment的父容器
            .commit()
    }
}