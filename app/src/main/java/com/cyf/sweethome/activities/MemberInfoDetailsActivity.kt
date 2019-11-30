package com.cyf.sweethome.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.android.shuizu.myutillibrary.E
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.LineDecoration
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.*
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_member_info_details.*
import kotlinx.android.synthetic.main.layout_comment_list_item.view.*
import org.jetbrains.anko.toast

class MemberInfoDetailsActivity : MyBaseActivity() {

    private val data = ArrayList<CommentInfo>()
    private val adapter = CommentListAdapter(data)
    private var id = "0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_member_info_details)
        setTitle("详情")
        getDetails()
    }

    private fun getDetails() {
        id = intent.getStringExtra("id") ?: "0"
        val map = mapOf(
            Pair("id", id),
            Pair("page", 1),
            Pair("page_size", 1000)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(DJINFO.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {
                            finish()
                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val memberInfoRes = Gson().fromJson(result, MemberInfoRes::class.java)
                    init(memberInfoRes.retRes)
                    getComments()
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private fun getComments() {
        val map = mapOf(
            Pair("id", id)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(NRPLLISTS.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {
                            finish()
                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val commentInfoListRes = Gson().fromJson(result, CommentInfoListRes::class.java)
                    data.clear()
                    E("${data}")
                    data.addAll(commentInfoListRes.retRes)
                    adapter.notifyDataSetChanged()
                    comment_num.text = "评论（${data.size}）"
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun init(info: MemberInfo) {
        //Picasso.with(this).load(info.file_url.getImageUrl()).into(imgView)
        infoTitle.text = info.title
        time.text = CalendarUtil(info.create_time, true).format(
            CalendarUtil.STANDARD
        )
        subTitle.text = info.sub_title
        //content.initWebView()
        content.loadLocalHtml(info.app_contents)
        comment_num.text = "评论（${data.size}）"
        val layoutManager = LinearLayoutManager(this)
        comments.layoutManager = layoutManager
        layoutManager.orientation = VERTICAL
        comments.addItemDecoration(LineDecoration(this, LineDecoration.HORIZONTAL))
        comments.itemAnimator = DefaultItemAnimator()
        comments.isNestedScrollingEnabled = false
        comments.adapter = adapter
        adapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
//                val intent = Intent(view.context, MemberInfoDetailsActivity::class.java)
//                intent.putExtra("id", data[position].id)
//                startActivity(intent)
            }
        }
        send.setOnClickListener {
            sendComment()
        }
    }

    private fun sendComment() {
        if (comment_btn.text.trim().isEmpty()) {
            return
        }
        val map = mapOf(
            Pair("id", id),
            Pair("title", comment_btn.text.toString())
        )
        KevinRequest.build(this).apply {
            setRequestUrl(NRPLADD.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {

                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    getComments()
                    comment_btn.setText("")
                    toast("评论成功")
                }
            })
            setDataMap(map)
            setDialog()
            postRequest()
        }
    }

    private class CommentListAdapter(val data: ArrayList<CommentInfo>) :
        MyBaseAdapter(R.layout.layout_comment_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val commentInfo = data[position]
            Picasso.with(holder.itemView.context).load(commentInfo.account_file_url.getImageUrl())
                .resize(100, 100)
                .error(R.mipmap.ic_launcher)
                .into(holder.itemView.imgView)
            holder.itemView.name.text = commentInfo.account_title
            holder.itemView.content.text = commentInfo.title
            holder.itemView.time.text = CalendarUtil(commentInfo.create_time, true).format(
                CalendarUtil.STANDARD
            )
            holder.itemView.line.visibility =
                if (position == data.size - 1) View.GONE else View.VISIBLE
        }

        override fun getItemCount(): Int = data.size
    }
}