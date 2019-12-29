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
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.fragment.MyBaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.cyf.sweethome.R
import com.cyf.sweethome.activities.MemberInfoDetailsActivity
import com.cyf.sweethome.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_member_list_item.view.*
import kotlinx.android.synthetic.main.layout_swipe_refresh_empty_recycleview.*

class MemberListFragmentMy(val id: String) : MyBaseFragment() {

    private val data = ArrayList<MemberInfo>()
    private val adapter = MemberListAdapter(data)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        val layoutManager = LinearLayoutManager(context)
        listView.layoutManager = layoutManager
        layoutManager.orientation = VERTICAL
        listView.addItemDecoration(LineDecoration(context, LineDecoration.HORIZONTAL))
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
                val intent = Intent(view.context, MemberInfoDetailsActivity::class.java)
                intent.putExtra("id", data[position].id)
                startActivity(intent)
            }
        }
        listViewSwipe.isRefreshing = true
        refresh()
    }

    private fun refresh() {
        getMessageInfo(listViewSwipe.currPage, true)
    }

    private fun loadMore(currPage: Int) {
        getMessageInfo(currPage)
    }


    private fun getMessageInfo(page: Int, isRefresh: Boolean = false) {
        val map = mapOf(
            Pair("stype_id", id),
            Pair("page_size", "15"),
            Pair("page", page.toString())
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(DJLISTS.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    listViewSwipe.isRefreshing = false
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {
                            activity?.finish()
                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val memberInfoListRes = Gson().fromJson(result, MemberInfoListRes::class.java)
                    listViewSwipe.setTotalPages(memberInfoListRes.retCounts, 15)
                    if (isRefresh) {
                        data.clear()
                    }
                    data.addAll(memberInfoListRes.retRes)
                    adapter.notifyDataSetChanged()
                    listViewSwipe.isRefreshing = false
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private class MemberListAdapter(val data: ArrayList<MemberInfo>) :
        MyBaseAdapter(R.layout.layout_member_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val memberInfo = data[position]
            Picasso.with(holder.itemView.context).load(memberInfo.file_url.getImageUrl())
                .resize(300, 300)
                .into(holder.itemView.imgView)
            holder.itemView.title.text = memberInfo.title
            holder.itemView.time.text = CalendarUtil(memberInfo.create_time, true).format(
                CalendarUtil.STANDARD
            )
        }

        override fun getItemCount(): Int = data.size
    }
}