package com.cyf.sweethome.entity

import com.android.shuizu.myutillibrary.request.RequestResult
import com.cyf.sweethome.R
import java.io.Serializable

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
    var file_url: String, // :String, // 图片地址
    var resize_file_url: String, // :String, // 缩略图地址
    var ext: String, // :String, // png
    var file_size: String // :String, // 112717
)

val HOUSE_STATUS = listOf("", "审核中", "审核已通过", "已拒绝")
val HOUSE_STATUS_COLOR = listOf(0, R.color.color_FAB10B, R.color.color_1FAF51, R.color.color_FF4753)

val CHECK_ROOM_STATUS = listOf("", "通过", "不通过")
val CHECK_ROOM_STATUS_COLOR = listOf(0, R.color.color_1FAF51, R.color.color_FF4753)

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

data class HouseInfoRes(var retRes: HouseInfo) : RequestResult()

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

data class ContactInfo(
    var id: String, // 联系人id
    var title: String, // 姓名
    var phone: String // 电话
)

data class ContactInfoListRes(var retRes: ArrayList<ContactInfo>) : RequestResult()

data class RepairType(
    var id: String, // 报事保修分类id
    var title: String // 名称
)

data class RepairTypeListRes(var retRes: ArrayList<RepairType>) : RequestResult()

data class WorkOrderNumListRes(var retRes: Map<Int, String>) : RequestResult()

data class WorkOrderListItem(
    var id: String, // 报事保修id
    var xqbsbxlx_title: String, // 绿化养护
    var title: String, // 刘大哥
    var phone: String, // 18062626737
    var contents: String, // 灯坏了
    var yy_time: String, // 预约时间（2019-09-15 09:39）
    var sh_status: Int, // 状态（1：待接单 2：待处理 3：待评价 4：已完成 0或不传为所有）
    var sh_title: String,//状态标题
    var create_time: Long, // 创建时间戳（1568510942）
    var fw_title: String // 周店1栋1单元101
)

data class WorkOrderListRes(var retRes: ArrayList<WorkOrderListItem>) : RequestResult()

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

data class UserInfo(
    var id: String, // 用户id
    var xq_id: String, // 小区id
    var phone: String, // 电话账号
    var card_num: String, // 身份证
    var title: String, // 姓名
    var file_url: String, // 头像/证件照
    var zc_file_url: String, // 注册照
    var sex: String, // 女
    var login_time: String, // 登录时间
    var create_time: String, // 创建时间
    var xqfh_id: String, // 当前房号id（未认证通过时为0）
    var jpush_status: Int  //是否开启推送
) : Serializable

data class UserInfoRes(var retRes: UserInfo) : RequestResult()

data class UserInfoListRes(var retRes: ArrayList<UserInfo>) : RequestResult()

data class MemberStatus(
    var sh_status: Int //审核状态（0：未申请，1：审核中，2：已通过，3：已拒绝）
)

data class MemberStatusRes(var retRes: MemberStatus) : RequestResult()

data class MemberType(
    var id: String, // 党建分类id
    var title: String // 标题
)

data class MemberTypeListRes(var retRes: ArrayList<MemberType>) : RequestResult()

data class MemberInfo(
    var id: String, // 党建id
    var title: String, // 标题
    var sub_title: String, // 简介
    var file_url: String, //图片
    var app_contents: String, // 详情（html）
    var view_nums: String, // 阅读量
    var pl_nums: String, // 评论量
    var create_time: Long, // 发布时间
    var is_sq: Int, // 是否申请（0|1）
    var stype_id: String, // 党建分类id
    var stype_title: String // 党建分类标题
)

data class MemberInfoRes(var retRes: MemberInfo) : RequestResult()

data class MemberInfoListRes(var retRes: ArrayList<MemberInfo>) : RequestResult()

open class CommentBase(
    var id: String = "", // 评论id
    var title: String = "", // 评论详情
    var create_time: Long = 0, // 时间戳
    var account_id: String = "", // 发布人账号id
    var account_title: String = "", // 发布人昵称
    var account_file_url: String = "", // 发布人头像
    var hf_account_title: String = "", // 回复对象昵称
    var hf_account_file_url: String = "", // 回复对象头像
    var zan_nums: Int = 0, // 点赞数
    var pl_nums: Int = 0, // 回复数
    var my: Int = 0, // 是否是自己发布的（0|1）
    var is_sq: Int = 0 //是否申请
)

data class CommentInfo(
    var hf_lists: ArrayList<CommentBase> //回复列表
) : CommentBase()

