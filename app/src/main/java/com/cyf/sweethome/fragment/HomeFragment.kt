package com.cyf.sweethome.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.cyf.sweethome.R
import com.cyf.sweethome.activities.CheckRoomLogActivity
import com.cyf.sweethome.activities.MyHouseActivity
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
    }

    private fun initViews() {
        houseAddress.setOnClickListener {
            startActivity(Intent(activity, MyHouseActivity::class.java))
        }
        ownerCheckRoom.setOnClickListener {
            startActivity(Intent(activity, CheckRoomLogActivity::class.java))
        }
    }
}