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
import com.android.shuizu.myutillibrary.utils.getMessageDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome
import com.cyf.sweethome.activities.*
import com.cyf.sweethome.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.toast

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
        message.setOnClickListener {
            it.context.toast("建设中...")
        }
        open_door.setOnClickListener {
            it.context.toast("建设中...")
        }
        self_pay.setOnClickListener {
            it.context.toast("建设中...")
        }
        house_sale.setOnClickListener {
            it.context.toast("建设中...")
        }
        volun_apply.setOnClickListener {
            it.context.toast("建设中...")
        }
        submit_info.setOnClickListener {
            getMemberStatus()
        }
        get_help.setOnClickListener {
            it.context.toast("建设中...")
        }
        praise.setOnClickListener {
            it.context.toast("建设中...")
        }
        feedback.setOnClickListener {
            it.context.toast("建设中...")
        }
        cloudTalk.setOnClickListener {
            it.context.toast("建设中...")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 101){
            houseAddress.text = SweetHome.houseInfo?.fw_title
        }
    }

    private fun getMemberStatus(){
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(DJSQZT.getInterface())
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
                    val houseInfoRes = Gson().fromJson(result, MemberStatusRes::class.java)
                    when(houseInfoRes.retRes.sh_status){
                        0->{//未申请
                            startActivity(Intent(context,MemberApplyActivity::class.java))
                        }
                        1->{//审核中
                            getSuccessDialog(context,"您的党员认证申请还在审核中，请耐心等待！")
                        }
                        2->{//已通过
                            startActivity(Intent(context,MemberInfoNewsActivity::class.java))
                        }
                        3->{//已拒绝
                            getMessageDialog(context,"您的党员认证已被拒绝，需要重新申请吗？",object : DialogUIListener() {
                                override fun onPositive() {
                                    startActivity(Intent(context,MemberApplyActivity::class.java))
                                }

                                override fun onNegative() {

                                }
                            })
                        }
                    }
                }
            })
            setDialog()
            postRequest()
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