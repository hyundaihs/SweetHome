package com.cyf.sweethome.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.shuizu.myutillibrary.D
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.cyf.sweethome.R
import com.cyf.sweethome.activities.WorkOrderDetailsActivity
import com.cyf.sweethome.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_work_order_list.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import kotlinx.android.synthetic.main.layout_work_order_item.view.*
import java.util.*

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/16.
 */
class WorkOrderListFragment(val type: Int) : BaseFragment() {

    private val workOrderList = ArrayList<WorkOrderListItem>()
    private val workOrderAdapter = WorkOrderAdapter(workOrderList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_work_order_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(context)
        listView.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        listView.addItemDecoration(
            RecyclerViewDivider(
                activity as Context,
                LinearLayoutManager.VERTICAL,
                DisplayUtils.dp2px(activity as Context, 15f),
                resources.getColor(android.R.color.transparent)
            )
        )
        listView.itemAnimator = DefaultItemAnimator()
        listView.isNestedScrollingEnabled = false
        listView.adapter = workOrderAdapter
        listView.setEmptyView(listEmptyView)
        listViewSwipe.setOnRefreshListener(object : SwipeRefreshAndLoadLayout.OnRefreshListener {
            override fun onRefresh() {
                refresh()
            }

            override fun onLoadMore(currPage: Int, totalPages: Int) {
                loadMore(currPage)
            }
        })
        workOrderAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                val intent = Intent(context, WorkOrderDetailsActivity::class.java)
                intent.putExtra("id", workOrderList[position].id)
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
            Pair("page", page),
            Pair("sh_status", type)
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(BSBXLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                    listViewSwipe.isRefreshing = false
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val workOrderListRes = Gson().fromJson(result, WorkOrderListRes::class.java)
                    listViewSwipe.setTotalPages(workOrderListRes.retCounts, 15)
                    if (isRefresh) {
                        workOrderList.clear()
                    }
                    workOrderList.addAll(workOrderListRes.retRes)
                    workOrderAdapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    inner class WorkOrderAdapter(val data: ArrayList<WorkOrderListItem>) :
        MyBaseAdapter(R.layout.layout_work_order_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val workOrderListItem = data[position]
            holder.itemView.workOrderType.text = workOrderListItem.xqbsbxlx_title
            holder.itemView.workOrderStatus.text = workOrderListItem.sh_title
            holder.itemView.workOrderAddress.text = workOrderListItem.fw_title
            holder.itemView.workOrderTime.text = CalendarUtil(
                workOrderListItem.create_time,
                true
            ).format(CalendarUtil.YYYY_MM_DD_HH_MM)
        }

        override fun getItemCount(): Int = data.size
    }
}