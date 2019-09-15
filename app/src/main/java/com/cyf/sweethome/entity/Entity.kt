package com.cyf.sweethome.entity

import com.android.shuizu.myutillibrary.request.RequestResult
import com.cyf.sweethome.R

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */

val HOUSE_STATUS = listOf("", "审核中", "审核已通过", "已拒绝")
val HOUSE_STATUS_COLOR = listOf(0, R.color.color18, R.color.color17, R.color.color19)

val CHECK_ROOM_STATUS = listOf("", "通过", "未通过")
val CHECK_ROOM_STATUS_COLOR = listOf(0, R.color.color17, R.color.color19)

data class HouseListItem(
    var id: String,// 申请id
    var xq_id: String, // 小区id
    var xqld_id: String, //楼栋id
    var xqdy_id: String, //单元id
    var xqfh_id: String, //房号id
    var xqyz_id: String, //业主id
    var xqyzsf_id: String, //业主身份id
    var xqyzsf_title: String, //业主身份名称
    var xq_title: String,// 小区名称
    var xqld_title: String,//楼栋名称（1栋）
    var xqdy_title: String,//单元名称（1单元）
    var xqfh_title: String,//房号（101）
    var cs: String,//楼层（1—）
    var title: String,//姓名（张三）
    var card_num: String,// 身份证号（610502199212247896）
    var sex: String,// 女
    var sh_status: Int,// 审核状态（1：审核中，2：通过，3：拒绝）
    var create_time: String,// 提交时间戳
    var fw_title: String// 房屋全称（周店1栋1单元101）*/
)

data class HouseListRes(var retRes: ArrayList<HouseListItem>) : RequestResult()

data class HouseInfo(
    var id: String, // 房号id
    var xq_id: String, // 小区id
    var xqld_id: String, // 楼栋id
    var xqdy_id: String, // 单元id
    var numbers: String, // 房屋编码
    var codes: String, // 房屋代码
    var cs: String, // 楼层
    var mj: String, // 面积
    var wyf: String, // 物业费
    var wyf_end_date: String, // 物业费截止日期
    var title: String, // 房号
    var title2: String, // 1栋 1单元 101
    var fw_title: String, // 全称（周店1栋1单元101）
    var fw_status: String, // 房屋状态（1：自主，2：出租，3：闲置）
    var xq_title: String, // 小区名 （周店）
    var xqld_title: String, // 楼栋名（1栋）
    var xqdy_title: String // 单元名（1单元）
)

data class CheckRoomLog(
    var id: String, // 验方记录id
    var title: String, // 刘丽丽
    var phone: String, // 17139061224
    var contents: String, // 挺好
    var sh_status: Int, // 审核状态（1：通过，2：未通过）
    var create_time: Long, // 创建时间戳
    var fw_title: String // 周店1栋1单元101
)

data class CheckRoomLogListRes(var retRes: ArrayList<CheckRoomLog>) : RequestResult()