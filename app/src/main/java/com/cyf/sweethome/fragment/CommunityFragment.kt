package com.cyf.sweethome.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.cyf.sweethome.R
import kotlinx.android.synthetic.main.fragment_community.*

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class CommunityFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val titles = ArrayList<String>()
        titles.add("活动中心")
        titles.add("健康小屋")
        titles.add("我的社区")

        val fragments = ArrayList<Fragment>()
        fragments.add(EventsCenterFragment())
        fragments.add(HealthHouseFragment())
        fragments.add(MineCommunityFragment())

        comViewPager.adapter = MyPagerAdapter(childFragmentManager, fragments, titles)
        comTabLayout.setupWithViewPager(comViewPager)//此方法就是让tablayout和ViewPager联动
    }

    private class MyPagerAdapter(fm: FragmentManager, val fragments: List<Fragment>, val titles: List<String>) :
        FragmentPagerAdapter(fm) {

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