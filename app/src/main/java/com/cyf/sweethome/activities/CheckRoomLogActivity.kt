package com.cyf.sweethome.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.cyf.sweethome.R
import com.cyf.sweethome.adapters.CheckRoomLogsAdapter
import com.cyf.sweethome.entity.CheckRoomLog
import kotlinx.android.synthetic.main.activity_checkroom_log.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import java.util.ArrayList

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

        })
        initViews()
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        checkRoomLogs.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        checkRoomLogs.addItemDecoration(
            RecyclerViewDivider(
                this,
                LinearLayoutManager.VERTICAL,
                10,
                resources.getColor(android.R.color.transparent)
            )
        )
        checkRoomLogs.addItemDecoration(LineDecoration(this, LineDecoration.VERTICAL))
        checkRoomLogs.itemAnimator = DefaultItemAnimator()
        checkRoomLogs.isNestedScrollingEnabled = false
        checkRoomLogs.setEmptyView(listEmptyView)
        checkRoomLogs.adapter = myAdapter
        myAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {

            }
        }
    }
}