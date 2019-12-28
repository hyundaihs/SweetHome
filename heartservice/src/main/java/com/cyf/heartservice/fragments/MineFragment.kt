package com.cyf.heartservice.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.cyf.heartservice.HeartService
import com.cyf.heartservice.R
import com.cyf.heartservice.activities.FeedbackActivity
import com.cyf.heartservice.activities.HelpActivity
import com.cyf.heartservice.activities.SettingActivity
import com.cyf.heartservice.entity.getImageUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mine.*
import org.jetbrains.anko.toast

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
class MineFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        HeartService.userInfo?.let {
            Picasso.with(context).load(it.file_url.getImageUrl())
                .resize(300, 300)
                .error(R.mipmap.contact_title_no_image)
                .into(minePhoto)
            mineName.text = it.title
            mineInfo.text = "${it.sf_title}  ${it.xq_title}"
        }
        layoutInfo.setOnClickListener {
            it.context.toast("本小区暂无此功能")
        }
        help.setOnClickListener {
            startActivity(Intent(activity, HelpActivity::class.java))
        }
        opinion.setOnClickListener {
            startActivity(Intent(activity, FeedbackActivity::class.java))
        }
        set.setOnClickListener {
            startActivityForResult(Intent(activity, SettingActivity::class.java), 104)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 105) {
            activity?.finish()
        }
    }
}