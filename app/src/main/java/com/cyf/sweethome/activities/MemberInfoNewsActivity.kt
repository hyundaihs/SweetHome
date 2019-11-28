package com.cyf.sweethome.activities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.DJSTYPE
import com.cyf.sweethome.entity.MemberType
import com.cyf.sweethome.entity.MemberTypeListRes
import com.cyf.sweethome.entity.getInterface
import com.cyf.sweethome.fragment.MemberListFragment
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_member_info_news.*

class MemberInfoNewsActivity : MyBaseActivity() {

    var data = ArrayList<MemberType>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_member_info_news)
        allType()
        init()
    }

    private fun allType() {
        KevinRequest.build(this).apply {
            setRequestUrl(DJSTYPE.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {
                            finish()
                        }

                        override fun onNegative() {

                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val memberTypeListRes = Gson().fromJson(result, MemberTypeListRes::class.java)
                    data.clear()
                    data.addAll(memberTypeListRes.retRes)
                    init()
                }
            })
            setDialog()
            postRequest()
        }
    }

    private fun init() {
        setTitle("党员信息提报")
        val fragments = ArrayList<Fragment>()

        for (i in 0 until data.size) {
            val memberListFragment = MemberListFragment(data[i].id)
            fragments.add(memberListFragment)
        }

        viewpagerMember.adapter = MyPagerAdapter(supportFragmentManager, fragments, data)
        tabLayoutMember.setupWithViewPager(viewpagerMember)//此方法就是让tablayout和ViewPager联动
    }

    private class MyPagerAdapter(
        fm: FragmentManager,
        val fragments: List<Fragment>,
        val titles: List<MemberType>
    ) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position].title
        }
    }
}