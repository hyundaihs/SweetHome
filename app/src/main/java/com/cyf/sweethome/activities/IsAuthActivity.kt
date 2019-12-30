package com.cyf.sweethome.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.cyf.sweethome.R
import kotlinx.android.synthetic.main.activity_is_auth.*

class IsAuthActivity : MyBaseActivity() {

    val STATUS_STR = listOf("", "您的信息正在审核中，请耐心等待", "", "您的审核已被拒绝！")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_is_auth)
        setTitle("业主认证")
        val status = intent.getIntExtra("status", 0)
        authStatus.text = STATUS_STR[status]
        submit.visibility = if (status == 3) View.VISIBLE else View.GONE
        submit.setOnClickListener {
            startActivity(Intent(it.context, JumingRenzhengActivity::class.java))
            finish()
        }
    }
}