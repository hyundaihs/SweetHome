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
import com.cyf.sweethome.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_choose_community.*
import kotlinx.android.synthetic.main.layout_choose_building_list_item.view.*

class ChooseCommunityActivity : MyBaseActivity() {

    val data = ArrayList<Room>()
    val adapter = ListItemAdapter(data)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 155){
            val intent = Intent()
            intent.putExtra("choose_building", data?.getStringExtra("choose_building"))
            setResult(155, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_choose_community)
        setTitle("选择社区")
        init()
    }

    private fun init() {
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
                val intent = Intent(this@ChooseCommunityActivity, ChooseRoomActivity::class.java)
                intent.putExtra("id", data[position].id)
                intent.putExtra("name", data[position].title)
                startActivityForResult(intent, 155)
            }
        }
        getCommunityData()
    }

    private fun getCommunityData() {
        KevinRequest.build(this).apply {
            setRequestUrl(XQLISTS.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val communityListRes = Gson().fromJson(result, CommunityListRes::class.java)
                    data.clear()
                    data.addAll(communityListRes.retRes)
                    adapter.notifyDataSetChanged()
                }
            })
            setDialog()
            postRequest()
        }
    }

    inner class ListItemAdapter(val data: ArrayList<Room>) :
        MyBaseAdapter(R.layout.layout_choose_building_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.itemView.text.text = data[position].title
        }

        override fun getItemCount(): Int = data.size
    }
}