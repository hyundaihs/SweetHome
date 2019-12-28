package com.cyf.heartservice.activities

import android.content.Context
import android.os.Bundle
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_share_code.*

class MyShareCodeActivity : MyBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_share_code)
        setTitle("邀请客户")
        getShareCode()
    }

    private fun init(s: ShareCode) {
        Picasso.with(this).load(s.ewm_file_url.getImageUrl())
            .error(R.mipmap.ic_launcher)
            .into(shareCode)
        shareBtn.setOnClickListener {
            share()
        }
    }

    private fun getShareCode() {
        KevinRequest.build(this).apply {
            setRequestUrl(WDTGM.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val shareCodeRes = Gson().fromJson(result, ShareCodeRes::class.java)
                    init(shareCodeRes.retRes)
                }
            })
            setDialog()
            postRequest()
        }
    }

    private fun share() {

    }
}