package com.cyf.sweethome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.shuizu.myutillibrary.utils.ActionBarUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActionBarUtil(this).setTitle("测试页面")
    }
}
