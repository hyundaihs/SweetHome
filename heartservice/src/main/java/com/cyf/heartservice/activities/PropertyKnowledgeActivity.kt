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
import com.cyf.heartservice.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_notify_list_item.view.*
import kotlinx.android.synthetic.main.layout_swipe_refresh_empty_recycleview.*
import java.util.ArrayList

class PropertyKnowledgeActivity  : MyBaseActivity(){

    private val notifyList = ArrayList<Notify>()
    private val notifyAdapter = NotifyAdapter(notifyList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_property_knowledge)
        setTitle("物业知识库")
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
        listView.adapter = notifyAdapter
        listView.setEmptyView(emptyView)
        listViewSwipe.setOnRefreshListener(object : SwipeRefreshAndLoadLayout.OnRefreshListener {
            override fun onRefresh() {
                refresh()
            }

            override fun onLoadMore(currPage: Int, totalPages: Int) {
                loadMore(currPage)
            }
        })
        notifyAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                val intent = Intent(this@PropertyKnowledgeActivity, NotifyDetailsActivity::class.java)
                intent.putExtra("id", notifyList[position].id)
                startActivity(intent)
            }
        }
        refresh()
    }

    private fun refresh() {
        getListData(listViewSwipe.currPage, true)
    }

    private fun loadMore(currPage: Int) {
        getListData(currPage, false)
    }

    private fun getListData(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", 10),
            Pair("page", page)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(ZSKLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                    listViewSwipe.isRefreshing = false
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val notifyListRes = Gson().fromJson(result, NotifyListRes::class.java)
                    listViewSwipe.setTotalPages(notifyListRes.retCounts, 15)
                    if (isRefresh) {
                        notifyList.clear()
                    }
                    notifyList.addAll(notifyListRes.retRes)
                    notifyAdapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    inner class NotifyAdapter(val data: ArrayList<Notify>) :
        MyBaseAdapter(R.layout.layout_notify_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val notify = data[position]
            holder.itemView.notifyTitle.text = notify.title
            holder.itemView.notifyTime.text = CalendarUtil(
                notify.create_time,
                true
            ).format(CalendarUtil.YYYY_MM_DD_HH_MM)
        }

        override fun getItemCount(): Int = data.size
    }
}