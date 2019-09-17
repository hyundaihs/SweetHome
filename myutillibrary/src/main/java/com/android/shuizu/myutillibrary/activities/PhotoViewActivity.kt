package com.android.shuizu.myutillibrary.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.R
import com.android.shuizu.myutillibrary.fragment.PhotoFragment
import kotlinx.android.synthetic.main.activity_photo_view.*

/**
 * SweetHome
 * Created by 蔡雨峰 on 2019/9/17.
 */
class PhotoViewActivity : MyBaseActivity() {

    companion object {
        private val data = ArrayList<String>()
        private var pos = 0
        private var isURL = false
        fun setData(d: List<String>, url: Boolean = false, current: Int = 0) {
            data.clear()
            data.addAll(d)
            pos = current
            isURL = url
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_photo_view)
        setTitle("${pos + 1}/${data.size}")
        initViews()
    }

    private fun initViews() {
        val fragments = ArrayList<Fragment>()
        for (i in 0 until data.size) {
            fragments.add(PhotoFragment(data[i], isURL))
        }
        myPager.adapter = MyPagerAdapter(supportFragmentManager, fragments)
        myPager.setCurrentItem(pos, true)
        myPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                setTitle("${position + 1}/${data.size}")
            }

        })
    }

    private class MyPagerAdapter(
        fm: FragmentManager,
        val fragments: List<Fragment>
    ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

    }
}