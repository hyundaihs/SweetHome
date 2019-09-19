package com.cyf.sweethome.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome
import com.cyf.sweethome.activities.CheckRoomLogActivity
import com.cyf.sweethome.activities.MyHouseActivity
import com.cyf.sweethome.activities.SubmitRepairActivity
import com.cyf.sweethome.entity.DQFWXX
import com.cyf.sweethome.entity.HouseInfoRes
import com.cyf.sweethome.entity.getInterface
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class HomeFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        getHouseInfo()
    }

    private fun initViews() {
        houseAddress.setOnClickListener {
            startActivityForResult(Intent(activity, MyHouseActivity::class.java), 100)
        }
        ownerCheckRoom.setOnClickListener {
            startActivity(Intent(activity, CheckRoomLogActivity::class.java))
        }
        callRepair.setOnClickListener {
            startActivity(Intent(activity, SubmitRepairActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 101){
            houseAddress.text = SweetHome.houseInfo?.fw_title
        }
    }

    private fun getHouseInfo() {
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(DQFWXX.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {
                            activity?.finish()
                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val houseInfoRes = Gson().fromJson(result, HouseInfoRes::class.java)
                    SweetHome.houseInfo = houseInfoRes.retRes
                    houseAddress.text = SweetHome.houseInfo?.fw_title
                }
            })
            setDialog()
            postRequest()
        }
    }
}