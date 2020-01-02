package com.cyf.heartservice.entity

import com.android.shuizu.myutillibrary.request.RequestResult
import com.chezi008.libcontacts.bean.ContactBean
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

data class Msg(
    var type_id: String, // 1
    var title: String, // 小秘书
    var contents: String, // 系统已于2019年通过
    var date_str: String // 今天
)

data class MsgListRes(var retRes: ArrayList<Msg>) : RequestResult()

data class RepairRoomListItem(
    var id: String, //通知id
    var type_id: Int, // 类型（1：报事报修，2：验房）
    var row_id: String, // 报事报修id/验房记录id
    var title: String, // 名称【业主验房】
    var contents: String, // 内容【周店1栋1单元101】
    var create_time: Long, // 创建时间戳
    var is_read: Int //是否已读 1是  0 否
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
    var type_title: String,//状态标题
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

//data class Contact(
//    var account: String, // 电话
//    var title: String, // 李主管
//    var file_url: String, // 头像
//    var type_title: String, // 楼宇管家
//    var xq_title: String, // 周店
//    var py: String // 拼音首字母
//):ContactBean(title)

data class ContactListRes(var retRes: List<ContactBean>) : RequestResult()

data class UserInfo(
    var id: String, // id
    var xq_id: String, // 小区id
    var type_id: String, // 账号类型
    var account: String, // 账号
    var file_url: String, // 头像
    var title: String, // 昵称（刘小哲）
    var login_time: String, // 登录时间戳
    var create_time: String, // 创建时间戳
    var sf_title: String, // 角色
    var xq_title: String, // 小区名
    var jpush_status: Int  //是否开启推送
)

data class UserInfoRes(var retRes: UserInfo) : RequestResult()
data class UserInfoListRes(var retRes: ArrayList<UserInfo>) : RequestResult()

data class MemberApplyInfo(
    var id: String, // 申请id
    var phone: String, // 电话
    var title: String, // 姓名
    var card_num: String, // 身份证号
    var file_url: String, // 头像
    var sex: String, // 性别
    var rdsj: String, // 入党时间
    var dzbmc: String, // 党支部名称
    var dzbdz: String, // 党支部地址
    var contents: String, // 备注
    var sh_status: Int, // 审核状态（0：未申请，1：审核中，2：已通过，3：已拒绝）
    var create_time: Long // 申请时间戳
)

data class MemberApplyInfoRes(var retRes: MemberApplyInfo) : RequestResult()

data class MemberApplyInfoListRes(var retRes: ArrayList<MemberApplyInfo>) : RequestResult()

data class VolunApplyInfo(
    var id: String, // 申请id
    var phone: String, // 电话
    var title: String, // 姓名
    var card_num: String, // 身份证号
    var file_url: String, // 头像
    var sex: String, // 性别
    var contents: String, // 备注
    var sh_status: Int, // 审核状态（0：未申请，1：审核中，2：已通过，3：已拒绝）
    var create_time: Long // 申请时间戳
)

data class VolunApplyInfoRes(var retRes: VolunApplyInfo) : RequestResult()

data class VolunApplyInfoListRes(var retRes: ArrayList<VolunApplyInfo>) : RequestResult()

data class ShareCode(
    var tg_code: String, // 推广码
    var ewm_file_url: String  //二维码图片地址（upload/qrcode/1f0e3dad99908345f7439f8ffabdffc4_l9.png
)

data class ShareCodeRes(var retRes: ShareCode) : RequestResult()

data class HelpContent(
    var app_contents: String
)

data class HelpContentRes(var retRes: HelpContent) : RequestResult()

data class Notify(//与物业知识内容一样
    var id: String, // 通知公告id
    var stype_id: String, // 分类id
    var title: String, // 标题
    var view_nums: String, // 浏览量
    var app_contents: String, //详情（html）
    var create_time: Long, // 发布时间（时间戳）
    var stype_title: String // 分类标题
)

data class NotifyRes(var retRes: Notify) : RequestResult()

data class NotifyListRes(var retRes: ArrayList<Notify>) : RequestResult()

open class Room(
    var id: String = "0", //单元id
    var title: String = "", //单元名称
    var cs: String = "0" //总层数
)

data class Floor(
    var lists: ArrayList<Room> //房间
) : Room()

data class Building(
    var lists: ArrayList<Floor> //楼层
) : Room()

data class BuildingListRes(var retRes: ArrayList<Building>) : RequestResult()

data class CommunityListRes(var retRes: ArrayList<Room>) : RequestResult()

data class JumingInfo(
    var xqyz_id: String, // 业主id
    var xqfh_id: String, // 房号id
    var title: String, // 王中华
    var phone: String, // 13429895721
    var fw_title: String, // 周店1栋第1单元0701
    var xqyzsf_title: String // 业主(身份)
)

data class JumingInfoListRes(var retRes: ArrayList<JumingInfo>) : RequestResult()

data class JumingDetails(
    var id: String, // 业主id
    var title: String, // 王中华
    var phone: String, // 13429895720
    var sex: String, // 男
    var card_num: String, // 420102198707243010
    var xqyzsf_title: String // 业主(身份)
)

data class JumingDetailsRes(var retRes: JumingDetails) : RequestResult()
data class JumingDetailsListRes(var retRes: ArrayList<JumingDetails>) : RequestResult()

data class HouseInfo(
    var xqyz_id: String, // 业主id
    var xqld_title: String, // 1栋
    var xqdy_title: String, // 第1单元
    var xqfh_title: String, // 0701
    var yf_status: String, // 验房状态（1：通过，2：未通过）
    var create_time: Long, // 时间戳
    var xqfh_id: String // 房号id
)

data class HouseInfoListRes(var retRes: ArrayList<HouseInfo>) : RequestResult()

data class Payment(
    var id: String, //id
    var dates: String, //日期
    var price: String //金额
)

data class SumPayment(
    var all_price: String, //总欠费
    var lists: ArrayList<Payment> //待缴列表
)

data class SumPaymentRes(var retRes: SumPayment) : RequestResult()

data class Repair(
    var id: String, // 报事保修id
    var xqfh_id: String, // 房号id
    var xqbsbxlx_title: String, // 家具维修
    var fw_title: String, // 周店3栋第2单元0101
    var contents: String, // 沙发断了
    var sh_status: Int, // 状态（1：待接单 2：待处理 3：待评价 4：已完成）
    var sh_title: String, // 状态标题（待接单）
    var create_time: Long // 时间戳
)

data class RepairListRes(var retRes: ArrayList<Repair>) : RequestResult()

data class VersionInfo(
    var v_title: String, // 版本名称
    var v_num: Int, // 版本号
    var http_url: String, // 下载地址
    var contents: String // 更新说明（html）
)

data class VersionInfoRes(var retRes: VersionInfo) : RequestResult()

data class ZXFXInfo(
    var id: String, // 6
    var jsr: String, // 经手人
    var xq_title: String, // 周店
    var xqld_title: String, // 1栋
    var xqdy_title: String, // 第1单元
    var xqfh_title: String, // 0301
    var xqyz_id: String, // 业主id
    var fwzt_title: String, // 自住（房屋兴致）
    var xqyz_title: String, // 刘涛（业主）
    var xqyz_phone: String, // 业主电话）
    var sbr_title: String, // 张大哥（申办人）
    var sbr_phone: String, // 申办人电话）
    var sbr_card_num: String, // 申办人身份证）
    var zxfzr_title: String, // 装修负责人）
    var zxfzr_phone: String, // 装修负责人电话）
    var zxfzr_card_num: String, // 装修负责人身份证）
    var zxqx: String, // 2019-12-31（装修截止日期）
    var zxdw: String, // 装修单位
    var blyy: String, // 办理原因
    var yzqrfs: String, // 当面（业主确认方式）
    var contents: String, // 备注
    var file_url_wz: String, // 照片（一张）
    var file_url_jz: String, // 行驶证照片（一张）
    var create_time: Long // 时间戳
)

data class ZXFXInfoRes(var retRes: ZXFXInfo) : RequestResult()

data class JpushInfo(
    var type: String //消息类型
)