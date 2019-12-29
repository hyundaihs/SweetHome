package com.cyf.sweethome.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome.Companion.CHOOSE_BUILDING_RESULT
import com.cyf.sweethome.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_choose_room.*
import kotlinx.android.synthetic.main.layout_choose_building_list_item.view.*

class ChooseRoomActivity : MyBaseActivity() {

    companion object {
        val BUILDING = 0
        val FLOOR = 1
        val ROOM = 2
    }

    var chooseType = BUILDING

    val buildData = ArrayList<Building>()
    val buildAdapter = BuildingAdapter(buildData)
    val floorData = ArrayList<Floor>()
    val floorAdapter = FloorAdapter(floorData)
    val roomData = ArrayList<Room>()
    val roomAdapter = RoomAdapter(roomData)

    var chooseBuildingName = ""
    var chooseFloorName = ""
    var chooseRoomName = ""
    var communityId = "0"
    var communityName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_choose_room)
        communityId = intent.getStringExtra("id") ?: ""
        communityName = intent.getStringExtra("name") ?: ""

        setTitle("选择房间")
        initRecyclerView(chooseBuilding, buildAdapter)
        initRecyclerView(chooseFloor, floorAdapter)
        initRecyclerView(chooseRoom, roomAdapter)
        getBuildingData()
        buildAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                chooseBuildingName = buildData[position].title
                floorData.clear()
                floorData.addAll(buildData[position].lists)
                floorAdapter.notifyDataSetChanged()
                layoutFloor.visibility = if (floorData.size > 0) View.VISIBLE else View.GONE
                chooseType = FLOOR
                refreshData()
            }
        }
        floorAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                chooseFloorName = floorData[position].title
                roomData.clear()
                roomData.addAll(floorData[position].lists)
                roomAdapter.notifyDataSetChanged()
                layoutRoom.visibility = if (roomData.size > 0) View.VISIBLE else View.GONE
                chooseType = ROOM
                refreshData()
            }
        }
        roomAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                chooseRoomName = roomData[position].title
                val intent = Intent()
                intent.putExtra(
                    "chooseId",
                    roomData[position].id
                )
                intent.putExtra(
                    "chooseName",
                    "${chooseBuildingName} ${chooseFloorName} ${chooseRoomName}"
                )
                setResult(CHOOSE_BUILDING_RESULT, intent)
                finish()
            }
        }
        refreshData()
    }

    private fun initRecyclerView(recyclerView: RecyclerView, adapter: MyBaseAdapter) {
        chooseText.addTextChangedListener {
            val isShow = it?.isEmpty() ?: true
            chooseText.visibility = if (isShow) View.GONE else View.VISIBLE
        }
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.addItemDecoration(
            RecyclerViewDivider(
                this,
                LinearLayoutManager.HORIZONTAL,
                DisplayUtils.dp2px(this, 1f),
                resources.getColor(R.color.color_727C8E)
            )
        )
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
    }

    private fun refreshData() {
        chooseText.text =
            "您已选择：${communityName} ${chooseBuildingName} ${chooseFloorName} ${chooseRoomName}"
    }

    private fun getBuildingData() {
        val map = mapOf(
            Pair("xq_id", communityId)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(XQDYLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val buildingListRes = Gson().fromJson(result, BuildingListRes::class.java)
                    buildData.clear()
                    buildData.addAll(buildingListRes.retRes)
                    buildAdapter.notifyDataSetChanged()
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    inner class BuildingAdapter(val data: ArrayList<Building>) :
        MyBaseAdapter(R.layout.layout_choose_building_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.itemView.text.text = data[position].title
        }

        override fun getItemCount(): Int = data.size
    }

    inner class FloorAdapter(val data: ArrayList<Floor>) :
        MyBaseAdapter(R.layout.layout_choose_building_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.itemView.text.text = data[position].title
        }

        override fun getItemCount(): Int = data.size
    }

    inner class RoomAdapter(val data: ArrayList<Room>) :
        MyBaseAdapter(R.layout.layout_choose_building_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.itemView.text.text = data[position].title
        }

        override fun getItemCount(): Int = data.size
    }

}