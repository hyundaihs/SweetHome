package com.android.shuizu.myutillibrary.utils

import android.app.ActionBar
import android.app.Activity
import com.android.shuizu.myutillibrary.R
import kotlinx.android.synthetic.main.layout_toolbar.view.*

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/12.
 */
class ActionBarUtil(activity: Activity) {

    private var actionBar: ActionBar

    init {
        actionBar = activity.actionBar!!
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setBackup(true)
        actionBar.run {
            setCustomView(R.layout.layout_toolbar)
            customView.setOnClickListener {

            }
        }
    }

    fun setTitle(text: String) {
        actionBar.customView.toolbarTitle.text = text
    }

    fun setBackup(showBack: Boolean) {
        actionBar.setDisplayHomeAsUpEnabled(showBack)
    }

}