data class CommentInfoListRes(var retRes: ArrayList<CommentInfo>) : RequestResult()

data class BannerInfo(
    var id: String, //党建id
    var title: String, //标题
    var file_url: String //图片
)

data class BannerInfoListRes(var retRes: ArrayList<BannerInfo>) : RequestResult()

data class ActInfo(
    var id: String = "", // 活动id
    var stype_id: String = "", // 活动分类id
    var title: String = "", // 标题
    var sub_title: String = "", // 简介
    var file_url: String = "", // 图片
    var app_contents: String = "", // 详情（html）
    var date_time: String = "", // 活动时间
    var view_nums: Int = 0, // 浏览量
    var pl_nums: Int = 0, // 评论量
    var bm_nums: Int = 0, // 报名量
    var hd_status: String = "", // 活动状态id
    var create_time: Long = 0, // 发布时间（时间戳）
    var stype_title: String = "", // 分类标题（人文社区）
    var hd_status_title: String = "", // 活动状态标题（未开始）
    var is_bm: Int = 0, // 是否报名（0|1）
    var is_sq: Int = 0 //是否申请
)

data class ActInfoRes(var retRes: ActInfo) : RequestResult()

data class ActInfoListRes(var retRes: ArrayList<ActInfo>) : RequestResult()

data class CheckBodyInfo(
    var id: String, // 体检记录id
    var numbers: String, // 编号
    var title: String, // 刘涛
    var dates: String, // 2019-11-21
    var sex: String, // 女
    var sub_title: String // 周店物业服务中心
)

data class CheckBodyInfoListRes(var retRes: ArrayList<CheckBodyInfo>) : RequestResult()

data class CheckReportInfo(//体检报告
    var diastolic: String, // 舒张压
    var diastolic_str: String, // 舒张压评价（偏高）
    var systolic: String, // 收缩压
    var systolic_str: String, // 收缩压评价（偏高）
    var pulseRate: String, // 79
    var bmi: String, // BMI 27.95
    var bmi_str: String, // bmi评价（肥胖）
    var height: String, // 身高 168
    var weight: String, // 体重 78
    var temperature: String, // 体温
    var temperature_str: String // 体温评价（偏低）
)

data class CheckReport(
    var title: String, // 姓名
    var id: String, // id
    var dates: String, // 日期：2019-11-20
    var datas: CheckReportInfo, // Array（体检数据）
    var create_time: String, // 提交时间戳
    var age: String // 年龄
)

data class CheckReportRes(var retRes: CheckReport) : RequestResult()

data class ShareCode(
    var tg_code: String, // 推广码
    var ewm_file_url: String  //二维码图片地址（upload/qrcode/1f0e3dad99908345f7439f8ffabdffc4_l9.png
)

data class ShareCodeRes(var retRes: ShareCode) : RequestResult()

data class AboutUs(
    var app_contents: String
)

data class AboutUsRes(var retRes: AboutUs) : RequestResult()

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

data class VersionInfo(
    var v_title: String, // 版本名称
    var v_num: Int, // 版本号
    var http_url: String, // 下载地址
    var contents: String // 更新说明（html）
)

data class VersionInfoRes(var retRes: VersionInfo) : RequestResult()

data class LoginInfo(
    var login_verf: String, // 登录密钥（自动登录用）
    var is_rz: Int //是否认证
)

data class LoginInfoRes(var retRes: LoginInfo) : RequestResult()

data class XQInfo(
    var id: String, // 小区id
    var province: String, // 湖北
    var city: String, // 武汉
    var area: String, // 洪山区
    var street: String, // 洪山街道
    var address: String, // 洪山区88号
    var title: String, // 周店小区
    var kfs_title: String, // 开发商名称
    var jc_date: String, // 2018-09-01（建成日期）
    var mj: String, // 17000（总面积）
    var wygs_title: String, // 物业公司名称
    var rjlv: String, // 80.00（容积率）
    var lhlv: String, // 90.00（绿化率）
    var wygly_phone: String, // 物业电话
    var jwhmc: String, // 居委会名称
    var sspcs: String // 派出所名称
)

data class XQInfoRes(var retRes: XQInfo) : RequestResult()

data class XQPFInfo(
    var counts: String, //报事总数
    var zhpf: String, //4.8（平均分）
    var arr: Map<Int, Int> //Array（百分比）
)

data class XQPFInfoRes(var retRes: XQPFInfo) : RequestResult()