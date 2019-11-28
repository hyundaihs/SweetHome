package com.android.shuizu.myutillibrary.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2018/1/6/006.
 */
open class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }
}