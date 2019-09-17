package com.cyf.sweethome.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.activities.PhotoViewActivity
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_checkroom_details.*
import kotlinx.android.synthetic.main.layout_upload_image_list_item.view.*

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/16.
 */
class CheckRoomDetailsActivity : MyBaseActivity() {

    private val imageData = ArrayList<ImageInfo>()
    private val imageAdapter = ImageAdapter(imageData)
    private val previewData = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_checkroom_details)
        setTitle("验房详情")
        initViews()
        val id = intent.getStringExtra("id")
        id?.let { getData(it) }
    }

    private fun getData(id: String) {
        val map = mapOf(
            Pair("id", id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(YFJLINFO.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val checkRoomDetailsRes =
                        Gson().fromJson(result, CheckRoomDetailsRes::class.java)
                    fillViews(checkRoomDetailsRes.retRes)
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun initViews() {
        val gridLayoutManager1 = GridLayoutManager(this, 5)
        detailsRecyclerView.layoutManager = gridLayoutManager1
        detailsRecyclerView.addItemDecoration(GridDivider(this, dp2px(10f).toInt(), 5))
        detailsRecyclerView.itemAnimator = DefaultItemAnimator()
        detailsRecyclerView.adapter = imageAdapter
        detailsRecyclerView.isNestedScrollingEnabled = false
        imageAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                PhotoViewActivity.setData(previewData,true,position)
                startActivity(Intent(view.context, PhotoViewActivity::class.java))
            }
        }
    }

    private fun fillViews(details: CheckRoomDetails) {
        roomAddress.text = details.fw_title
        roomStatus.text = CHECK_ROOM_STATUS[details.sh_status]
        roomStatus.setTextColor(resources.getColor(CHECK_ROOM_STATUS_COLOR[details.sh_status]))
        roomRemark.text = details.contents
        roomTime.text =
            CalendarUtil(details.create_time, true).format(CalendarUtil.YYYY_MM_DD_HH_MM)
        name.text = details.title
        phone.text = details.phone
        imageData.clear()
        imageData.addAll(details.img_lists)
        for(i in 0 until imageData.size){
            previewData.add(imageData[i].file_url.getImageUrl())
        }
        imageAdapter.notifyDataSetChanged()
    }

    inner class ImageAdapter(val data: ArrayList<ImageInfo>) :
        MyBaseAdapter(R.layout.layout_upload_image_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val imageInfo = data[position]
            holder.itemView.uploadDelete.visibility = View.GONE
            Picasso.with(holder.itemView.context).load(imageInfo.resize_file_url.getImageUrl())
                .resize(300, 300)
                .into(holder.itemView.uploadImage)
        }

        override fun getItemCount(): Int = data.size
    }
}