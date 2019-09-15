package com.cyf.sweethome.activities

import android.os.Bundle
import android.view.View
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.cyf.sweethome.R

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class CheckRoomActivity : MyBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_check_room)
        setTitle("提交验房")
        addRightStringBtn("取消", View.OnClickListener {

        })
    }
}