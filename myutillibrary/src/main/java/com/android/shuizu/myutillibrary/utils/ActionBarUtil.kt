package com.android.shuizu.myutillibrary.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.shuizu.myutillibrary.R
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/12.
 */
class ActionBarUtil(val activity: AppCompatActivity) {

    init {
        val toolbarView = activity.layoutInflater.inflate(R.layout.layout_toolbar, null, false)
        activity.setSupportActionBar(toolbarView.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setBackup()
    }

    fun setTitle(text: String) {
        activity.toolbarTitle.text = text
    }

    fun setBackup(showBack: Boolean = true) {
        if(showBack) {
            addLeftImageBtn(R.mipmap.icon_back,View.OnClickListener {
                activity.finish()
            })
        }else{
            removeLeftImageBtn()
        }
    }

    fun addLeftStringBtn(text: String, click: View.OnClickListener) {
        activity.toolbarLeftBtn.visibility = View.VISIBLE
        activity.toolbarLeftBtn.text = text
        activity.toolbarLeftBtn.setOnClickListener(click)
    }

    fun addLeftImageBtn(res: Int, click: View.OnClickListener) {
        activity.toolbarLeftImage.visibility = View.VISIBLE
        activity.toolbarLeftImage.setImageResource(res)
        activity.toolbarLeftImage.setOnClickListener(click)
    }

    fun removeLeftImageBtn(){
        activity.toolbarLeftImage.visibility = View.GONE
    }

    fun addRightStringBtn(text: String,click: View.OnClickListener){
        activity.toolbarRightBtn.visibility = View.VISIBLE
        activity.toolbarRightBtn.text = text
        activity.toolbarRightBtn.setOnClickListener(click)
    }

    fun addRightImageBtn(res: Int,click: View.OnClickListener){
        activity.toolbarRightImg.visibility = View.VISIBLE
        activity.toolbarRightImg.setImageResource(res)
        activity.toolbarRightImg.setOnClickListener(click)
    }


}