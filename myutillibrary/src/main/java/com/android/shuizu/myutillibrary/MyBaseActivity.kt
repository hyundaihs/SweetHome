package com.android.shuizu.myutillibrary

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/4/30/030.
 */
open class MyBaseActivity : AppCompatActivity() {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}