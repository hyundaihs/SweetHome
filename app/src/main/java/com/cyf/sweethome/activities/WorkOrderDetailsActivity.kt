package com.cyf.sweethome.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.activities.PhotoViewActivity
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_work_order_details.*
import kotlinx.android.synthetic.main.layout_upload_image_list_item.view.*
import kotlinx.android.synthetic.main.layout_work_order_operaing_record_item.view.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/16/016.
 */
class WorkOrderDetailsActivity : MyBaseActivity() {

    private val imageData = ArrayList<ImageInfo>()
    private val imageAdapter = ImageAdapter(imageData)
    private val previewData = ArrayList<String>()

    private val operatingRecordList = java.util.ArrayList<OperatingRecord>()
    private val operatingRecordAdapter = OperatingRecordAdapter(operatingRecordList)
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_work_order_details)
        setTitle("报事详情")
        initViews()
        id = intent.getStringExtra("id")
        id?.let { getData(it) }
    }

    private fun initViews() {
        val gridLayoutManager1 = GridLayoutManager(this, 5)
        workOrderDetailImages.layoutManager = gridLayoutManager1
        workOrderDetailImages.addItemDecoration(GridDivider(this, dp2px(10f).toInt(), 5))
        workOrderDetailImages.itemAnimator = DefaultItemAnimator()
        workOrderDetailImages.adapter = imageAdapter
        workOrderDetailImages.isNestedScrollingEnabled = false
        imageAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                PhotoViewActivity.setData(previewData, true, position)
                startActivity(Intent(view.context, PhotoViewActivity::class.java))
            }
        }

        val layoutManager = LinearLayoutManager(this)
        workOrderDetailFlow.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        workOrderDetailFlow.addItemDecoration(
            RecyclerViewDivider(
                this,
                LinearLayoutManager.VERTICAL,
                DisplayUtils.dp2px(this, 1f),
                resources.getColor(R.color.color_EAEAEA)
            )
        )
        workOrderDetailFlow.itemAnimator = DefaultItemAnimator()
        workOrderDetailFlow.isNestedScrollingEnabled = false
        workOrderDetailFlow.adapter = operatingRecordAdapter
        operatingRecordAdapter.myOnItemClickListener =
            object : MyBaseAdapter.MyOnItemClickListener {
                override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                    val operatingRecord = operatingRecordList[position]
                    if(operatingRecord.sh_status > 2){
                        //查看详情
                        val intent = Intent(view.context,OrderHandleActivity::class.java)
                        val bundle = Bundle()
                        bundle.putSerializable("record",operatingRecord)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                }
            }
    }

    private fun getData(id: String) {
        val map = mapOf(
            Pair("id", id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(BSBXINFO.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val workOrderDetailsRes =
                        Gson().fromJson(result, WorkOrderDetailsRes::class.java)
                    fillViews(workOrderDetailsRes.retRes)
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun fillViews(details: WorkOrderDetails) {
        if (details.sh_status == 3) {
            ping_jia.visibility = View.VISIBLE
        } else {
            ping_jia.visibility = View.GONE
        }
        workOrderType.text = details.xqbsbxlx_title
        workOrderStatus.text = details.sh_title
        workOrderRemark.text = details.contents
        workOrderTime.text = CalendarUtil(details.create_time,true).format(CalendarUtil.YYYY_MM_DD_HH_MM)
        address.text = details.fw_title
        time.text = details.yy_time
        name.text = details.title
        phone.text = details.phone
        imageData.clear()
        imageData.addAll(details.img_lists)
        for (i in 0 until imageData.size) {
            previewData.add(imageData[i].file_url.getImageUrl())
        }
        imageAdapter.notifyDataSetChanged()
        operatingRecordList.clear()
        operatingRecordList.addAll(details.log_lists)
        operatingRecordAdapter.notifyDataSetChanged()
        ping_jia.setOnClickListener {
            val intent = Intent(it.context, PingJiaActivity::class.java)
            intent.putExtra("id", details.id)
            startActivityForResult(intent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 101) {
            id?.let { getData(it) }
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

    inner class OperatingRecordAdapter(val data: ArrayList<OperatingRecord>) :
        MyBaseAdapter(R.layout.layout_work_order_operaing_record_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val operatingRecord = data[position]
            if (operatingRecord.sh_status > 2) {
                holder.itemView.moreItem.visibility = View.VISIBLE
            } else {
                holder.itemView.moreItem.visibility = View.GONE
            }
            holder.itemView.status.text = operatingRecord.type_title
            Picasso.with(holder.itemView.context).load(operatingRecord.xqyg_file_url.getImageUrl())
                .resize(300, 300)
                .into(holder.itemView.photo)
            holder.itemView.contents.text = operatingRecord.title
            holder.itemView.time.text =
                CalendarUtil(operatingRecord.create_time,true).format(CalendarUtil.YYYY_MM_DD_HH_MM)
            holder.itemView.lineDot.isChecked = (position == 0)
            holder.itemView.lineTop.visibility = if (position == 0) View.GONE else View.VISIBLE
            holder.itemView.lineBottom.visibility =
                if (position == itemCount - 1) View.GONE else View.VISIBLE
            holder.itemView.line.visibility =
                if (position == itemCount - 1) View.INVISIBLE else View.VISIBLE
        }

        override fun getItemCount(): Int = data.size
    }

}