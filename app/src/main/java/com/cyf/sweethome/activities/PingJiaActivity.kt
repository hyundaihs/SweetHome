package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.BSBXINFO
import com.cyf.sweethome.entity.BSBXPJ
import com.cyf.sweethome.entity.WorkOrderDetailsRes
import com.cyf.sweethome.entity.getInterface
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_ping_jia.*
import org.jetbrains.anko.toast

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/18/018.
 */
class PingJiaActivity : MyBaseActivity(), View.OnClickListener {
    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.star1 -> {
                setColorForStars(1)
            }
            R.id.star2 -> {
                setColorForStars(2)
            }
            R.id.star3 -> {
                setColorForStars(3)
            }
            R.id.star4 -> {
                setColorForStars(4)
            }
            R.id.star5 -> {
                setColorForStars(5)
            }
        }
    }

    private var id: String = "0"
    private var currStar = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_ping_jia)
        setTitle("服务评价")
        id = intent.getStringExtra("id") as String
        initViews()
    }

    private fun initViews() {
        star1.setOnClickListener(this)
        star2.setOnClickListener(this)
        star3.setOnClickListener(this)
        star4.setOnClickListener(this)
        star5.setOnClickListener(this)
        submit.setOnClickListener {
            if (currStar <= 0) {
                toast("请选择星级")
            } else {
                submit()
            }

        }
    }

    private fun submit() {
        val map = mapOf(
            Pair("id", id),
            Pair("pj", currStar),
            Pair("title", pjContent.text.toString())
        )
        KevinRequest.build(this).apply {
            setRequestUrl(BSBXPJ.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    getSuccessDialog(context, "操作成功", object : DialogUIListener() {
                        override fun onPositive() {
                            setResult(101)
                            finish()
                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun setColorForStars(num: Int) {
        currStar = num
        when (num) {
            1 -> {
                star1.setImageResource(R.mipmap.red_star)
                star2.setImageResource(R.mipmap.gray_star)
                star3.setImageResource(R.mipmap.gray_star)
                star4.setImageResource(R.mipmap.gray_star)
                star5.setImageResource(R.mipmap.gray_star)
                hint.setTextColor(resources.getColor(R.color.colorAccent))
                hint.text = "非常不满意"
            }
            2 -> {
                star1.setImageResource(R.mipmap.red_star)
                star2.setImageResource(R.mipmap.red_star)
                star3.setImageResource(R.mipmap.gray_star)
                star4.setImageResource(R.mipmap.gray_star)
                star5.setImageResource(R.mipmap.gray_star)
                hint.setTextColor(resources.getColor(R.color.colorAccent))
                hint.text = "不满意"
            }
            3 -> {
                star1.setImageResource(R.mipmap.red_star)
                star2.setImageResource(R.mipmap.red_star)
                star3.setImageResource(R.mipmap.red_star)
                star4.setImageResource(R.mipmap.gray_star)
                star5.setImageResource(R.mipmap.gray_star)
                hint.setTextColor(resources.getColor(R.color.colorAccent))
                hint.text = "一般"
            }
            4 -> {
                star1.setImageResource(R.mipmap.yellow_star)
                star2.setImageResource(R.mipmap.yellow_star)
                star3.setImageResource(R.mipmap.yellow_star)
                star4.setImageResource(R.mipmap.yellow_star)
                star5.setImageResource(R.mipmap.gray_star)
                hint.setTextColor(resources.getColor(R.color.color_FFB700))
                hint.text = "满意"
            }
            5 -> {
                star1.setImageResource(R.mipmap.yellow_star)
                star2.setImageResource(R.mipmap.yellow_star)
                star3.setImageResource(R.mipmap.yellow_star)
                star4.setImageResource(R.mipmap.yellow_star)
                star5.setImageResource(R.mipmap.yellow_star)
                hint.setTextColor(resources.getColor(R.color.color_FFB700))
                hint.text = "非常满意"
            }
        }
    }
}