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
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.layout_payment_list_item.view.*
import java.util.ArrayList

class PaymentActivity : MyBaseActivity() {

    private val data = ArrayList<Payment>()
    private val myAdapter = PaymentAdapter(data)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_payment)
        setTitle("物业缴费")
        init()
        getInfo()
    }

    private fun init() {
        val layoutManager = LinearLayoutManager(this)
        paymentList.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        paymentList.addItemDecoration(
            RecyclerViewDivider(
                this,
                LinearLayoutManager.VERTICAL,
                DisplayUtils.dp2px(this, 10f),
                resources.getColor(android.R.color.transparent)
            )
        )
        paymentList.itemAnimator = DefaultItemAnimator()
        paymentList.isNestedScrollingEnabled = false
        paymentList.adapter = myAdapter
    }

    private fun fillViews(sumPayment: SumPayment) {
        sumPrice.text = sumPayment.all_price
        data.clear()
        data.addAll(sumPayment.lists)
        myAdapter.notifyDataSetChanged()
    }

    private fun getInfo() {
        KevinRequest.build(this).apply {
            setRequestUrl(DJWYF.getInterface())
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
                    val actInfoRes = Gson().fromJson(result, SumPaymentRes::class.java)
                    fillViews(actInfoRes.retRes)
                }
            })
            setDialog()
            postRequest()
        }
    }

    inner class PaymentAdapter(val data: ArrayList<Payment>) :
        MyBaseAdapter(R.layout.layout_payment_list_item) {

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val payment = data[position]
            holder.itemView.date.text = payment.dates
            holder.itemView.price.text = payment.price
        }

        override fun getItemCount(): Int = data.size
    }
}