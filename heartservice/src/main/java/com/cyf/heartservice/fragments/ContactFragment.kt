package com.cyf.heartservice.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.shuizu.myutillibrary.fragment.BaseFragment
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
class ContactFragment : BaseFragment() {

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
//                        val contactBeans = ArrayList<ContactBean>()
//                        for (i in 0 until contactListRes.retRes.size) {
//                            val c = contactListRes.retRes[i]
//                            val contactBean = ContactBean()
//                            contactBean.id = i.toString()
//                            contactBean.name = c.title
//                            contactBeans.add(contactBean)
//                        }
                        contactView.setData(contactListRes.retRes, false)
                        contactView.setContactListener(MyListener())
                    }
                })
                setDialog()
                postRequest()
            }
        }
    }

//    inner class ContactAdapter(val data:List<Contact>) :
//        MyBaseAdapter(R.layout.layout_contact_list_item) {
//
//        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//            super.onBindViewHolder(holder, position)
//            val contact = data[position]
//            Picasso.with(holder.itemView.context).load(contact.file_url.getImageUrl())
//                .resize(100,100)
//                .error(R.mipmap.contact_title_no_image)
//                .into(holder.itemView.photo)
//            holder.itemView.name.text = contact.title
//            holder.itemView.contents.text = "${contact.type_title}  ${contact.xq_title}"
//        }
//
//        override fun getItemCount(): Int = data.size
//    }

    inner class MyListener : ContactListener<ContactBean> {
        override fun loadAvatar(imageView: ImageView, contactBean: ContactBean) {
            imageView.setImageResource(R.mipmap.contact_title_no_image)
            if(contactBean.file_url.isNotEmpty()){
                Picasso.with(context).load(contactBean.file_url.getImageUrl())
                    .resize(100, 100)
                    .error(R.mipmap.contact_title_no_image)
                    .into(imageView)
            }
        }

        override fun onClick(item: ContactBean?) {

        }

        override fun onLongClick(item: ContactBean?) {

        }


    }

}