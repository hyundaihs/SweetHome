package com.cyf.heartservice.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.shuizu.myutillibrary.adapter.MyBaseAdapter
import com.android.shuizu.myutillibrary.adapter.RecyclerViewDivider
import com.android.shuizu.myutillibrary.fragment.MyBaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.DisplayUtils
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_house_payment.*
import kotlinx.android.synthetic.main.layout_payment_list_item.view.*
import java.util.ArrayList

class HousePaymentFragment(val id: String) : MyBaseFragment() {

    private val data = ArrayList<Payment>()
    private val myAdapter = PaymentAdapter(data)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_house_payment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        getInfo()
    }

    private fun init() {
        val layoutManager = LinearLayoutManager(context)
        paymentList.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        paymentList.addItemDecoration(
            RecyclerViewDivider(
                context,
                LinearLayoutManager.VERTICAL,
                DisplayUtils.dp2px(context, 10f),
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
        val map = mapOf(
            Pair("xqfh_id", id)
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(DJWYF.getInterface(map))
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val actInfoRes = Gson().fromJson(result, SumPaymentRes::class.java)
                    fillViews(actInfoRes.retRes)
                }
            })
            setDataMap(map)
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