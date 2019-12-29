package com.cyf.heartservice.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.shuizu.myutillibrary.fragment.MyBaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.chezi008.libcontacts.bean.ContactBean
import com.chezi008.libcontacts.listener.ContactListener
import com.cyf.heartservice.R
import com.cyf.heartservice.entity.ContactListRes
import com.cyf.heartservice.entity.LXRLISTS
import com.cyf.heartservice.entity.getImageUrl
import com.cyf.heartservice.entity.getInterface
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_contact.*

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
class ContactFragmentMy : MyBaseFragment() {

//    private val contact = ArrayList<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getListData()
        deleteSearch.setOnClickListener {
            searchText.setText("")
        }
    }

    private fun getListData() {
        context?.let {
            KevinRequest.build(it).apply {
                setRequestUrl(LXRLISTS.getInterface())
                setErrorCallback(object : KevinRequest.ErrorCallback {
                    override fun onError(context: Context, error: String) {
                        getErrorDialog(context, error)
                    }
                })
                setSuccessCallback(object : KevinRequest.SuccessCallback {
                    override fun onSuccess(context: Context, result: String) {
                        val contactListRes = Gson().fromJson(result, ContactListRes::class.java)
                        contactView.setData(contactListRes.retRes, false)
                        contactView.setContactListener(MyListener())
                    }
                })
                setDialog()
                postRequest()
            }
        }
    }

    inner class MyListener : ContactListener<ContactBean> {
        override fun loadAvatar(imageView: ImageView, contactBean: ContactBean) {
            imageView.setImageResource(R.mipmap.contact_title_no_image)
            if (contactBean.file_url.isNotEmpty()) {
                Picasso.with(context).load(contactBean.file_url.getImageUrl())
                    .resize(300, 300)
                    .error(R.mipmap.contact_title_no_image)
                    .into(imageView)
            }
        }

        override fun onClick(item: ContactBean) {
            callPhone(item.account)
        }

        override fun onLongClick(item: ContactBean) {

        }


    }

    fun callPhone(phoneNum: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:" + phoneNum)
        intent.data = data
        startActivity(intent)
    }

}