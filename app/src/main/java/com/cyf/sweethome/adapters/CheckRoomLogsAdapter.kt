package com.cyf.sweethome.adapters

import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.*
import kotlinx.android.synthetic.main.layout_myhouse_list_item.view.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class CheckRoomLogsAdapter(val data: ArrayList<CheckRoomLog>) :
    MyBaseAdapter(R.layout.layout_myhouse_list_item) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val checkRoomLog = data[position]
        holder.itemView.house_address.text = checkRoomLog.fw_title
        holder.itemView.house_status.text = CHECK_ROOM_STATUS[checkRoomLog.sh_status]
        holder.itemView.house_status.setTextColor(
            holder.itemView.context.resources.getColor(
                CHECK_ROOM_STATUS_COLOR[checkRoomLog.sh_status]
            )
        )
        holder.itemView.house_relation.text =
            CalendarUtil(checkRoomLog.create_time, true).format(CalendarUtil.YY_MM_DD_HH_MM)
    }

    override fun getItemCount(): Int = data.size
}