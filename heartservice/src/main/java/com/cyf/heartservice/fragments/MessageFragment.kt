package com.cyf.heartservice.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.cyf.heartservice.R
import com.cyf.heartservice.activities.RepairRoomActivity
import com.cyf.heartservice.entity.RepairRoomListItem
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.layout_tab_message_list_item.view.*

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
class MessageFragment : BaseFragment() {

    private val data = ArrayList<RepairRoomListItem>()
    private val adapter = TabMessageAdapter(data)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(context)
        tabMessageList.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        tabMessageList.addItemDecoration(
            RecyclerViewDivider(
                context,
                LinearLayoutManager.VERTICAL,
                DisplayUtils.dp2px(context, 1f),
                resources.getColor(R.color.color_C6BFC2)
            )
        )
        tabMessageList.itemAnimator = DefaultItemAnimator()
        tabMessageList.isNestedScrollingEnabled = false
        tabMessageList.adapter = adapter
        adapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                when (position) {
                    0 -> {

                    }
                    1 -> {
                        val intent = Intent(context, RepairRoomActivity::class.java)
                        intent.putExtra("type", 1)
                        startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(context, RepairRoomActivity::class.java)
                        intent.putExtra("type", 2)
                        startActivity(intent)
                    }
                }
            }
        }
        setData()
        deleteSearch.setOnClickListener {
            searchText.setText("")
        }
    }

    private val bgs = listOf(
        R.drawable.rect_1777fe_corner_5,
        R.drawable.rect_ffb700_corner_5,
        R.drawable.rect_35bda5_corner_5
    )

    private val pics = listOf(
        R.mipmap.headphones,
        R.mipmap.gong_dan_chu_li,
        R.mipmap.bell
    )

    private fun setData() {
        data.add(RepairRoomListItem(0, "0", "小秘书", "最新的", 111111111))
        data.add(RepairRoomListItem(0, "0", "工单", "最新的", 111111111))
        data.add(RepairRoomListItem(0, "0", "服务通知", "最新的", 111111111))
        adapter.notifyDataSetChanged()
    }

    inner class TabMessageAdapter(val data: java.util.ArrayList<RepairRoomListItem>) :
        MyBaseAdapter(R.layout.layout_tab_message_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val repairRoomListItem = data[position]
            holder.itemView.icon.setBackgroundResource(bgs[position])
            holder.itemView.icon.setImageResource(pics[position])
            holder.itemView.title.text = repairRoomListItem.title
            holder.itemView.message.text = repairRoomListItem.contents
            holder.itemView.time.text = CalendarUtil(
                repairRoomListItem.create_time,
                true
            ).format(CalendarUtil.YYYY_MM_DD_HH_MM)
        }

        override fun getItemCount(): Int = data.size
    }
}