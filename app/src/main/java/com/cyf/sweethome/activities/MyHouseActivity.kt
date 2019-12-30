package com.cyf.sweethome.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.cyf.sweethome.SweetHome
import com.cyf.sweethome.SweetHome.Companion.ADD_HOUSE_RESULT
import com.cyf.sweethome.adapters.MyHouseAdapter
import com.cyf.sweethome.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_house.*
import org.jetbrains.anko.toast
import java.util.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class MyHouseActivity : MyBaseActivity() {

    private val myHouseList = ArrayList<HouseListItem>()
    private val myHouseAdapter = MyHouseAdapter(myHouseList)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ADD_HOUSE_RESULT) {
            getMyHouse()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_my_house)
        setTitle("我的房屋")
        addRightStringBtn("添加房屋", View.OnClickListener {
            startActivityForResult(
                Intent(
                    this@MyHouseActivity,
                    AddHouseActivity::class.java
                ), 155
            )
        })
        initViews()
        getMyHouse()
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        myHouse.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        myHouse.addItemDecoration(
            RecyclerViewDivider(
                this,
                LinearLayoutManager.VERTICAL,
                DisplayUtils.dp2px(this, 15f),
                resources.getColor(android.R.color.transparent)
            )
        )
        myHouse.itemAnimator = DefaultItemAnimator()
        myHouse.isNestedScrollingEnabled = false
        myHouse.setEmptyView(emptyView)
        myHouse.adapter = myHouseAdapter
        myHouseAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                if (myHouseList[position].sh_status != 2) {
                    getErrorDialog(view.context, "暂未通过审核!")
                    return
                }
                if (myHouseList[position].xqfh_id != SweetHome.houseInfo?.id) {
                    setMyHouse(myHouseList[position].xqfh_id)
                } else {
                    finish()
                }
            }
        }
    }

    private fun setMyHouse(id: String) {
        val map = mapOf(
            Pair("xqfh_id", id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(SETDQFW.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val houseInfoRes = Gson().fromJson(result, HouseInfoRes::class.java)
                    SweetHome.houseInfo = houseInfoRes.retRes
                    finish()
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun getMyHouse() {
        val map = mapOf(
            Pair("wdfwlb", 0)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(WDFWLB.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val houseListRes = Gson().fromJson(result, HouseListRes::class.java)
                    myHouseList.clear()
                    myHouseList.addAll(houseListRes.retRes)
                    myHouseAdapter.notifyDataSetChanged()
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }
}