package com.cyf.heartservice.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.cyf.heartservice.R
import com.cyf.heartservice.fragments.BaseInfoFragment
import com.cyf.heartservice.fragments.HouseInfoFragment
import kotlinx.android.synthetic.main.activity_juming_details.*

class JumingDetailsActivity : MyBaseActivity() {

    var id = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_juming_details)
        id = intent.getStringExtra("id") ?: "0"
        val name = intent.getStringExtra("name") ?: "用户信息"
        val phone = intent.getStringExtra("phone") ?: ""
        setTitle("${name} ${phone}")
        addRightImageBtn(R.mipmap.call_phone_blue, View.OnClickListener {
            callPhone(phone)
        })
        initViews()
    }

    private fun callPhone(phoneNum: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:" + phoneNum)
        intent.data = data
        startActivity(intent)
    }

    private fun initViews() {
        val titles = ArrayList<String>()
        titles.add("基本信息")
        titles.add("房屋信息")

        val fragments = ArrayList<Fragment>()
        fragments.add(BaseInfoFragment(id))
        fragments.add(HouseInfoFragment(id))

        comViewPager.adapter = MyPagerAdapter(supportFragmentManager, fragments, titles)
        comTabLayout.setupWithViewPager(comViewPager)//此方法就是让tablayout和ViewPager联动
    }

    private class MyPagerAdapter(
        fm: FragmentManager,
        val fragments: List<Fragment>,
        val titles: List<String>
    ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}