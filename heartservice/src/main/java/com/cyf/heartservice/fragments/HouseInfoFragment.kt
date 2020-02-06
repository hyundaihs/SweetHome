package com.cyf.heartservice.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.android.shuizu.myutillibrary.fragment.MyBaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_house_info.*
import kotlinx.android.synthetic.main.layout_house_info_list_item.view.*

class HouseInfoFragment(val id: String) : MyBaseFragment() {

    val data = ArrayList<HouseInfo>()
    val adapter = MyAdapter(data)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_house_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        getData()
    }

    private fun init() {
        val layoutManager = LinearLayoutManager(context)
        houseList.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        houseList.addItemDecoration(
            RecyclerViewDivider(
                context,
                LinearLayoutManager.HORIZONTAL,
                DisplayUtils.dp2px(context, 15f),
                resources.getColor(R.color.color_F6F6F8)
            )
        )
        houseList.isNestedScrollingEnabled = false
        houseList.adapter = adapter
    }

    private fun getData() {
        val map = mapOf(
            Pair("xqyz_id", id)
        )
        activity?.let {
            KevinRequest.build(it).apply {
                setRequestUrl(YZFWXX.getInterface(map))
                setSuccessCallback(object : KevinRequest.SuccessCallback {
                    override fun onSuccess(context: Context, result: String) {
                        val houseInfoListRes = Gson().fromJson(result, HouseInfoListRes::class.java)
                        data.clear()
                        data.addAll(houseInfoListRes.retRes)
                        adapter.notifyDataSetChanged()
                    }
                })
                setDataMap(map)
                postRequest()
            }
        }
    }

    inner class MyAdapter(val data: ArrayList<HouseInfo>) :
        MyBaseAdapter(R.layout.layout_house_info_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.itemView.danyuan.text =
                "${data[position].xqld_title}${data[position].xqdy_title}"
            holder.itemView.fanghao.text = data[position].xqfh_title
            holder.itemView.status.text = if (data[position].yf_status == "1") "通过" else "未通过"
            holder.itemView.time.text =
                CalendarUtil(data[position].create_time,true).format(CalendarUtil.YYYY_MM_DD)
        }

        override fun getItemCount(): Int = data.size
    }
}