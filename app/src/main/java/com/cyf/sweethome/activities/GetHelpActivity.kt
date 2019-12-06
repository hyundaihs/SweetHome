package com.cyf.sweethome.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.activities.PhotoViewActivity
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.hideInput
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.*
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.*
import com.cyf.sweethome.utils.PickerUtil
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_get_help.*
import kotlinx.android.synthetic.main.layout_upload_image_list_item.view.*
import org.jetbrains.anko.toast
import java.io.File

class GetHelpActivity : MyBaseActivity() {

    private val imageData = ArrayList<String>()
    private val imageAdapter = ImageAdapter(imageData)
    private val submitUrl = ArrayList<String>()
    val REQUEST_CODE_CHOOSE = 10
    val MAX_IMAGE = 5

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectList = PictureSelectorObtainMultipleResult(data)
            when (requestCode) {
                REQUEST_CODE_CHOOSE -> {

                    for (i in 0 until selectList.size) {
                        val file = selectList[i]
                        uploadPhoto(file)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_get_help)
        setTitle("帮帮忙")
        init()
    }

    private fun init() {
        initTypeRecyclerView()
        time.setOnClickListener {
            showPickTimer()
        }
        submit.setOnClickListener {
            submit()
        }
    }

    private fun showPickTimer() {
        hideInput()
        PickerUtil.showTimerPicker(this) { date, v ->
            if (date != null) {
                val chooseTime = CalendarUtil(date.time).format(CalendarUtil.YYYY_MM_DD_HH_MM)
                time.text = "${chooseTime}"
            }
        }
    }

    private fun initTypeRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, 5)
        images.layoutManager = gridLayoutManager
        images.addItemDecoration(GridDivider(this, dp2px(10f).toInt(), 5))
        images.itemAnimator = DefaultItemAnimator()
        images.isNestedScrollingEnabled = false
        images.adapter = imageAdapter
        imageAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                if (position == imageData.size && position < MAX_IMAGE) {
                    PictureSelectorStart(MAX_IMAGE - imageData.size, REQUEST_CODE_CHOOSE)
                } else {
                    PhotoViewActivity.setData(imageData, false, position)
                    startActivity(Intent(view.context, PhotoViewActivity::class.java))
                }
            }
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

    private fun checkEdits(): Boolean {
        when {
            question.text.isEmpty() -> {
                question.error = "请填写您遇到的问题"
                return false
            }
            time.text.isEmpty() -> {
                time.error = "请填写您遇到的问题"
                return false
            }
            contactName.text.isEmpty() -> {
                contactName.error = "请填写您遇到的问题"
                return false
            }
            contactPhone.text.isEmpty() -> {
                contactPhone.error = "请填写您遇到的问题"
                return false
            }
            else -> return true
        }
    }

    private fun submit() {
        if(!checkEdits()){
            return
        }
        val map = mapOf(
            Pair("contents", question.text.toString()),
            Pair("title", contactName.text.toString()),
            Pair("phone", contactPhone.text.toString()),
            Pair("yy_time", time.text.toString()),
            Pair("img_lists", submitUrl)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(TJJSBBM.getInterface(map))
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

    private inner class ImageAdapter(val data: ArrayList<String>) :
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
}