package com.cyf.sweethome.activities

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
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.ImageInfo
import com.cyf.sweethome.entity.OperatingRecord
import com.cyf.sweethome.entity.getImageUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_order_handle.*
import kotlinx.android.synthetic.main.layout_upload_image_list_item.view.*

class OrderHandleActivity : MyBaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_order_handle)
        setTitle("操作详情")
        val op = intent.extras?.getSerializable("record") as OperatingRecord
        op.let {
            status.text = op.type_title
            contents.text = op.contents
            val gridLayoutManager1 = GridLayoutManager(this, 5)
            images.layoutManager = gridLayoutManager1
            images.addItemDecoration(GridDivider(this, dp2px(10f).toInt(), 5))
            images.itemAnimator = DefaultItemAnimator()

            val imageAdapter = ImageAdapter(op.img_lists)
            val previewData = ArrayList<String>()
            for (i in 0 until op.img_lists.size) {
                previewData.add(op.img_lists[i].file_url.getImageUrl())
            }

            images.adapter = imageAdapter
            images.isNestedScrollingEnabled = false
            imageAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
                override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {

                    PhotoViewActivity.setData(previewData, true, position)
                    startActivity(Intent(view.context, PhotoViewActivity::class.java))
                }
            }
        }
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