package com.cyf.sweethome.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.android.shuizu.myutillibrary.E
import com.android.shuizu.myutillibrary.fragment.BaseFragment
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getMessageDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.android.shuizu.myutillibrary.widget.GlideImageLoader
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome
import com.cyf.sweethome.activities.*
import com.cyf.sweethome.entity.*
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.toast

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */
class HomeFragment : BaseFragment() {

    private val bannerInfos = ArrayList<BannerInfo>()
    private val actBannerInfos = ArrayList<BannerInfo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        getHouseInfo()
        getBannerInfo()
        getActBannerInfo()
    }

    private fun initBanner() {
        val images = ArrayList<String>()
        //val titles = ArrayList<String>()
        for (i in 0 until bannerInfos.size) {
            images.add(bannerInfos[i].file_url.getImageUrl())
            //titles.add(bannerInfos[i].title)
        }
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.RIGHT)
        //banner.setBannerTitles(titles)
        banner.setImages(images).setImageLoader(GlideImageLoader()).start()
        banner.setOnBannerListener {
            val banner = bannerInfos[it]
            val intent = Intent(activity, MemberInfoDetailsActivity::class.java)
            intent.putExtra("id", banner.id)
            startActivity(intent)
        }

        banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                bannerText.text = bannerInfos[position].title
            }
        })
        banner.start()
    }

    private fun initActBanner() {
        val images = ArrayList<String>()
        //val titles = ArrayList<String>()
        for (i in 0 until actBannerInfos.size) {
            images.add(actBannerInfos[i].file_url.getImageUrl())
            //titles.add(bannerInfos[i].title)
        }
        //设置指示器位置（指示器居右）
        banner_act.setIndicatorGravity(BannerConfig.RIGHT)
        //banner.setBannerTitles(titles)
        banner_act.setImages(images).setImageLoader(GlideImageLoader()).start()
        banner_act.setOnBannerListener {
            val banner = actBannerInfos[it]
            val intent = Intent(activity, ActInfoDetailsActivity::class.java)
            intent.putExtra("id", banner.id)
            startActivity(intent)
        }

        banner_act.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                bannerActText.text = actBannerInfos[position].title
            }
        })
        banner_act.start()
    }

    private fun initViews() {
        houseAddress.setOnClickListener {
            startActivityForResult(Intent(activity, MyHouseActivity::class.java), 100)
        }
        ownerCheckRoom.setOnClickListener {
            startActivity(Intent(activity, CheckRoomLogActivity::class.java))
        }
        callRepair.setOnClickListener {
            startActivity(Intent(activity, SubmitRepairActivity::class.java))
        }
        message.setOnClickListener {
            it.context.toast("建设中...")
        }
        open_door.setOnClickListener {
            it.context.toast("建设中...")
        }
        self_pay.setOnClickListener {
            it.context.toast("建设中...")
        }
        house_sale.setOnClickListener {
            it.context.toast("建设中...")
        }
        volun_apply.setOnClickListener {
            it.context.toast("建设中...")
        }
        submit_info.setOnClickListener {
            getMemberStatus()
        }
        get_help.setOnClickListener {
            startActivity(Intent(activity, GetHelpActivity::class.java))
        }
        praise.setOnClickListener {
            startActivity(Intent(activity, PraiseActivity::class.java))
        }
        feedback.setOnClickListener {
            startActivity(Intent(activity, FeedbackActivity::class.java))
        }
        cloudTalk.setOnClickListener {
            it.context.toast("建设中...")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 101) {
            houseAddress.text = SweetHome.houseInfo?.fw_title
        }
    }

    private fun getBannerInfo() {
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(DJBANNER.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {

                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val bannerInfoListRes = Gson().fromJson(result, BannerInfoListRes::class.java)
                    bannerInfos.clear()
                    bannerInfos.addAll(bannerInfoListRes.retRes)
                    initBanner()
                }
            })
            postRequest()
        }
    }

    private fun getActBannerInfo() {
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(HDBANNER.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {

                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val bannerInfoListRes = Gson().fromJson(result, BannerInfoListRes::class.java)
                    actBannerInfos.clear()
                    actBannerInfos.addAll(bannerInfoListRes.retRes)
                    initActBanner()
                }
            })
            postRequest()
        }
    }

    private fun getMemberStatus() {
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(DJSQZT.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {

                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val houseInfoRes = Gson().fromJson(result, MemberStatusRes::class.java)
                    when (houseInfoRes.retRes.sh_status) {
                        0 -> {//未申请
                            startActivity(Intent(context, MemberApplyActivity::class.java))
                        }
                        1 -> {//审核中
                            getSuccessDialog(context, "您的党员认证申请还在审核中，请耐心等待！")
                        }
                        2 -> {//已通过
                            startActivity(Intent(context, MemberInfoNewsActivity::class.java))
                        }
                        3 -> {//已拒绝
                            getMessageDialog(
                                context,
                                "您的党员认证已被拒绝，需要重新申请吗？",
                                object : DialogUIListener() {
                                    override fun onPositive() {
                                        startActivity(
                                            Intent(
                                                context,
                                                MemberApplyActivity::class.java
                                            )
                                        )
                                    }

                                    override fun onNegative() {

                                    }
                                })
                        }
                    }
                }
            })
            setDialog()
            postRequest()
        }
    }

    private fun getHouseInfo() {
        KevinRequest.build(activity as Context).apply {
            setRequestUrl(DQFWXX.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {
                            activity?.finish()
                        }

                        override fun onNegative() {
                        }
                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val houseInfoRes = Gson().fromJson(result, HouseInfoRes::class.java)
                    SweetHome.houseInfo = houseInfoRes.retRes
                    houseAddress.text = SweetHome.houseInfo?.fw_title
                }
            })
            setDialog()
            postRequest()
        }
    }
}