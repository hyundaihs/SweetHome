package com.cyf.sweethome.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.android.shuizu.myutillibrary.MyBaseActivity
import com.android.shuizu.myutillibrary.request.KevinRequest
import com.android.shuizu.myutillibrary.utils.getErrorDialog
import com.android.shuizu.myutillibrary.utils.getSuccessDialog
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.cyf.sweethome.R
import com.cyf.sweethome.SweetHome.Companion.CHOOSE_BUILDING_RESULT
import com.cyf.sweethome.SweetHome.Companion.CHOOSE_COMMUNITY_RESULT
import com.cyf.sweethome.entity.*
import com.cyf.sweethome.utils.PickerUtil
import com.dou361.dialogui.listener.DialogUIListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_juming_renzheng.*
import org.jetbrains.anko.toast

class JumingRenzhengActivity : MyBaseActivity() {

    var communityId = ""
    var communityName = ""
    var roomId = ""
    var roomName = ""
    var shenfenId = ""
    var userInfos = ArrayList<UserInfo>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == CHOOSE_COMMUNITY_RESULT) {
            communityId = data?.getStringExtra("chooseId") ?: ""
            communityName = data?.getStringExtra("chooseName") ?: ""
            renzhengShequ.text = communityName
            roomId = ""
            roomName = ""
        }
        if (resultCode == CHOOSE_BUILDING_RESULT) {
            roomId = data?.getStringExtra("chooseId") ?: ""
            roomName = data?.getStringExtra("chooseName") ?: ""
            renzhengFangjian.text = roomName
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentBaseView(R.layout.activity_juming_renzheng)
        setTitle("居民认证")
        getYezhuShenfenList()
        init()
    }

    private fun init() {
        renzhengSex.setOnClickListener {
            PickerUtil.showSexPicker(this, object : OnOptionsSelectChangeListener,
                OnOptionsSelectListener {
                override fun onOptionsSelect(
                    options1: Int,
                    options2: Int,
                    options3: Int,
                    v: View?
                ) {
                    renzhengSex.text = if (options1 == 0) "男" else "女"
                }

                override fun onOptionsSelectChanged(options1: Int, options2: Int, options3: Int) {

                }
            })
        }

        renzhengShequ.setOnClickListener {
            startActivityForResult(Intent(this, ChooseCommunityActivity::class.java), 155)
        }

        renzhengFangjian.setOnClickListener {
            if (communityId.isEmpty() || communityName.isEmpty()) {
                renzhengShequ.error = "请先选择小区"
                toast("请先选择小区")
            }else{
                val intent = Intent(this, ChooseRoomActivity::class.java)
                intent.putExtra("id", communityId)
                intent.putExtra("name", communityName)
                startActivityForResult(intent, 155)
            }
        }

        renzhengShenfen.setOnClickListener {
            PickerUtil.showUserInfoPicker(this, userInfos, object : OnOptionsSelectChangeListener,
                OnOptionsSelectListener {
                override fun onOptionsSelect(
                    options1: Int,
                    options2: Int,
                    options3: Int,
                    v: View?
                ) {
                    shenfenId = userInfos[options1].id
                    renzhengShenfen.text = userInfos[options1].title
                }

                override fun onOptionsSelectChanged(options1: Int, options2: Int, options3: Int) {

                }
            })
        }
        submit.setOnClickListener {
            submit()
        }
    }

    private fun getYezhuShenfenList() {
        KevinRequest.build(this).apply {
            setRequestUrl(YZSFLISTS.getInterface())
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error, object : DialogUIListener() {
                        override fun onPositive() {
                            //finish()
                        }

                        override fun onNegative() {
                        }

                    })
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    val userInfoListRes = Gson().fromJson(result, UserInfoListRes::class.java)
                    userInfos.clear()
                    userInfos.addAll(userInfoListRes.retRes)
                    init()
                }
            })
            setDialog()
            postRequest()
        }
    }

    private fun check(): Boolean {
        when {
            renzhengName.text.isEmpty() -> {
                renzhengName.error = "姓名不能为空"
                return false
            }
            renzhengCardnum.text.isEmpty() -> {
                renzhengCardnum.error = "身份证不能为空"
                return false
            }
            renzhengSex.text.isEmpty() -> {
                renzhengSex.error = "性别不能为空"
                return false
            }
            renzhengShequ.text.isEmpty() -> {
                renzhengShequ.error = "社区不能为空"
                return false
            }
            renzhengFangjian.text.isEmpty() -> {
                renzhengFangjian.error = "房间不能为空"
                return false
            }
            renzhengShenfen.text.isEmpty() -> {
                renzhengShenfen.error = "身份不能为空"
                return false
            }
            else -> return true
        }
    }

    private fun submit() {
        if (!check()) {
            return
        }
        val map = mapOf(
            Pair("title", renzhengName.text.toString()),
            Pair("card_num", renzhengCardnum.text.toString()),
            Pair("sex", renzhengSex.text.toString()),
            Pair("xqfh_id", roomId),
            Pair("xqyzsf_id", shenfenId)
        )
        KevinRequest.build(this).apply {
            setRequestUrl(TJSFRZ.getInterface(map))
            setErrorCallback(object : KevinRequest.ErrorCallback {
                override fun onError(context: Context, error: String) {
                    getErrorDialog(context, error)
                }
            })
            setSuccessCallback(object : KevinRequest.SuccessCallback {
                override fun onSuccess(context: Context, result: String) {
                    getSuccessDialog(context, "提交成功",object :DialogUIListener(){
                        override fun onPositive() {
                            startActivity(Intent(context, HomepageActivity::class.java))
                            finish()
                        }

                        override fun onNegative() {
                        }

                    })
                }
            })
            setDialog()
            setDataMap(map)
            postRequest()
        }
    }
}