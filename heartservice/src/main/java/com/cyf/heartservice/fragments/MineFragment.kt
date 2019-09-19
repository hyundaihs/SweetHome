package com.cyf.heartservice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.cyf.heartservice.HeartService
import com.cyf.heartservice.R
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
            it.context.toast("建设中...")
        }
        help.setOnClickListener {
            it.context.toast("建设中...")
        }
        opinion.setOnClickListener {
            it.context.toast("建设中...")
        }
        set.setOnClickListener {
            it.context.toast("建设中...")
        }
    }
}