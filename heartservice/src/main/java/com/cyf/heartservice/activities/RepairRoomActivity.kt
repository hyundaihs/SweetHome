package com.cyf.heartservice.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.shuizu.myutillibrary.D
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_repair_room.*
import kotlinx.android.synthetic.main.layout_repair_room_item.view.*
import java.util.*

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
class RepairRoomActivity : MyBaseActivity() {

    private val repairRoomList = ArrayList<RepairRoomListItem>()
    private val repairRoomAdapter = RepairRoomAdapter(repairRoomList)
    private var type = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_repair_room)
        type = intent.getIntExtra("type", 1)
        setTitle(if (type == 1) "工单" else "服务通知")
        initViews()
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        listView.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        listView.addItemDecoration(
            RecyclerViewDivider(
                this,
                LinearLayoutManager.VERTICAL,
                DisplayUtils.dp2px(this, 15f),
                resources.getColor(android.R.color.transparent)
            )
        )
        listView.itemAnimator = DefaultItemAnimator()
        listView.isNestedScrollingEnabled = false
        listView.adapter = repairRoomAdapter
        listView.setEmptyViewRes(R.layout.layout_list_empty)
        listViewSwipe.setOnRefreshListener(object : SwipeRefreshAndLoadLayout.OnRefreshListener {
            override fun onRefresh() {
                refresh()
            }

            override fun onLoadMore(currPage: Int, totalPages: Int) {
                loadMore(currPage)
            }
        })
        repairRoomAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                if (type == 1) {
                    setRead(position)
                    val intent = Intent(view.context, WorkOrderDetailsActivity::class.java)
                    intent.putExtra("id", repairRoomList[position].row_id)
                    startActivity(intent)
                } else {
                    setRead(position)
                    val intent = Intent(view.context, CheckRoomDetailsActivity::class.java)
                    intent.putExtra("id", repairRoomList[position].row_id)
                    startActivity(intent)
                }
            }
        }
        refresh()
    }

    private fun setRead(pos: Int) {
        val map = mapOf(
            Pair("id", repairRoomList[pos].row_id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(SETYD.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    repairRoomList[pos].is_read = 1
                    repairRoomAdapter.notifyItemChanged(pos)
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun refresh() {
        getListData(listViewSwipe.currPage, true)
    }

    private fun loadMore(currPage: Int) {
        getListData(currPage, false)
    }

    private fun getListData(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page", page),
            Pair("page_size", 10),
            Pair("type_id", type)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(GDYFTZLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                    listViewSwipe.isRefreshing = false
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val repairRoomListRes = Gson().fromJson(result, RepairRoomListRes::class.java)
                    listViewSwipe.setTotalPages(repairRoomListRes.retCounts, 15)
                    if (isRefresh) {
                        repairRoomList.clear()
                    }
                    repairRoomList.addAll(repairRoomListRes.retRes)
                    repairRoomAdapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    inner class RepairRoomAdapter(val data: ArrayList<RepairRoomListItem>) :
        MyBaseAdapter(R.layout.layout_repair_room_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val repairRoomListItem = data[position]
            holder.itemView.name.text = repairRoomListItem.title
            holder.itemView.contents.text = repairRoomListItem.contents
            holder.itemView.time.text = CalendarUtil(
                repairRoomListItem.create_time,
                true
            ).format(CalendarUtil.YYYY_MM_DD_HH_MM)
            holder.itemView.isRead.visibility =
                if (repairRoomListItem.is_read == 0) View.VISIBLE else View.GONE
        }

        override fun getItemCount(): Int = data.size
    }
}