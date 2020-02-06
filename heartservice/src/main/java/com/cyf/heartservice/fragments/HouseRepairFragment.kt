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
import kotlinx.android.synthetic.main.fragment_house_repair.*
import kotlinx.android.synthetic.main.layout_house_repair_list_item.view.*
import java.util.ArrayList

class HouseRepairFragment(val id: String) : MyBaseFragment() {

    val data = ArrayList<Repair>()
    val adapter = MyAdapter(data)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_house_repair, container, false)
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
            Pair("xqfh_id", id)
        )
        activity?.let {
            KevinRequest.build(it).apply {
                setRequestUrl(BSBXLISTS.getInterface(map))
                setSuccessCallback(object : KevinRequest.SuccessCallback {
                    override fun onSuccess(context: Context, result: String) {
                        val repairListRes =
                            Gson().fromJson(result, RepairListRes::class.java)
                        data.clear()
                        data.addAll(repairListRes.retRes)
                        adapter.notifyDataSetChanged()
                    }
                })
                setDataMap(map)
                postRequest()
            }
        }
    }

    inner class MyAdapter(val data: ArrayList<Repair>) :
        MyBaseAdapter(R.layout.layout_house_repair_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val repair = data[position]
            holder.itemView.type.text = repair.xqbsbxlx_title
            holder.itemView.status.text = when (repair.sh_status) {
                1 -> {
                    "待接单"
                }
                2 -> {
                    "待处理"
                }
                3 -> {
                    "待评价"
                }
                4 -> {
                    "已完成"
                }
                else -> ""
            }
            holder.itemView.address.text = repair.fw_title
            holder.itemView.time.text =
                CalendarUtil(repair.create_time, true).format(CalendarUtil.YYYY_MM_DD_HH_MM)
        }

        override fun getItemCount(): Int = data.size
    }
}