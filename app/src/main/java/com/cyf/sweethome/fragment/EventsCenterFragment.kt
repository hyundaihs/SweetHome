package com.cyf.sweethome.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.cyf.sweethome.R

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class EventsCenterFragment:BaseFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events_center, container, false)
    }
}
