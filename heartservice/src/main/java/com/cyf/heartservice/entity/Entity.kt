package com.cyf.heartservice.entity

import com.android.shuizu.myutillibrary.request.RequestResult
import com.cyf.heartservice.R

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/9/15/015.
 */


data class FileInfo(
    var id: String, // 文件id
    var file_url: String, // 文件地址
    var title: String, // 文件名称
    var file_size: String, // 文件大小
    var file_ext: String, // 文件后缀
    var resize_file: String // 缩略图地址
)

data class FileInfoRes(var retRes: FileInfo) : RequestResult()

data class ImageInfo(
    var file_url: String, // => 图片地址
    var resize_file_url: String, // => 缩略图地址
    var ext: String, // => png
    var file_size: String // => 112717
)


val HOUSE_STATUS = listOf("", "审核中", "审核已通过", "已拒绝")
val HOUSE_STATUS_COLOR = listOf(0, R.color.color_FAB10B, R.color.color_1FAF51, R.color.color_FF4753)

val CHECK_ROOM_STATUS = listOf("", "通过", "未通过")
val CHECK_ROOM_STATUS_COLOR = listOf(0, R.color.color_1FAF51, R.color.color_FF4753)

data class RepairRoomListItem(
    var type_id: Int, // 类型（1：报事报修，2：验房）
    var row_id: String, // 报事报修id/验房记录id
    var title: String, // 名称【业主验房】
    var contents: String, // 内容【周店1栋1单元101】
    var create_time: Long // 创建时间戳
)

data class RepairRoomListRes(var retRes: ArrayList<RepairRoomListItem>) : RequestResult()

data class CheckRoomDetails(
    var id: String, //  验房记录id
    var title: String, //  刘丽丽
    var phone: String, //  17139061224
    var contents: String, //  内容
    var sh_status: Int, //  审核状态（1：通过，2：未通过）
    var create_time: Long, //  创建时间戳
    var fw_title: String, //  周店1栋1单元101
    var img_lists: ArrayList<ImageInfo> //  Array（图片列表）
)

data class CheckRoomDetailsRes(var retRes: CheckRoomDetails) : RequestResult()

data class OperatingRecord(//操作记录
    var xqbsbx_id: String, // 报事保修id
    var title: String, // 报事已被提交，正在安排服务人员。
    var sh_status: Int, // 状态（1：待接单 2：待处理 3：待评价 4：已完成 0或不传为所有）
    var type_title:String,//状态标题
    var xqyg_title: String, // 服务人员姓名
    var xqyg_phone: String, // 服务人员电话
    var xqyg_file_url: String, // 服务人员头像
    var create_time: Long // 创建时间戳（1568510942）
)

data class WorkOrderDetails(
    var id: String, // 报事保修id
    var xqbsbxlx_title: String, // 绿化养护
    var title: String, // 报修人
    var phone: String, // 联系电话
    var contents: String, // 内容（灯坏了
    var yy_time: String, // 预约时间（2019-09-15 10:08）\
    var sh_status: Int, // 状态（1：待接单 2：待处理 3：待评价 4：已完成 0或不传为所有）
    var sh_title: String, //状态标题
    var create_time: Long, // 创建时间戳（1568510942）
    var fw_title: String, // 周店1栋1单元101
    var img_lists: ArrayList<ImageInfo>, // Array(图片列表)
    var log_lists: ArrayList<OperatingRecord>// Array（操作记录）
)

data class WorkOrderDetailsRes(var retRes: WorkOrderDetails) : RequestResult()