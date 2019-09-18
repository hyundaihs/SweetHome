package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.adapters.MyHouseAdapter
import com.cyf.sweethome.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_house.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import java.util.ArrayList

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class MyHouseActivity : MyBaseActivity() {

    private val myHouseList = ArrayList<HouseListItem>()
    private val myHouseAdapter = MyHouseAdapter(myHouseList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_my_house)
        setTitle("我的房屋")
        addRightStringBtn("添加房屋", View.OnClickListener {

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
                10,
                resources.getColor(android.R.color.transparent)
            )
        )
        myHouse.itemAnimator = DefaultItemAnimator()
        myHouse.isNestedScrollingEnabled = false
        myHouse.setEmptyView(listEmptyView)
        myHouse.adapter = myHouseAdapter
        myHouseAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {

            }
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