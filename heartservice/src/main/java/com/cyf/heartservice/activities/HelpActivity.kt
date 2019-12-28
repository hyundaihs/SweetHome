package com.cyf.heartservice.activities

import android.content.Context
import android.os.Bundle
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.loadLocalHtml
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.BZSC
import com.cyf.heartservice.entity.HelpContentRes
import com.cyf.heartservice.entity.getInterface
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity :MyBaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_help)
        setTitle("使用手册")
        getHtml()
    }

    private fun getHtml() {
        KevinRequest.build(this).apply {
            setRequestUrl(BZSC.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val helpContentRes = Gson().fromJson(result, HelpContentRes::class.java)
                    aboutUs.loadLocalHtml(helpContentRes.retRes.app_contents)
                }
            })
            setDialog()
            postRequest()
        }
    }
}