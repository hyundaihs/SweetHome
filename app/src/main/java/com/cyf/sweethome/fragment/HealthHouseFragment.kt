package com.cyf.sweethome.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.cyf.sweethome.R
import com.cyf.sweethome.activities.CheckBodyInfoDetailsActivity
import com.cyf.sweethome.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_health_house.*
import kotlinx.android.synthetic.main.layout_check_body_info_list_item.view.*
import kotlinx.android.synthetic.main.layout_swipe_refresh_empty_recycleview.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class HealthHouseFragment : BaseFragment(){

    private val data = ArrayList<CheckBodyInfo>()
    private val adapter = CheckBodyInfoListAdapter(data)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_health_house, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        val layoutManager = LinearLayoutManager(context)
        listView.layoutManager = layoutManager
        layoutManager.orientation = VERTICAL
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
        listView.setEmptyView(emptyView)
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
                val intent = Intent(view.context, CheckBodyInfoDetailsActivity::class.java)
                intent.putExtra("id", data[position].id)
                startActivity(intent)
            }
        }
        listViewSwipe.isRefreshing = true
        refresh()
    }

    private fun refresh() {
        getCheckBodyInfo(listViewSwipe.currPage, true)
    }

    private fun loadMore(currPage: Int) {
        getCheckBodyInfo(currPage)
    }


    private fun getCheckBodyInfo(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("page_size", "15"),
            Pair("page", page.toString())
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(TJBGJL.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    listViewSwipe.isRefreshing = false
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {

                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val checkBodyInfoListRes = Gson().fromJson(result, CheckBodyInfoListRes::class.java)
                    listViewSwipe.setTotalPages(checkBodyInfoListRes.retCounts, 15)
                    if (isRefresh) {
                        data.clear()
                    }
                    data.addAll(checkBodyInfoListRes.retRes)
                    adapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private class CheckBodyInfoListAdapter(val data: ArrayList<CheckBodyInfo>) :
        MyBaseAdapter(R.layout.layout_check_body_info_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val checkBodyInfo = data[position]
            holder.itemView.name.text = checkBodyInfo.title
            holder.itemView.sex.text = "【${checkBodyInfo.sex}】"
            holder.itemView.num.text = checkBodyInfo.numbers
            holder.itemView.group.text = checkBodyInfo.sub_title
            holder.itemView.time.text = checkBodyInfo.dates
        }

        override fun getItemCount(): Int = data.size
    }
}