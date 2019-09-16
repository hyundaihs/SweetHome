package com.cyf.sweethome.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.CalendarUtil
import com.android.shuizu.myutillibrary.utils.FileUtil
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.*
import com.cyf.sweethome.utils.PickerUtil
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.PicassoEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_submit_repair.*
import kotlinx.android.synthetic.main.layout_repair_contact_list_item.view.*
import kotlinx.android.synthetic.main.layout_repair_type_item.view.*
import kotlinx.android.synthetic.main.layout_upload_image_list_item.view.*
import org.jetbrains.anko.toast
import java.io.File

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/16.
 */
class SubmitRepairActivity : MyBaseActivity() {

    val REQUEST_CODE_CHOOSE = 10
    val MAX_IMAGE = 5

    private val typeList = ArrayList<RepairType>()
    private val typeAdapter = TypeAdapter(typeList)
    private val contactList = ArrayList<ContactInfo>()
    private val contactAdapter = ContactAdapter(contactList)
    private val imageData = ArrayList<String>()
    private val imageAdapter = ImageAdapter(imageData)
    private val submitUrl = ArrayList<String>()

    private var checkType = 1
    private var chooseTime = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val selectList = Matisse.obtainResult(data)
            for (i in 0 until selectList.size) {
                var file = FileUtil.getPathFromUri(this, selectList[i])
                if (file != null) {
                    uploadPhoto(file)
                } else {
                    file = "/storage/emulated/0/${Environment.DIRECTORY_PICTURES}/${selectList[i].lastPathSegment}"
                    uploadPhoto(file)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_submit_repair)
        setTitle("报事报修")
        setBackup(false)
        addRightStringBtn("取消", View.OnClickListener {
            finish()
        })
        initViews()
        getTypes()
        getContacts()
    }

    private fun initViews() {
        initTypeRecyclerView()
        initImageRecyclerView()
        initContactRecyclerView()
        val time =
            CalendarUtil(System.currentTimeMillis() + 10 * 60 * 1000).format(CalendarUtil.HH_mm)
        immediatelyTime.text = "（最快将在${time}为您服务）"
        submit.setOnClickListener {
            submit()
        }
        immediately.setOnClickListener {
            immediately.isChecked = true
            reservation.isChecked = false
            checkType = 1
            chooseTime = ""
            reservation_time.visibility = View.GONE
        }
        reservation.setOnClickListener {
            reservation.isChecked = true
            immediately.isChecked = false
            checkType = 2
            reservation_time.visibility = View.VISIBLE
            showPickTimer()
        }
        reservation_time.setOnClickListener {
            showPickTimer()
        }
    }

    private fun showPickTimer() {
        PickerUtil.showTimerPicker(this) { date, v ->
            if (date != null) {
                chooseTime = CalendarUtil(date.time).format(CalendarUtil.YYYY_MM_DD_HH_MM)
                reservation_time.text = "（${chooseTime}）"
            }
        }
    }

