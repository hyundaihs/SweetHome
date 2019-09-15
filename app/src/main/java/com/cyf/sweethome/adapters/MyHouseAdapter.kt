package com.cyf.sweethome.adapters

import android.view.View
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.HOUSE_STATUS
import com.cyf.sweethome.entity.HOUSE_STATUS_COLOR
import com.cyf.sweethome.entity.HouseListItem
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.layout_myhouse_list_item.view.*
import java.util.ArrayList

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class MyHouseAdapter(val data: ArrayList<HouseListItem>) :
    MyBaseAdapter(R.layout.layout_myhouse_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val houseListItem = data[position]
        holder.itemView.house_address.text = houseListItem.fw_title
        holder.itemView.house_status.text = HOUSE_STATUS[houseListItem.sh_status]
        holder.itemView.house_status.setTextColor(
            holder.itemView.context.resources.getColor(
                HOUSE_STATUS_COLOR[houseListItem.sh_status]
            )
        )
        holder.itemView.house_relation.text = houseListItem.xqyzsf_title
    }

    override fun getItemCount(): Int = data.size
}