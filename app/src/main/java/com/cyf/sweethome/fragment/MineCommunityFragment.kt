package com.cyf.sweethome.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.shuizu.myutillibrary.E
import com.android.shuizu.myutillibrary.fragment.MyBaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.cyf.sweethome.R
import com.cyf.sweethome.entity.*
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_mine_community.*


/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class MineCommunityFragment : MyBaseFragment() {

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
        initViews()
        getXQInfo()
        getXQPF()
        initChart()
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
        mPieChart.centerText = "" // 圆环中心的文字，会自动适配不会被覆盖
        mPieChart.setCenterTextColor(Color.BLACK)
        mPieChart.setCenterTextSize(20f)
        mPieChart.centerTextRadiusPercent = 100f // 中心文本边界框矩形半径比例，默认是100%.
        mPieChart.transparentCircleRadius =
            0f   // 设置环形与中心圆之间的透明圆环半径占图表半径的百分比。默认55%（比如，中心圆为50%占比，而透明环设置为55%占比，要去掉中心圆的占比，也就是环只有5%的占比）
        mPieChart.setTransparentCircleAlpha(50)    // 上述透明圆环的透明度[0-255]，默认100
        mPieChart.maxAngle = 360f    // 设置整个饼形图的角度，默认是360°即一个整圆，也可以设置为弧，这样现实的值也会重新计算
    }

    private fun fillInfo(xqInfo: XQInfo){
        communityName.text = xqInfo.address
        communityPhone.text = xqInfo.wygly_phone
        communityAddress.text = xqInfo.city + xqInfo.area + xqInfo.street + xqInfo.address
        communityTime.text = xqInfo.jc_date
        communityTotalArea.text = xqInfo.mj
        communityPlotRatio.text = xqInfo.rjlv
        communityGreenRate.text = xqInfo.lhlv
        communityDeveloper.text = xqInfo.kfs_title
        communityCompany.text = xqInfo.wygs_title
        communityStreet.text = xqInfo.city + xqInfo.area + xqInfo.street
        communityNeighbor.text = xqInfo.jwhmc
        communityPolice.text = xqInfo.sspcs
    }

    private fun fillPF(xqpf: XQPFInfo){
        workCount.text = xqpf.counts
        workScore.text = xqpf.zhpf
        val v1 = xqpf.arr[1]
        val v2 = xqpf.arr[2]
        val v3 = xqpf.arr[3]
        val v4 = xqpf.arr[4]
        val v5 = xqpf.arr[5]
        lable1.text = "${v1}%${lable1.text}"
        lable2.text = "${v2}%${lable2.text}"
        lable3.text = "${v3}%${lable3.text}"
        lable4.text = "${v4}%${lable4.text}"
        lable5.text = "${v5}%${lable5.text}"
        pieEntries.clear()
        if (v1 != null && v1 > 0) {
            pieEntries.add(PieEntry(v1/10.toFloat(), ""))
        }
        if (v2 != null && v2 > 0) {
            pieEntries.add(PieEntry(v2/10.toFloat(), ""))
        }
        if (v3 != null && v3 > 0) {
            pieEntries.add(PieEntry(v3/10.toFloat(), ""))
        }
        if (v4 != null && v4 > 0) {
            pieEntries.add(PieEntry(v4/10.toFloat(), ""))
        }
        if (v5 != null && v5 > 0) {
            pieEntries.add(PieEntry(v5/10.toFloat(), ""))
        }
        mPieChart.centerText = xqpf.zhpf
        mPieChart.notifyDataSetChanged()
        mPieChart.invalidate()

    }

    private fun getXQInfo(){
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(XQINFO.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val xQInfoRes = Gson().fromJson(result, XQInfoRes::class.java)
                    fillInfo(xQInfoRes.retRes)
                }
            })
            postRequest()
        }
    }

    private fun getXQPF(){
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(XQPF.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val xQPFRes = Gson().fromJson(result, XQPFInfoRes::class.java)
                    fillPF(xQPFRes.retRes)
                }
            })
            postRequest()
        }
    }
}