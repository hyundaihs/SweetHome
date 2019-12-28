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
    val building = ArrayList<Building>()
    val data = ArrayList<String>()
    val adapter = ListItemAdapter(data)
    var chooseBuilding = 0
    var chooseFloor = 0
    var chooseRoom = 0
    var communityId = "0"
    var communityName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_choose_room)
        communityId = intent.getStringExtra("id")?:""
        communityName = intent.getStringExtra("name")?:""

        setTitle("选择房间")
        addRightStringBtn("完成", View.OnClickListener {
            val intent = Intent()
            intent.putExtra("choose_building", getChooseStr())
            setResult(155, intent)
            chooseType = BUILDING
            finish()
        })
        init()
    }

    private fun getChooseStr(): String {
        var rel = "您已选择：communityName"
        when (chooseType) {
            BUILDING -> {
                rel += building[chooseBuilding].title
            }
            FLOOR -> {
                rel += building[chooseBuilding].title
                rel += building[chooseBuilding].lists[chooseFloor].title
            }
            ROOM -> {
                rel += building[chooseBuilding].title
                rel += building[chooseBuilding].lists[chooseFloor].title
                rel += building[chooseBuilding].lists[chooseFloor].lists[chooseRoom].title
            }
        }
        return rel
    }

    private fun init() {
        chooseText.addTextChangedListener {
            val isShow = it?.isEmpty() ?: true
            chooseText.visibility = if (isShow) View.GONE else View.VISIBLE
        }
        val layoutManager = LinearLayoutManager(this)
        chooseList.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        chooseList.addItemDecoration(
            RecyclerViewDivider(
                this,
                LinearLayoutManager.VERTICAL,
                DisplayUtils.dp2px(this, 1f),
                resources.getColor(R.color.color_727C8E)
            )
        )
        chooseList.itemAnimator = DefaultItemAnimator()
        chooseList.isNestedScrollingEnabled = false
        chooseList.adapter = adapter
        adapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                when (chooseType) {
                    BUILDING -> {
                        chooseBuilding = position
                        chooseType = FLOOR
                    }
                    FLOOR -> {
                        chooseFloor = position
                        chooseType = ROOM
                    }
                    ROOM -> {
                        chooseRoom = position
                    }
                }
                refreshData()
            }
        }
        getBuildingData()
        refreshData()
    }

    override fun finish() {
        when (chooseType) {
            BUILDING -> {
                super.finish()
            }
            FLOOR -> {
                chooseType = BUILDING
                refreshData()
            }
            ROOM -> {
                chooseRoom = FLOOR
                refreshData()
            }
        }
    }

    private fun refreshData() {
        when (chooseType) {
            BUILDING -> {
                for (i in 0 until building.size) {
                    data.clear()
                    data.add(building[i].title)
                }
            }
            FLOOR -> {
                val floors = building[chooseBuilding].lists
                for (i in 0 until floors.size) {
                    data.clear()
                    data.add(floors[i].title)
                }
            }
            ROOM -> {
                val rooms = building[chooseBuilding].lists[chooseFloor].lists
                for (i in 0 until rooms.size) {
                    data.clear()
                    data.add(rooms[i].title)
                }
            }
        }
        adapter.notifyDataSetChanged()
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
                    building.clear()
                    building.addAll(buildingListRes.retRes)
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    inner class ListItemAdapter(val data: ArrayList<String>) :
        MyBaseAdapter(R.layout.layout_choose_building_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.itemView.text.text = data[position]
        }

        override fun getItemCount(): Int = data.size
    }

}