    private fun submit() {
        if(repairRemark.text.isEmpty()){
            repairRemark.error = "请填写您遇到的问题"
            return
        }
        val map = mapOf(
            Pair("contents", repairRemark.text.toString()),
            Pair("title", contactList[contactAdapter.getChecked()].title),
            Pair("phone", contactList[contactAdapter.getChecked()].phone),
            Pair("xqbsbxlx_id", typeList[typeAdapter.getChecked()].id),
            Pair("yylx", checkType),
            Pair("yy_time", chooseTime),
            Pair("img_lists", submitUrl)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(TJBSBX.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    getSuccessDialog(context, "操作成功", object : DialogUIListener() {
                        override fun onPositive() {
                            finish()
                        }

                        override fun onNegative() {

                        }
                    })
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun initTypeRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, 3)
        repairType.layoutManager = gridLayoutManager
        repairType.addItemDecoration(GridDivider(this, dp2px(10f).toInt(), 3))
        repairType.itemAnimator = DefaultItemAnimator()
        repairType.isNestedScrollingEnabled = false
        repairType.adapter = typeAdapter
        typeAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                typeAdapter.setChecked(position)
            }
        }
    }

    private fun initImageRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, 5)
        repairImages.layoutManager = gridLayoutManager
        repairImages.addItemDecoration(GridDivider(this, dp2px(10f).toInt(), 5))
        repairImages.itemAnimator = DefaultItemAnimator()
        repairImages.isNestedScrollingEnabled = false
        repairImages.adapter = imageAdapter
        imageAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                if (position == imageData.size && position < MAX_IMAGE) {
                    choosePic(MAX_IMAGE - imageData.size)
                } else {
                    //ShowImageDialog(File(images[position]))
                }
            }
        }
    }

    private fun initContactRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, 2)
        repairContact.layoutManager = gridLayoutManager
        repairContact.addItemDecoration(GridDivider(this, dp2px(10f).toInt(), 2))
        repairContact.itemAnimator = DefaultItemAnimator()
        repairContact.isNestedScrollingEnabled = false
        repairContact.adapter = contactAdapter
        contactAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                contactAdapter.setChecked(position)
            }
        }
    }

    fun choosePic(max: Int) {
        Matisse.from(this)
            .choose(MimeType.allOf())
            .countable(true)
            .maxSelectable(max)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .capture(true)
            .captureStrategy(CaptureStrategy(true, "PhotoPicker"))
            .imageEngine(PicassoEngine())
            .forResult(REQUEST_CODE_CHOOSE)
    }

    private fun getTypes() {
        KevinRequest.build(this).apply {
            setRequestUrl(BSBXFLLISTS.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val repairTypeListRes = Gson().fromJson(result, RepairTypeListRes::class.java)
                    typeList.clear()
                    typeList.addAll(repairTypeListRes.retRes)
                    typeAdapter.notifyDataSetChanged()
                }

            })
            setDialog()
            postRequest()
        }
    }

    private fun getContacts() {
        KevinRequest.build(this).apply {
            setRequestUrl(WDLXR.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val contactInfoListRes = Gson().fromJson(result, ContactInfoListRes::class.java)
                    contactList.clear()
                    contactList.addAll(contactInfoListRes.retRes)
                    contactAdapter.notifyDataSetChanged()
                }

            })
            setDialog()
            postRequest()
        }
    }

    private fun uploadPhoto(file: String) {
        val list = ArrayList<String>()
        list.add(file)
        KevinRequest.build(this).apply {
            setRequestUrl(UPLOADFILE.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    toast(error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val fileInfoRes = Gson().fromJson(result, FileInfoRes::class.java)
                    submitUrl.add(fileInfoRes.retRes.file_url)
                    imageData.add(file)
                    imageAdapter.notifyDataSetChanged()
                }
            })
            openLoginErrCallback(LoginActivity::class.java)
            setDialog()
            uploadFile(list)
        }
    }


    inner class TypeAdapter(val data: ArrayList<RepairType>) :
        MyBaseAdapter(R.layout.layout_repair_type_item) {

        private var checked = 0

        fun setChecked(pos: Int) {
            checked = pos
            notifyDataSetChanged()
        }

        fun getChecked(): Int {
            return checked
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val type = data[position]
            holder.itemView.typeName.text = type.title
            holder.itemView.typeName.isChecked = (position == checked)
            holder.itemView.typeName.setOnClickListener {
                setChecked(position)
            }
        }

        override fun getItemCount(): Int = data.size
    }

    inner class ImageAdapter(val data: ArrayList<String>) :
        MyBaseAdapter(R.layout.layout_upload_image_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            if (position >= data.size && position < MAX_IMAGE) {
                holder.itemView.uploadImage.setImageResource(R.mipmap.add_pic)
                holder.itemView.uploadDelete.visibility = View.GONE
            } else {
                Picasso.with(holder.itemView.context).load(File(data[position])).resize(300, 300)
                    .into(holder.itemView.uploadImage)
                holder.itemView.uploadDelete.visibility = View.VISIBLE
            }
            holder.itemView.uploadDelete.setOnClickListener {
                data.removeAt(position)
                notifyDataSetChanged()
            }
        }

        override fun getItemCount(): Int = if (data.size < MAX_IMAGE) data.size + 1 else data.size
    }

    inner class ContactAdapter(val data: ArrayList<ContactInfo>) :
        MyBaseAdapter(R.layout.layout_repair_contact_list_item) {

        private var checked = 0

        fun setChecked(pos: Int) {
            checked = pos
            notifyDataSetChanged()
        }

        fun getChecked(): Int {
            return checked
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val contact = data[position]
            holder.itemView.contactName.text = contact.title
            holder.itemView.contactPhone.text = contact.phone
            holder.itemView.contactCheck.isChecked = (position == checked)
            holder.itemView.contactCheck.setOnClickListener {
                setChecked(position)
            }
        }

        override fun getItemCount(): Int = data.size
    }
}