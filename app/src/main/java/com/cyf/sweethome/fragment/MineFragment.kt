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
import com.cyf.sweethome.activities.WorkOrderListActivity
import com.cyf.sweethome.entity.GDSL
import com.cyf.sweethome.entity.WorkOrderNumListRes
import com.cyf.sweethome.entity.getInterface
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class MineFragment : BaseFragment() {
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

    private fun initViews() {
        val intent = Intent(context, WorkOrderListActivity::class.java)
        workOrder.setOnClickListener {
            intent.putExtra("page",0)
            startActivity(intent)
        }
        orderReceiveing.setOnClickListener {
            intent.putExtra("page",1)
            startActivity(intent)
        }
        processing.setOnClickListener {
            intent.putExtra("page",2)
            startActivity(intent)
        }
        evaluate.setOnClickListener {
            intent.putExtra("page",3)
            startActivity(intent)
        }
    }

    private fun getWorkOrderNum() {
        KevinRequest.build(activity as Context).apply {
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

    private fun fillWorkOrder(map: Map<Int, String>) {
        orderReceiveingNum.text = map[1]
        processingNum.text = map[2]
        evaluateNum.text = map[3]
    }
}