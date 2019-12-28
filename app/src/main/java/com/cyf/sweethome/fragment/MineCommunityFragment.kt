package com.cyf.sweethome.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.widget.SwipeRefreshAndLoadLayout
import com.cyf.sweethome.R
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_mine_community.*
import kotlinx.android.synthetic.main.layout_swipe_refresh_empty_recycleview.*
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.layout_choose_building_list_item.view.*


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class MineCommunityFragment : BaseFragment() {

    val COLORS = listOf(
        R.color.color_23CA93,
        R.color.color_1FE1E3,
        R.color.color_7459FF,
        R.color.color_FFB700,
        R.color.color_FF4750
    )

    val pieEntries = ArrayList<PieEntry>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine_community, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initChart()
        initViews()
    }

    private fun initViews() {
        communityPhone.setOnClickListener {
            callPhone(communityPhone.text.toString())
        }
    }

    fun callPhone(phoneNum: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:" + phoneNum)
        intent.data = data
        startActivity(intent)
    }

    private fun initChart() {
        val colors = ArrayList<Int>()
        for (i in 0 until COLORS.size) {
            colors.add(resources.getColor(COLORS[i]))
        }
        pieEntries.add(PieEntry(1f, ""))
        pieEntries.add(PieEntry(2f, ""))
        pieEntries.add(PieEntry(3f, ""))
        pieEntries.add(PieEntry(4f, ""))
        pieEntries.add(PieEntry(5f, ""))
        val iPieDataSet = PieDataSet(pieEntries, "pie label")
        iPieDataSet.colors = colors
        iPieDataSet.valueTextColor = Color.TRANSPARENT
        iPieDataSet.sliceSpace = 0f   // 每块之间的距离
        val pieData = PieData(iPieDataSet)
        mPieChart.data = pieData

        mPieChart.legend.isEnabled = false//设置比例图
        mPieChart.description.isEnabled = false

        mPieChart.setNoDataText("")
        mPieChart.isRotationEnabled = false
        mPieChart.isDrawHoleEnabled = true
        mPieChart.isHighlightPerTapEnabled = false
        mPieChart.holeRadius = 80f//设置中间洞的大小
        mPieChart.setDrawEntryLabels(false)   // 将X值绘制到饼状图环切片内,否则不显示。默认true，记住颜色和环不要一样，否则会显示不出来
        mPieChart.setUsePercentValues(true)    // 表内数据用百分比替代，而不是原先的值。并且ValueFormatter中提供的值也是该百分比的。默认false
        mPieChart.centerText = "4.0" // 圆环中心的文字，会自动适配不会被覆盖
        mPieChart.setCenterTextColor(Color.BLACK)
        mPieChart.setCenterTextSize(20f)
        mPieChart.centerTextRadiusPercent = 100f // 中心文本边界框矩形半径比例，默认是100%.
        mPieChart.transparentCircleRadius =
            0f   // 设置环形与中心圆之间的透明圆环半径占图表半径的百分比。默认55%（比如，中心圆为50%占比，而透明环设置为55%占比，要去掉中心圆的占比，也就是环只有5%的占比）
        mPieChart.setTransparentCircleAlpha(50)    // 上述透明圆环的透明度[0-255]，默认100
        mPieChart.maxAngle = 360f    // 设置整个饼形图的角度，默认是360°即一个整圆，也可以设置为弧，这样现实的值也会重新计算
    }
}