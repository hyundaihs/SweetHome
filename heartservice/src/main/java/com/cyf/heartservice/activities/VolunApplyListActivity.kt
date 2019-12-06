package com.cyf.heartservice.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.HDSQJL
import com.cyf.heartservice.entity.VolunApplyInfo
import com.cyf.heartservice.entity.VolunApplyInfoListRes
import com.cyf.heartservice.entity.getInterface
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_volun_apply_list.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import kotlinx.android.synthetic.main.layout_member_apply_list_item.view.*

class VolunApplyListActivity : MyBaseActivity() {

    private val data = ArrayList<VolunApplyInfo>()
    private val adapter = VolunApplyListAdapter(data)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_volun_apply_list)
        setTitle("志愿者认证")
        init()
    }

    private fun init() {
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
        listView.setEmptyView(listEmptyView)
        listViewSwipe.setOnRefreshListener(object : SwipeRefreshAndLoadLayout.OnRefreshListener {
            override fun onRefresh() {
                refresh()
            }

            override fun onLoadMore(currPage: Int, totalPages: Int) {
                loadMore(currPage)
            }
        })
        listView.adapter = adapter
        adapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                val intent = Intent(view.context, VolunApplyDetailsActivity::class.java)
                intent.putExtra("id", data[position].id)
                startActivityForResult(intent, 1001)
            }
        }
        listViewSwipe.isRefreshing = true
        refresh()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == 1002) {
            listViewSwipe.isRefreshing = true
            refresh()
        }
    }

    private fun refresh() {
        getMemberApplyList(listViewSwipe.currPage, true)
    }

    private fun loadMore(currPage: Int) {
        getMemberApplyList(currPage)
    }

    private fun getMemberApplyList(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "15"),
            Pair("page", page.toString())
        )
        KevinRequest.build(this).apply {
            setRequestUrl(HDSQJL.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    listViewSwipe.isRefreshing = false
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
                    val volunApplyInfoListRes =
                        Gson().fromJson(result, VolunApplyInfoListRes::class.java)
                    listViewSwipe.setTotalPages(volunApplyInfoListRes.retCounts, 15)
                    if (isRefresh) {
                        data.clear()
                    }
                    data.addAll(volunApplyInfoListRes.retRes)
                    adapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private class VolunApplyListAdapter(val data: ArrayList<VolunApplyInfo>) :
        MyBaseAdapter(R.layout.layout_member_apply_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val volunApplyInfo = data[position]
            holder.itemView.name.text = volunApplyInfo.title
            holder.itemView.status.text = when (volunApplyInfo.sh_status) {
                1 -> {
                    holder.itemView.status.setTextColor(holder.itemView.context.resources.getColor(R.color.color_FEB900))
                    "待审核"
                }
                2 -> {
                    holder.itemView.status.setTextColor(holder.itemView.context.resources.getColor(R.color.color_38BEA5))
                    "已通过"
                }
                3 -> {
                    holder.itemView.status.setTextColor(holder.itemView.context.resources.getColor(R.color.color_F86A6A))
                    "已拒绝"
                }
                else -> {
                    holder.itemView.status.setTextColor(holder.itemView.context.resources.getColor(R.color.color_FEB900))
                    "未申请"
                }
            }
            holder.itemView.time.text = CalendarUtil(volunApplyInfo.create_time, true).format(
                CalendarUtil.STANDARD
            )
        }

        override fun getItemCount(): Int = data.size
    }
}