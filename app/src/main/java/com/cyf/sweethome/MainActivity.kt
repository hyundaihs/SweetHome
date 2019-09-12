package com.cyf.sweethome

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.utils.ActionBarUtil
import org.jetbrains.anko.toast

class MainActivity : MyBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_main)
    }

}
