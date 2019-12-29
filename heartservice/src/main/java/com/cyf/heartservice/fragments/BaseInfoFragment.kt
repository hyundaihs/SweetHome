package com.cyf.heartservice.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shuizu.myutillibrary.fragment.MyBaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_base_info.*

class BaseInfoFragment(val id: String) : MyBaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getData()
    }

    private fun getData() {
        val map = mapOf(
            Pair("xqyz_id", id)
        )
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(JMXXXQ.getInterface(map))
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val jumingDetailsRes = Gson().fromJson(result, JumingDetailsRes::class.java)
                    fillViews(jumingDetailsRes.retRes)
                }
            })
            setDataMap(map)
            postRequest()
        }
    }

    private fun fillViews(jumingDetails: JumingDetails) {
        name.text = jumingDetails.title
        sex.text = jumingDetails.sex
        phone.text = jumingDetails.phone
        cardNum.text = jumingDetails.card_num
        shenfen.text = jumingDetails.xqyzsf_title
    }
}