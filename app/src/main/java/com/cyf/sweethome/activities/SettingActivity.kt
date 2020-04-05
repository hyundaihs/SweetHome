package com.cyf.sweethome.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.*
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome
import com.cyf.sweethome.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.photo
import kotlinx.android.synthetic.main.fragment_mine.*
import org.jetbrains.anko.toast

class SettingActivity : MyBaseActivity() {

    val REQUEST_CODE_CHOOSE = 100

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectList = PictureSelectorObtainMultipleResult(data)
            when (requestCode) {
                REQUEST_CODE_CHOOSE -> {

                    for (i in 0 until selectList.size) {
                        val file = selectList[i]
                        uploadPhoto(file)
                    }
                }
            }
        }
        if (requestCode == 106 && resultCode == 107) {
            startActivity(Intent(this, LoginActivity::class.java))
            setResult(105)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_setting)
        setTitle("设置")
        init()
    }

    private fun init() {
        SweetHome.userInfo?.let {
            switch1.isChecked = it.jpush_status == 1
            Picasso.with(this).load(it.file_url.getImageUrl())
                .resize(100, 100)
                .error(R.mipmap.ic_launcher)
                .into(photo)
            phone.text = it.phone
        }
        switch1.setOnClickListener {
            setSwitch()
        }
        exitLogin.setOnClickListener {
            getMessageDialog(this, "确定要退出登陆吗？", object : DialogUIListener() {
                override fun onPositive() {
                    startActivity(Intent(it.context, LoginActivity::class.java))
                    setResult(105)
                    finish()
                }

                override fun onNegative() {
                }

            })
        }
        photoBtn.setOnClickListener {
            PictureSelectorStart(1, REQUEST_CODE_CHOOSE)
        }
        phoneBtn.setOnClickListener {
            val intent = Intent(this, ChangeAccountActivity::class.java)
            startActivityForResult(intent,106)
        }
    }

    private fun uploadPhoto(file: String) {
        val list = ArrayList<String>()
        list.add(file)
        KevinRequest.build(this).apply {
            setRequestUrl(UPLOADFILE.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    toast(error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val fileInfoRes = Gson().fromJson(result, FileInfoRes::class.java)
                    setPhoto(fileInfoRes.retRes.file_url)
                }
            })
            openLoginErrCallback(LoginActivity::class.java)
            setDialog()
            uploadFile(list)
        }
    }

    private fun setPhoto(fileUrl: String) {
        val map = mapOf(
            Pair("file_url", fileUrl)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(SETFILEURL.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    Picasso.with(context).load(fileUrl.getImageUrl())
                        .resize(100, 100)
                        .error(R.mipmap.ic_launcher)
                        .into(photo)
                    SweetHome.userInfo?.let {
                        it.file_url = fileUrl
                    }
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun setSwitch() {
        val map = mapOf(
            Pair("jpush_status", if (switch1.isChecked) 1 else 0)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(SETINFO.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    switch1.isChecked = !switch1.isChecked
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {

                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }
}