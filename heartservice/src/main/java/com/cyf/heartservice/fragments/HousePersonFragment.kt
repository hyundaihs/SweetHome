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
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.FXKH
import com.cyf.heartservice.entity.JumingDetails
import com.cyf.heartservice.entity.JumingDetailsListRes
import com.cyf.heartservice.entity.getInterface
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_house_person.*
import kotlinx.android.synthetic.main.layout_house_person_list_item.view.*
import java.util.*

class HousePersonFragment(val id: String) : MyBaseFragment() {

    val data = ArrayList<JumingDetails>()
    val adapter = MyAdapter(data)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_house_person, container, false)
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
                setRequestUrl(FXKH.getInterface(map))
                setSuccessCallback(object : KevinRequest.SuccessCallback {
                    override fun onSuccess(context: Context, result: String) {
                        val jumingDetailsListRes =
                            Gson().fromJson(result, JumingDetailsListRes::class.java)
                        data.clear()
                        data.addAll(jumingDetailsListRes.retRes)
                        adapter.notifyDataSetChanged()
                    }
                })
                setDataMap(map)
                postRequest()
            }
        }
    }

    inner class MyAdapter(val data: ArrayList<JumingDetails>) :
        MyBaseAdapter(R.layout.layout_house_person_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val jumingDetails = data[position]
            holder.itemView.name.text = jumingDetails.title
            holder.itemView.sex.text = jumingDetails.sex
            holder.itemView.phone.text = jumingDetails.phone
            holder.itemView.cardNum.text = jumingDetails.card_num
            holder.itemView.shenfen.text = jumingDetails.xqyzsf_title
        }

        override fun getItemCount(): Int = data.size
    }
}