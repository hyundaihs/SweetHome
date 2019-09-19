package com.cyf.heartservice.fragments

import android.content.Context
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
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.heartservice.R
import com.cyf.heartservice.activities.RepairRoomActivity
import com.cyf.heartservice.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.layout_tab_message_list_item.view.*
import org.jetbrains.anko.toast

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
class MessageFragment : BaseFragment() {

    private val data = ArrayList<Msg>()
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
                        view.context.toast("建设中...")
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
        deleteSearch.setOnClickListener {
            searchText.setText("")
        }
        getListData()
    }

    private fun getListData(){
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(XXZX.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val msgListRes = Gson().fromJson(result, MsgListRes::class.java)
                    data.clear()
                    data.addAll(msgListRes.retRes)
                    adapter.notifyDataSetChanged()
                }
            })
            setDialog()
            postRequest()
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

    inner class TabMessageAdapter(val data: ArrayList<Msg>) :
        MyBaseAdapter(R.layout.layout_tab_message_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val repairRoomListItem = data[position]
            holder.itemView.photo.setBackgroundResource(bgs[position])
            holder.itemView.photo.setImageResource(pics[position])
            holder.itemView.name.text = repairRoomListItem.title
            holder.itemView.contents.text = repairRoomListItem.contents
            holder.itemView.time.text = repairRoomListItem.date_str
        }

        override fun getItemCount(): Int = data.size
    }
}