package com.android.shuizu.myutillibrary

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_my_base.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/4/30/030.
 */
abstract class MyBaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_base)
        setBackup(true)
    }

    fun setContentBaseView(res:Int){
        baseContent.addView(layoutInflater.inflate(res,null,false))
    }

    fun setTitle(text: String) {
        toolbarTitle.text = text
    }

    fun setBackup(showBack: Boolean) {
        if(showBack) {
            addLeftImageBtn(R.mipmap.back, View.OnClickListener {
                finish()
            })
        }else{
            removeLeftImageBtn()
        }
    }

    fun addLeftStringBtn(text: String, click: View.OnClickListener) {
        toolbarLeftBtn.visibility = View.VISIBLE
        toolbarLeftBtn.text = text
        toolbarLeftBtn.setOnClickListener(click)
    }

    fun addLeftImageBtn(res: Int, click: View.OnClickListener) {
        toolbarLeftImage.visibility = View.VISIBLE
        toolbarLeftImage.setImageResource(res)
        toolbarLeftImage.setOnClickListener(click)
    }

    fun removeLeftImageBtn(){
        toolbarLeftImage.visibility = View.GONE
    }

    fun addRightStringBtn(text: String,click: View.OnClickListener){
        toolbarRightBtn.visibility = View.VISIBLE
        toolbarRightBtn.text = text
        toolbarRightBtn.setOnClickListener(click)
    }

    fun addRightImageBtn(res: Int,click: View.OnClickListener){
        toolbarRightImg.visibility = View.VISIBLE
        toolbarRightImg.setImageResource(res)
        toolbarRightImg.setOnClickListener(click)
    }
}