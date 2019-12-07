package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.adapter.GridDivider
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.dp2px
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_volun_apply.*
import kotlinx.android.synthetic.main.layout_repair_type_item.view.*
import org.jetbrains.anko.toast

class VolunApplyActivity : MyBaseActivity() {

    private val typeList = ArrayList<RepairType>()
    private val typeAdapter = TypeAdapter(typeList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_volun_apply)
        setTitle("志愿者认证申请")
        init()
    }

    private fun init() {
        submit.setOnClickListener {
            submit()
        }
        val gridLayoutManager = GridLayoutManager(this, 3)
        volunType.layoutManager = gridLayoutManager
        volunType.addItemDecoration(GridDivider(this, dp2px(10f).toInt(), 3))
        volunType.itemAnimator = DefaultItemAnimator()
        volunType.isNestedScrollingEnabled = false
        volunType.adapter = typeAdapter
        typeAdapter.myOnItemClickListener = object : MyBaseAdapter.MyOnItemClickListener {
            override fun onItemClick(parent: MyBaseAdapter, view: View, position: Int) {
                typeAdapter.setChecked(position)
            }
        }
        getTypes()
    }

    private fun getTypes() {
        KevinRequest.build(this).apply {
            setRequestUrl(HDSTYPE.getInterface())
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

    private fun checkEdits(): Boolean {
        return when {
            name.text.isEmpty() -> {
                name.error = "请填写内容"
                false
            }
            idCard.text.isEmpty() -> {
                idCard.error = "请填写内容"
                false
            }
            info.text.isEmpty() -> {
                info.error = "请填写内容"
                false
            }
            typeAdapter.getChecked().size <= 0 -> {
                toast("请选择分类")
                false
            }
            else -> true
        }
    }

    private fun submit() {
        if (!checkEdits()) {
            return
        }
        val ids = ArrayList<String>()
        for (pos in typeAdapter.getChecked()) {
            ids.add(typeList[pos].id)
        }
        val map = mapOf(
            Pair("title", name.text.toString()),
            Pair("card_num", idCard.text.toString()),
            Pair("contents", info.text.toString()),
            Pair("stype_ids", ids)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(HDSQ.getInterface(map))
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

    inner class TypeAdapter(val data: ArrayList<RepairType>) :
        MyBaseAdapter(R.layout.layout_repair_type_item) {

        private var checked = ArrayList<Int>()

        fun setChecked(pos: Int) {
            if (checked.contains(pos)) {
                checked.remove(pos)
            } else {
                checked.add(pos)
            }
            notifyDataSetChanged()
        }

        fun getChecked(): ArrayList<Int> {
            return checked
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val type = data[position]
            holder.itemView.typeName.text = type.title
            holder.itemView.typeName.isChecked = (checked.contains(position))
            holder.itemView.typeName.setOnClickListener {
                setChecked(position)
            }
        }

        override fun getItemCount(): Int = data.size
    }
}