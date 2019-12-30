package com.cyf.heartservice.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.activities.PhotoViewActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_zxfx.*
import java.io.File

class ZXFXActivity : MyBaseActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        if (images.size > 0) {
            PhotoViewActivity.setData(images, true, 0)
            startActivity(Intent(this, PhotoViewActivity::class.java))
        }
    }

    companion object {
        val PAGE_CODE_ZX = 0 //装修
        val PAGE_CODE_FX = 1 //放行
    }

    var id = "0"
    var pageCode = PAGE_CODE_ZX
    var images = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_zxfx)
        id = intent.getStringExtra("id") ?: "0"
        pageCode = intent.getIntExtra("page_code", PAGE_CODE_ZX)
        if (pageCode == PAGE_CODE_FX) {
            setTitle("物品放行")
            fxLayout.visibility = View.VISIBLE
            zxLayout.visibility = View.GONE
        } else {
            setTitle("装修申请详情")
            zxLayout.visibility = View.VISIBLE
            fxLayout.visibility = View.GONE
        }
        getData()
    }

    private fun getData() {
        val map = mapOf(
            Pair("id", id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(
                if (pageCode == PAGE_CODE_ZX) ZXSQINFO.getInterface(map) else WPFXINFO.getInterface(
                    map
                )
            )
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {
                            finish()
                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val zxfxInfoRes = Gson().fromJson(result, ZXFXInfoRes::class.java)
                    fillViews(zxfxInfoRes.retRes)
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun fillViews(zxfxInfo: ZXFXInfo) {
        images.clear()
        address.text =
            zxfxInfo.xq_title + zxfxInfo.xqld_title + zxfxInfo.xqdy_title + zxfxInfo.xqfh_title
        beizhu.text = zxfxInfo.contents
        yezhu.text = zxfxInfo.xqyz_title
        yezhuPhone.text = zxfxInfo.xqyz_phone
        shenban.text = zxfxInfo.sbr_title
        shenbanCardNum.text = zxfxInfo.sbr_card_num
        shenbanPhone.text = zxfxInfo.sbr_phone
        if (zxfxInfo.file_url_wz.isBlank()) {
            image_1.visibility = View.GONE
        } else {
            image_1.visibility = View.VISIBLE
            images.add(zxfxInfo.file_url_wz.getImageUrl())
            Picasso.with(this).load(zxfxInfo.file_url_wz.getImageUrl()).into(image_1)
        }
        if (pageCode == PAGE_CODE_ZX) {
            zxName.text = zxfxInfo.zxfzr_title
            zxCardNum.text = zxfxInfo.zxfzr_card_num
            zxPhone.text = zxfxInfo.zxfzr_phone
            zxEndTime.text = zxfxInfo.zxqx
            zxCompany.text = zxfxInfo.zxdw
            yezhuCheck.text = zxfxInfo.yzqrfs
            image_2.visibility = View.GONE
        } else {
            yuanyin.text = zxfxInfo.blyy
            xingzhi.text = zxfxInfo.fwzt_title
            if (zxfxInfo.file_url_jz.isBlank()) {
                image_2.visibility = View.GONE
            } else {
                image_2.visibility = View.VISIBLE
                images.add(zxfxInfo.file_url_jz.getImageUrl())
                Picasso.with(this).load(zxfxInfo.file_url_jz.getImageUrl()).into(image_2)
            }
        }
        applyTime.text =
            CalendarUtil(zxfxInfo.create_time, true).format(CalendarUtil.YYYY_MM_DD_HH_MM)
        jsRen.text = zxfxInfo.jsr
        image_1.setOnClickListener(this)
        image_2.setOnClickListener(this)
    }
}