package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.downloadApk
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getMessageDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cyf.sweethome.BuildConfig
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_about_us.*


class AboutUsActivity : MyBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_about_us)
        setTitle("关于我们")
        init()
    }

    private fun init() {
        versionName.text = BuildConfig.VERSION_NAME
        checkVersion.setOnClickListener {
            requestVersionInfo()
        }
    }

    //获取版本信息
    private fun requestVersionInfo() {
        val map = mapOf(
            Pair("type", "android")
        )
        KevinRequest.build(this).apply {
            setRequestUrl(APKV.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val versionInfoRes = Gson().fromJson(result, VersionInfoRes::class.java)
                    checkVersion(versionInfoRes.retRes)
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun checkVersion(versionInfo: VersionInfo) {
        if (versionInfo.v_num > BuildConfig.VERSION_CODE) {//有新的版本是否更新
            getMessageDialog(
                this,
                "发现新版本${versionInfo.v_title}，是否立即下载？",
                object : DialogUIListener() {
                    override fun onPositive() {
                        download(versionInfo.http_url)
                    }

                    override fun onNegative() {

                    }
                })
        } else {
            getSuccessDialog(this, "已经是最新版本！")
        }
    }

    private fun download(url: String) {
        downloadApk(this, "$ROOT_URL/$url")
    }

}