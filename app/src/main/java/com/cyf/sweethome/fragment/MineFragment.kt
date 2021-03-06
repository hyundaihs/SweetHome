package com.cyf.sweethome.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shuizu.myutillibrary.fragment.MyBaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.PictureSelectorObtainMultipleResult
import com.android.shuizu.myutillibrary.utils.PictureSelectorStart
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome
import com.cyf.sweethome.activities.*
import com.cyf.sweethome.entity.GDSL
import com.cyf.sweethome.entity.WorkOrderNumListRes
import com.cyf.sweethome.entity.getImageUrl
import com.cyf.sweethome.entity.getInterface
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mine.*
import org.jetbrains.anko.toast

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class MineFragment : MyBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        getWorkOrderNum()
    }

    override fun onResume() {
        super.onResume()
        SweetHome.userInfo?.let {
            Picasso.with(context).load(it.file_url.getImageUrl())
                .resize(300, 300)
                .error(R.mipmap.ic_launcher)
                .into(photo)
            account.text = it.phone
            username.text = it.title
        }
    }

    private fun initViews() {

        val intent = Intent(context, WorkOrderListActivity::class.java)
        workOrder.setOnClickListener {
            intent.putExtra("page", 0)
            startActivity(intent)
        }
        orderReceiveing.setOnClickListener {
            intent.putExtra("page", 1)
            startActivity(intent)
        }
        processing.setOnClickListener {
            intent.putExtra("page", 2)
            startActivity(intent)
        }
        evaluate.setOnClickListener {
            intent.putExtra("page", 3)
            startActivity(intent)
        }
        edit_info.setOnClickListener {
            //it.context.toast("本小区暂无此功能")
        }
        myHouse.setOnClickListener {
            startActivityForResult(Intent(activity, MyHouseActivity::class.java), 100)
        }
        myActivity.setOnClickListener {
            startActivity(Intent(activity, EventsCenterActivity::class.java))
        }
        myHealth.setOnClickListener {
            startActivity(Intent(activity, HealthActivity::class.java))
        }
        tuiGuang.setOnClickListener {
            startActivity(Intent(context, MyShareCodeActivity::class.java))
        }
        setting.setOnClickListener {
            startActivityForResult(Intent(context, SettingActivity::class.java), 104)
        }
        about.setOnClickListener {
            startActivity(Intent(context, AboutUsActivity::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 105) {
            activity?.finish()
        }
    }

    private fun getWorkOrderNum() {
        activity?.let {
            KevinRequest.build(it).apply {
                setRequestUrl(GDSL.getInterface())
                setErrorCallback(object : KevinRequest.ErrorCallback {
                    override fun onError(context: Context, error: String) {
                        getErrorDialog(context, error)
                    }
                })
                setSuccessCallback(object : KevinRequest.SuccessCallback {
                    override fun onSuccess(context: Context, result: String) {
                        val workOrderNumListRes =
                            Gson().fromJson(result, WorkOrderNumListRes::class.java)
                        fillWorkOrder(workOrderNumListRes.retRes)
                    }

                })
                setDialog()
                postRequest()
            }
        }
    }

    private fun fillWorkOrder(map: Map<Int, String>) {
        orderReceiveingNum.text = map[1]
        processingNum.text = map[2]
        evaluateNum.text = map[3]
    }
}