package com.cyf.sweethome.activities

import android.os.Bundle
import android.view.View
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.cyf.sweethome.R
import kotlinx.android.synthetic.main.activity_ping_jia.*

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

    private var id: String? = null
    private var currStar = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_ping_jia)
        setTitle("服务评价")
        id = intent.getStringExtra("id")
        initViews()
    }

    private fun initViews() {
        star1.setOnClickListener(this)
        star2.setOnClickListener(this)
        star3.setOnClickListener(this)
        star4.setOnClickListener(this)
        star5.setOnClickListener(this)
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