package com.urgoo.net;

/**
 * Created by bb on 2016/7/7.
 */
public enum EventCode {
    /**
     * 注册ZOOM用户
     */
    EventCodeSignUpZoomUser,
    /**
     * ZOOM创建邮箱账号和密码
     */
    EventCodeZoomAccount,
    /**
     * 获取Banner图
     */
    EventCodeGetBanner,
    /**
     * 选择顾问按钮
     */
    EventCodeSelectCounselor,
    /**
     * 首页直播LIVE列表
     */
    EventCodeSelectZoomLive,
    /**
     * 顾问列表
     */
    EventCodeGetTranslateCounselor,
    /**
     * 首页推荐列表
     */
    EventCodeGetMyCounselorListTop,
    /**
     * 顾问列表
     */
    EventCodeGetCounselorList,
    /**
     * 筛选条件
     */
    EventCodeGetCounselorFilter,
    /**
     * 热门搜索
     */
    EventCodeGetHotFilter,
    /**
     * 所有顾问
     */
    EventCodeGetMyCounselorList,
    /**
     * 我关注的顾问
     */
    EventCodeFollowConsultants,
    /**
     * 我收藏的视频
     */
    EventCodeFollowVideos,
    /**
     * 支付宝详情
     */
    EventCodeAliPayDetail,
    /**
     * 微信支付详情
     */
    EventCodeWechatPayDetail,
    /**
     * 银行卡支付详情
     */
    EventCodeHuaRuiPay,
    /**
     * 获取订单详情
     */
    EventCodeGetPayOrder,
    /**
     * 更新订单
     */
    EventCodeUpdateUser,
    /**
     * 规划标签页任务列表
     */
    EventCodeGetStudentTaskListNewest,
    // 列表
    selectMyReport,
    //获取订单信息
    parentOrderPay,
    //下单
    insertOrder,

    /**
     * 提交 ID
     */
    insertOrderProtocol,

    /**
     * 订单的timeline
     */

    EventCodeGetPayTimeDetail,
    /**
     * 登录自己服务器
     */
    EventCodeLoginUrgoo,
    /**
     * 问卷调查
     */
    EventCodeQuestionList,
    /**
     * 提交问卷
     */
    EventCodeRegistContent,
    /**
     * 获取验证码
     */
    EventCodeGetVerifyCode,
    /**
     * 注册
     */
    EventCodeRegist,
    /**
     * 直播最新动态
     */
    EventCodeZoomLiveNewest,
    /**
     * 往期直播
     */
    EventCodeZoomPassed,
    /**
     * 直播详情
     */
    EventCodeZoomLiveDetail,
    /**
     * 二维码
     */
    createQrcode,
    /**
     * 规划列表
     */
    newTaskList,
    /**
     * 规划列表详情
     */
    newTaskDetail,
    /**
     * 规划详情
     */
    EventCodeNewTimeLine,
    /**
     * 未开始
     */
    EventCodeGetNotDataList,
    /**
     * 进行中
     */
    EventCodeGetOngDataList,
    /**
     * 直播评论
     */
    EventCodeGetZhiBoPinglunList,
    /**
     * 已确定的详情
     */
    EvetCodeAdvanceConfirmeDetailClient2,
    /**
     * 待确定的详情
     */
    EventCodeAdvanceConfirmeDetailClient1,
    /**
     * 已结束详情
     */
    EventCodeAdvanceDetailClosedClient3,
    /**
     * 接收预约
     */
    EventCodeReAdvanceAccept,
    /**
     * 顾问被查看调用
     */
    EventCodeStatCounselorCount,
    /**
     * 顾问详情BANNA
     */
    EventCodeSelectCounselorDetailSubList,
    /**
     * 顾问详情 基本信息
     */
    EventCodeFindCounselorDetail,
    /**
     * 顾问详情 服务项目
     */
    EventCodeSelectCounselorServiceList,
    /**
     * 顾问详情 关注
     */
    EventCodeAddFollow,
    /**
     * 顾问详情 取消关注
     */
    EventCodeCancleFollow,
    /**
     * 系统消息
     */
    EventCodegetInformationPerson,
    /**
     * 一级界面红点
     */
    EventCodeSelectRedCount,
    /**
     * 消息列表
     */
    EventCodeMessageList,
    /**
     * 更新系统消息
     */
    EventCodeUpdateMessage,
    /**
     * 更新个人消息
     */
    EventCodeUpdateUserMessage,
    /**
     * 专辑列表
     */
    EventCodeGetAlbumList,
    /**
     * 直播列表
     */
    EventCodeGetLiveList,
    /**
     * 获取个人信息
     */
    EventCodeGetUserInfo,
    /**
     * 我的活动
     */
    EventCodeGetMyAcitivites,
    /**
     * 用户资料
     */
    EventCodeGetUserData,
    /**
     * 学生评价
     */
    EventCodeGetStuEvaluation,
    /**
     * 专辑详情
     */
    EventCodeGetAlbumDetail,
    /**
     * 视频详情
     */
    EventCodeGetVideoDetail,
    /**
     * 获取评论
     */
    EventCodeGetCommentList,
    /**
     * 发表评论
     */
    EventCodePostComment,
    /**
     * 回复列表
     */
    EventCodeReplyList,
    /**
     * 删除评论
     */
    EventCodeDelComment,
}
