package com.cyf.sweethome.activities

import android.os.Bundle
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.cyf.sweethome.BuildConfig
import com.cyf.sweethome.R
import kotlinx.android.synthetic.main.activity_about_us.*


class AboutUsActivity : MyBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_about_us)
        setTitle("关于我们")
        init()
    }

    private fun init() {

        checkVersion.setOnClickListener {
            requestVersionInfo()
        }
    }

    //获取版本信息
    private fun requestVersionInfo() {

    }

    private fun checkVersion(serverCode: Int) {
        if(serverCode > BuildConfig.VERSION_CODE){//有新的版本是否更新
            //增加提示
            //进入下载

            //进入下载
        }
    }

//    private fun getHtml() {
//        KevinRequest.build(this).apply {
//            setRequestUrl(GYWM.getInterface())
//            setErrorCallback(object : KevinRequest.ErrorCallback {
//                override fun onError(context: Context, error: String) {
//                    getErrorDialog(context, error)
//                }
//            })
//            setSuccessCallback(object : KevinRequest.SuccessCallback {
//                override fun onSuccess(context: Context, result: String) {
//                    val aboutUsRes = Gson().fromJson(result, AboutUsRes::class.java)
//                    aboutUs.loadLocalHtml(aboutUsRes.retRes.app_contents)
//                }
//            })
//            setDialog()
//            postRequest()
//        }
//    }
}