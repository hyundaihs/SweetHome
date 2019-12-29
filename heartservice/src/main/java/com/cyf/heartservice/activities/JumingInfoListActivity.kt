package com.cyf.heartservice.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_juming_info.*
import kotlinx.android.synthetic.main.layout_juming_info_list_item.view.*
import org.jetbrains.anko.toast

class JumingInfoListActivity : MyBaseActivity(), TextWatcher {

    val data = ArrayList<JumingInfo>()
    val adapter = MyAdapter(data)

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        if (searchText.text.isNotBlank()) {
            getData(searchText.text.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_juming_info)
        setTitle("居民信息全览")
        init()
    }

    private fun init() {
        search.setOnClickListener { getData(searchText.text.toString()) }
        searchText.addTextChangedListener(this)
        delete.setOnClickListener { searchText.setText("") }
        val layoutManager = LinearLayoutManager(this)
        infos.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        infos.addItemDecoration(
            RecyclerViewDivider(
                this,
                LinearLayoutManager.HORIZONTAL,
                DisplayUtils.dp2px(this, 1f),
                resources.getColor(R.color.color_727C8E)
            )
        )
        infos.isNestedScrollingEnabled = false
        infos.adapter = adapter
        adapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                val intent = Intent(this@JumingInfoListActivity, JumingDetailsActivity::class.java)
                intent.putExtra("id", data[position].xqyz_id)
                intent.putExtra("name", data[position].title)
                intent.putExtra("phone", data[position].phone)
                startActivity(intent)
            }
        }
    }

    private fun getData(text: String) {
        val map = mapOf(
            Pair("title", text)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(JMXXSS.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    toast(error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val jumingInfoListRes = Gson().fromJson(result, JumingInfoListRes::class.java)
                    data.clear()
                    data.addAll(jumingInfoListRes.retRes)
                    adapter.notifyDataSetChanged()
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    inner class MyAdapter(val data: ArrayList<JumingInfo>) :
        MyBaseAdapter(R.layout.layout_juming_info_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.itemView.name.text = "${data[position].title} ${data[position].phone}"
            holder.itemView.address.text = data[position].fw_title
        }

        override fun getItemCount(): Int = data.size
    }
}