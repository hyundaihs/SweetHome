package com.android.shuizu.myutillibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shuizu.myutillibrary.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.File

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
class PhotoFragment(val file: String, val isURL: Boolean) : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(isURL) {
            Picasso.with(context).load(file).into(photo_view)
        }else{
            Picasso.with(context).load(File(file)).into(photo_view)
        }
    }
}