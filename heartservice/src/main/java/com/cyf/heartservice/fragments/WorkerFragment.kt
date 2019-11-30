package com.cyf.heartservice.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.android.shuizu.myutillibrary.activities.PhotoViewActivity
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.utils.PictureSelectorStart
import com.cyf.heartservice.R
import com.cyf.heartservice.activities.MemberApplyListActivity
import com.cyf.heartservice.activities.RepairRoomActivity
import kotlinx.android.synthetic.main.fragment_worker.*
import kotlinx.android.synthetic.main.layout_tab_worker_list_item.view.*
import org.jetbrains.anko.toast

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
class WorkerFragment : BaseFragment() {

    private val imageData: List<String> = listOf(
        "工单处理",
        "客服查询",
        "APP推广",
        "物业知识库",
        "居民认证",
        "志愿者认证",
        "党员认证",
        "停车管理",
        "管家品质巡查",
        "品质检查",
        "居民信息全览",
        "房屋集成视图"
    )
    private val srcData = listOf(
        R.mipmap.gong_dan_chu_li,
        R.mipmap.ke_fu_cha_xun,
        R.mipmap.app_tui_guang,
        R.mipmap.wu_ye_zhi_shi,
        R.mipmap.ju_ming_ren_zheng,
        R.mipmap.zhi_yuan_zhe_ren_zheng,
        R.mipmap.dang_yuan_ren_zheng,
        R.mipmap.ting_che_guan_li,
        R.mipmap.guan_jia_pin_zhi,
        R.mipmap.ping_zhi_jian_cha,
        R.mipmap.ju_ming_xin_xi,
        R.mipmap.fang_wu_ji_cheng
    )
    private val bgData = listOf(
        R.drawable.rect_ffb700_corner_5,
        R.drawable.rect_1777fe_corner_5,
        R.drawable.rect_35bda5_corner_5,
        R.drawable.rect_f96a6a_corner_5,
        R.drawable.rect_f96a6a_corner_5,
        R.drawable.rect_35bda5_corner_5,
        R.drawable.rect_1777fe_corner_5,
        R.drawable.rect_ffb700_corner_5,
        R.drawable.rect_ffb700_corner_5,
        R.drawable.rect_1777fe_corner_5,
        R.drawable.rect_35bda5_corner_5,
        R.drawable.rect_f96a6a_corner_5
    )
    private val imageAdapter = WorkerAdapter(imageData)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_worker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val gridLayoutManager = GridLayoutManager(context, 4)
        workerList.layoutManager = gridLayoutManager
        workerList.addItemDecoration(
            GridDivider(
                context,
                (activity as Context).dp2px(20f).toInt(),
                4
            )
        )
        workerList.itemAnimator = DefaultItemAnimator()
        workerList.isNestedScrollingEnabled = false
        workerList.adapter = imageAdapter
        imageAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                when (position) {
                    0 -> {
                        val intent = Intent(context, RepairRoomActivity::class.java)
                        intent.putExtra("type", 1)
                        startActivity(intent)
                    }
                    6->{
                        val intent = Intent(context, MemberApplyListActivity::class.java)
                        startActivity(intent)
                    }
                    else -> {
                        view.context.toast("建设中...")
                    }
                }
            }
        }
    }

    inner class WorkerAdapter(val data: List<String>) :
        MyBaseAdapter(R.layout.layout_tab_worker_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.itemView.titleText.text = data[position]
            holder.itemView.titleImage.setImageResource(srcData[position])
            holder.itemView.titleImage.setBackgroundResource(bgData[position])
        }

        override fun getItemCount(): Int = data.size
    }
}