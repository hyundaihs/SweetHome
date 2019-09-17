package com.cyf.heartservice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.cyf.heartservice.R

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
class ContactFragment :BaseFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }
}