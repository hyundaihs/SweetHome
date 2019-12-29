package com.cyf.heartservice.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.cyf.heartservice.HeartService
import com.cyf.heartservice.R
import com.cyf.heartservice.fragments.*
import kotlinx.android.synthetic.main.activity_house_info.*

class HouseInfoActivity : MyBaseActivity() {

    var id = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_house_info)
        id = intent.getStringExtra("id") ?: "0"
        val name = intent.getStringExtra("name") ?: ""
        setTitle("${HeartService.userInfo?.xq_title}${name}")
        initViews()
    }

    private fun initViews() {
        val titles = ArrayList<String>()
        titles.add("房下客户")
        titles.add("物业账单")
        titles.add("房下报事")

        val fragments = ArrayList<Fragment>()
        fragments.add(HousePersonFragment(id))
        fragments.add(HousePaymentFragment(id))
        fragments.add(HouseRepairFragment(id))

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