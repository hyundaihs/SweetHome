package com.cyf.sweethome.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.cyf.sweethome.R
import com.cyf.sweethome.fragment.WorkOrderListFragment
import kotlinx.android.synthetic.main.activity_work_order_list.*

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/16.
 */
class WorkOrderListActivity : MyBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_work_order_list)
        setTitle("报事工单")
        val page = intent.getIntExtra("page", 0)
        initViews(page)
    }

    private fun initViews(page: Int) {
        val titles = ArrayList<String>()
        titles.add("全部工单")
        titles.add("待接单")
        titles.add("待处理")
        titles.add("待评价")

        val fragments = ArrayList<Fragment>()
        fragments.add(WorkOrderListFragment(0))
        fragments.add(WorkOrderListFragment(1))
        fragments.add(WorkOrderListFragment(2))
        fragments.add(WorkOrderListFragment(3))

        workOrderViewPager.adapter = MyPagerAdapter(supportFragmentManager, fragments, titles)
        workOrderTabLayout.setupWithViewPager(workOrderViewPager)//此方法就是让tablayout和ViewPager联动
        workOrderViewPager.setCurrentItem(page, true)
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