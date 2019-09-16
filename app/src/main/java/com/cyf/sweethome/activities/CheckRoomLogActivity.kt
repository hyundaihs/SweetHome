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
import com.cyf.sweethome.adapters.CheckRoomLogsAdapter
import com.cyf.sweethome.entity.CheckRoomLog
import com.cyf.sweethome.entity.CheckRoomLogListRes
import com.cyf.sweethome.entity.YFJLLISTS
import com.cyf.sweethome.entity.getInterface
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_checkroom_log.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import java.util.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class CheckRoomLogActivity : MyBaseActivity() {

    private val data = ArrayList<CheckRoomLog>()
    private val myAdapter = CheckRoomLogsAdapter(data)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_checkroom_log)
        setTitle("验房记录")
        addRightStringBtn("提交验房", View.OnClickListener {
            startActivityForResult(Intent(this, CheckRoomActivity::class.java), 50)
        })
        initViews()
        getData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 50) and (resultCode == 51)) {
            getData()
        }
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        checkRoomLogs.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        checkRoomLogs.addItemDecoration(
            RecyclerViewDivider(
                this,
                LinearLayoutManager.VERTICAL,
                DisplayUtils.dp2px(this, 15f),
                resources.getColor(android.R.color.transparent)
            )
        )
        //checkRoomLogs.addItemDecoration(LineDecoration(this, LineDecoration.VERTICAL))
        checkRoomLogs.itemAnimator = DefaultItemAnimator()
        checkRoomLogs.isNestedScrollingEnabled = false
        checkRoomLogs.setEmptyView(listEmptyView)
        checkRoomLogs.adapter = myAdapter
        myAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                val intent = Intent(this@CheckRoomLogActivity, CheckRoomDetailsActivity::class.java)
                intent.putExtra("id", data[position].id)
                startActivity(intent)
            }
        }
    }

    private fun getData() {
        val map = mapOf(
            Pair("sh_status", 0)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(YFJLLISTS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val checkRoomLogListRes =
                        Gson().fromJson(result, CheckRoomLogListRes::class.java)
                    data.clear()
                    data.addAll(checkRoomLogListRes.retRes)
                    myAdapter.notifyDataSetChanged()
                }

            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }
}