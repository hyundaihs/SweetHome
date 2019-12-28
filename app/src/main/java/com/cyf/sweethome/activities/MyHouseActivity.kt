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
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome
import com.cyf.sweethome.adapters.MyHouseAdapter
import com.cyf.sweethome.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_empty_recycleview.*
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
        if(resultCode == 155){
            toast(data?.getStringExtra("choose_building")?:"")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_my_house)
        setTitle("我的房屋")
        addRightStringBtn("添加房屋", View.OnClickListener {
            toast("本小区暂无此功能")
//            startActivityForResult(
//                Intent(
//                    this@MyHouseActivity,
//                    ChooseCommunityActivity::class.java
//                ), 155
//            )
        })
        initViews()
        getMyHouse()
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        listView.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        listView.addItemDecoration(
            RecyclerViewDivider(
                this,
                LinearLayoutManager.VERTICAL,
                10,
                resources.getColor(android.R.color.transparent)
            )
        )
        listView.itemAnimator = DefaultItemAnimator()
        listView.isNestedScrollingEnabled = false
        listView.setEmptyView(emptyView)
        listView.adapter = myHouseAdapter
        myHouseAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                if (myHouseList[position].xqfh_id != SweetHome.houseInfo?.id) {
                    setMyHouse(myHouseList[position].xqfh_id)
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
                }
            })
            setDataMap(map)
            setDialog()
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