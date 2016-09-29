package com.urgoo.common;

import android.net.Uri;

public class ZWConfig {

    // *******************COMMON***********************************************
    public final static String APPCODE = "zwExpress";
    public final static String VERSION = "1.0.0";

    public final static String CHARSET = "utf-8";
    public final static boolean IS_DEBUG = true;

    public final static String DIALOG_SHOW = "isDialog";
    public final static String SPLITOR_STR = "\t";
    public final static String SPLITOR_OBJ = "\n";

    // *******************NET***************************************************
    // public final static String URL_BASE =
    // "http://115.28.12.120/am990-dev/api/";
    public final static String URL_BASE = "";

    // 问事模块
    public final static String URL_ASK_LIVELIST = "get_document_list.php"; // 民生&图说
    public final static String URL_ASK_INTERACTLIST = "get_question_list.php"; // 互动
    public final static String URL_SEARCHRESLT = "search.php"; // 搜索结果
    public final static String URL_GETDOCUMENT = "get_document.php"; // 详情-民生资讯&图说政策
    public final static String URL_POSTCOMMENT = "post_document_comment.php"; // 发表评论
    public final static String URL_GETQUESTION = "get_question.php"; // 详情-社区互动
    public final static String URL_GETANSWERLIST = "get_answer_list_v2.php"; // 获取回答&评论
    public final static String URL_FAVORITE = "favorite_document.php"; // 收藏&取消
    public final static String URL_QUESTION = "favorite_question.php"; // 收藏&取消问答
    public final static String URL_VOTE = "vote_answer.php"; // 顶&踩
    public final static String URL_ANSWERPOST = "answer_question.php"; // 回答问题
    public final static String URL_QUESTIONPOST = "post_question.php";
    public final static String URL_COMMENTANSWER = "comment_answer.php";
    public final static String URL_GETANSWERCOMMENTLIST = "get_answer_comment_list.php";
    public final static String URL_PRAISE = "up_document.php"; // 为好人点赞

    // 办事
    public final static String URL_GETGUIDETAGLIST = "get_apply_guide_tag_list_v2.php"; // 办事首页
    public final static String URL_GETGUIDELIST = "get_apply_guide_list.php"; // 办事列表
    public final static String URL_GETGUIDEDEATIL = "get_apply_guide.php"; // 办事详情
    public final static String URL_GETSERVICESPOTLIST = "get_service_spot_street_list.php"; // 办事受理点列表
    public final static String URL_GETSERVICESPOT = "get_service_spot_street.php";
    public final static String URL_GETSTREETVOTELIST = "get_street_vote_list.php";
    public final static String URL_POSTSTREETVOTE = "post_street_vote.php";

    // 我的
    public final static String URL_LOGIN = "login.php"; // 登录
    public final static String URL_REGISTER = "register.php";
    public final static String URL_MYASK = "get_my_qa_v2.php";
    public final static String URL_MYANSWER = "get_my_qa_v2.php";
    public final static String URL_MYFAVQUE = "get_favorite_question_v2.php";
    public final static String URL_MYFAVCON = "get_document_list.php";
    public final static String URL_UPDATEUSERAVATAR = "update_user_avatar.php";
    public final static String URL_GETVALICODE = "send_verify_code.php";
    public final static String URL_USERVERIFYCODE = "user_verify_code.php";
    public final static String URL_RESETPASS = "change_password.php";
    public final static String URL_RESETPASSWORD = "reset_password.php";
    public final static String URL_GETUSERSTREET = "get_user_street.php"; // 我的街道
    public final static String URL_UPDATESTREET = "update_user_street.php"; // 街道变更
    public final static String URL_GETMYAFFAIRLIST = "get_my_affair_list.php"; // 我的事物列表
    public final static String URL_GETMYAFFAIRTIMELINE = "get_my_affair_timeline.php"; // 事务流程

    // 网络参数
    public final static String URL_FILE = "file";
    public final static int NET_SUCC = 0;
    public final static String NET_URL = "url";
    public final static String NET_PARAM = "param";
    public final static String NET_RESPONSE = "response";
    public final static String NET_CODE = "code";
    public final static String NET_MSG = "msg";
    public final static String NET_RESULTS = "results";
    public final static String NET_RESULT = "result";
    public final static String NET_MSGSUCC = "msgsucc";
    public final static String NET_MSGFAIL = "msgfail";
    public final static String NET_FILEPATH = "filepath";
    public final static String NET_FILE = "file";

    // *************************************
    public static final String ZWTDIR = "am990";
    public static final String AM990DIR = "cache";

    public static final String IMAGEDISKCACHEDIR = "imagediskcache";

    // 20150508 itap
    public final static String ITAPURL_BASE = "";

    public final static String AppZL_key = "AppZL";
    public final static String AppZL_value = "APP01";

    public final static String ACTION_type = "type";
    public final static String ACTION_login = "YHdl";
    public final static String ACTION_qblist = "QBLB_qb";
    public final static String ACTION_gnlist = "hdGNLB";
    public final static String ACTION_gnqblist = "QBLB_gn";
    public final static String ACTION_qbdetaillist = "QBXQ";
    public final static String ACTION_qbReply = "QBHF";
    public final static String ACTION_qbAdd = "XZQB";
    public final static String ACTION_settingpassword = "szMM";
    public final static String ACTION_userdetail = "YHXQ";
    public final static String ACTION_netFile = "scXP";
    public final static String ACTION_getNetFile = "xzXP";

    public final static String iconURL = "";

    public final static String ITAPNET_CODE = "success";

    public final static String versionURL = "";
    /**
     * 图片路径
     */
    public static Uri tempPicture;
    public static String pickPicture;

    public static boolean isLogin;
    public static boolean isShowRed;

    /**
     * 更多的页数
     */
    public final static int pageSize = 10;

    //未授权key
    //public final static String bmapKey = "7ae13368159d6a513eaa7a17b9413b4b";

    //本地已授权key
    //public final static String bmapKey = "UkU8miQKruSYckmFmBsAav4V";

    //通过签名已授权key
    public final static String bmapKey = "UkU8miQKruSYckmFmBsAav4V";

    //public final static String avatarURL = "http://192.168.0.132:8081/ITAP/servlet/front/APPS?type=xzXP&AppZL=APP01&SJHM=18671106441";

    //public final static String avatarSCURL = "http://139.129.12.253:8081/ZYDL/servlet/front/APPS?type=scXP&AppZL=APP01&SJHM=18721564883";
    //urgoo外网接口IP地址
    //小帅地址：
//    public final static String URGOOURL_BASE = "http://10.203.203.49:8080/urgoo/";
    //shihui测试接口  104 8088
//    public final static String URGOOURL_BASE = "http://10.203.203.86:8080/urgoo/";
    public final static String URGOOURL_BASE3 = "http://139.129.164.163:8080/urgoo/";
    //测试环境
//    public final static String URGOOURL_BASE = "http://www-test-urgoo.com/urgoo/";
    //生产环境
//    public final static String URGOOURL_BASE = "http://www-prd-urgoo.com/urgoo/";
//    小孙
    public final static String URGOOURL_BASE = "http://10.203.203.49:8082/urgoo/";

//    真实环境
//    public final static String URGOOURL_BASE = "http://139.129.164.163:8082/urgoo/";
//    public final static String URGOOURL_BASE = "http://10.203.203.86:8080/urgoo/";
//    public final static String URGOOURL_BASE = "http://10.203.203.86:8082/urgoo/";

    //public final static String URGOOURL_BASE = "http://115.28.50.163:8080/urgoo/001/001/account/account";
    public final static String ACTION_Account = URGOOURL_BASE + "001/001/account/parentAccount";
    public final static String ACTION_MyOrder = URGOOURL_BASE + "001/001/order/parentOrder";
    public final static String ACTION_Follow = URGOOURL_BASE + "001/001/order/parentOrder";
    public final static String ACTION_Client = URGOOURL_BASE + "001/001/student/client";
    public final static String ACTION_SearchClient = URGOOURL_BASE + "001/001/student/searchClient2";
    public final static String ACTION_StudentInfo = URGOOURL_BASE + "001/001/student/studentInfo";
    public final static String ACTION_Task = URGOOURL_BASE + "001/001/task/task";

    public final static String ACTION_Regist = URGOOURL_BASE + "001/001/login/regist";
    public final static String ACTION_Login = URGOOURL_BASE + "001/001/login/clientLogin";

    public final static String URGOOURL_BASE2 = " http://10.203.203.124:8080/urgoo/";
    public final static String ACTION_ClientRegist = URGOOURL_BASE + "001/001/login/clientRegist";

    public final static String ACTION_ClientVerifycode = URGOOURL_BASE + "001/001/login/getIdentifyingCode";
    public final static String ACTION_IdentifyingCode = URGOOURL_BASE + "001/001/login/identifyingCode";
    public static final String ACTION_clientFindPassword = URGOOURL_BASE + "001/001/login/clientFindPassword";
    public static final String ACTION_updatePassword = URGOOURL_BASE + "001/001/user/updatePassword";

    public static final String ACTION_clientupdatePassword = URGOOURL_BASE + "001/001/login/updatePassword";
    public static final String ACTION_myConsultant = URGOOURL_BASE + "001/001/client/myConsultant";
    public static final String ACTION_myInfo = URGOOURL_BASE + "001/001/client/myInfo";

    public static final String ACTION_searchConsultant = URGOOURL_BASE + "001/001/client/myConsultant";
    public static final String ACTION_nosignsearchConsultant = URGOOURL_BASE + "001/001/nosign/searchConsultant";
    public static final String ACTION_taskJz = URGOOURL_BASE + "001/001/task/taskJz";
    public static final String ACTION_parentScheduleHtml = URGOOURL_BASE + "001/001/student/parentFollowHtml";
    public static final String ACTION_parentOrder = URGOOURL_BASE + "001/001/order/parentOrder";
    public static final String ACTION_parentOrderIn = URGOOURL_BASE + "001/001/order/parentOrderIn";
    public static final String ACTION_parentAccount = URGOOURL_BASE + "001/001/account/parentAccount";
    public static final String ACTION_parentPlanningHtml = URGOOURL_BASE + "001/001/student/parentPlanningHtml";

    //public final static String ACTION_updateUserIcon = URGOOURL_BASE + "001/001/user/updateUserIcon";
    public final static String ACTION_updateUserIcon = URGOOURL_BASE + "001/001/user/editUserIcon";


    public final static String ACTION_reportSemesterJz = URGOOURL_BASE + "001/001/student/reportSemesterJz";

    public final static String Action_perfectInformation = URGOOURL_BASE + "001/001/user/perfectInformation";


    public final static String Action_selectCityList = URGOOURL_BASE + "001/001/profile/selectCityList";
    public final static String Action_selectCountryList = URGOOURL_BASE + "001/001/profile/selectCountryList";


    public final static String Action_helpJz = URGOOURL_BASE + "001/001/client/helpJz";
    public final static String Action_getTaskDetail = URGOOURL_BASE + "001/001/top/clientTop";


    //public final static String ACTION_HXPWD = "123456";

    //系统消息
    public static final String Action_updateInformation = URGOOURL_BASE + "/001/001/information/selectInformationSystemDetail";
    //个人消息
    public static final String Action_updateUserInformation = URGOOURL_BASE + "/001/001/information/selectInformationDetail";


    //杨德成20160505 start
    public static final String Action_taskDetail = URGOOURL_BASE + "001/001/task/taskDetailStu?";

    public static final String Action_getAvatarAndnickname = URGOOURL_BASE + "001/001/profile/getUserInfoByHxCode";


    public final static String ACTION_CustomerService = "ydc2001";
    public final static String ACTION_HXPWD = "123456";
    public final static String ACTION_CustomerServiceNickname = "优优";
    public final static String ACTION_CustomerServiceMessage = "Hi,this is UU,your assistant.How may I help you?";


    public static final String Action_parentAccountDetail = URGOOURL_BASE + "/001/001/account/parentAccountDetail?";

    public static final String Action_AccountInformationDetail = URGOOURL_BASE + "/001/001/information/selectInformationDetail";
    public static final String Action_getVesionUpList = URGOOURL_BASE + "/adminLogin/getVesionUpList";

    //Jz端   顾问详情client/counselorInfoContract    学生详情client/myInfo

    public static final String Action_clientprofile = URGOOURL_BASE + "/001/001/client/counselorInfoContract?";
    public static final String Action_studentInfoPage = URGOOURL_BASE + "/001/001/client/myInfo?";
    public static final String Action_searchCounselorList = URGOOURL_BASE + "001/001/nosign/searchCounselorList";
    public static final String Action_searchInfo = URGOOURL_BASE + "001/001/nosign/searchInfo";
    public static final String Action_advanceInfoClient = URGOOURL_BASE + "001/001/advance/advanceInfoClient";


    public static final String Action_isAdvanceRelation = URGOOURL_BASE + "/001/001/advance/isAdvanceRelation1";


    //待确定页面所用url
    public static final String URL_advanceUnconfirmeListClient = URGOOURL_BASE + "/001/001/advance/advanceUnconfirmeListClient";
    //已确认页面的url
    public static final String URL_advanceConfirmeListClient = URGOOURL_BASE + "/001/001/advance/advanceConfirmeListClient";
    //已结束
    public static final String URL_advanceCloseListClient = URGOOURL_BASE + "/001/001/advance/advanceCloseListClient";


    //红点
    public static final String URL_RED = URGOOURL_BASE + "/001/001/advance/redShow";

    //待确定详情
    public static final String URL_advanceDetailClient = URGOOURL_BASE + "/001/001/advance/advanceDetailClient?termType=2&";
    //已确定详情
    public static final String URL_advanceConfirmeDetailClient = URGOOURL_BASE + "/001/001/advance/advanceConfirmeDetailClient";
    public static final String URL_advanceConfirmeDetailClient2 = URGOOURL_BASE + "/001/001/advance/advanceConfirmeDetailClient2";
    //已结束详情
    public static final String URL_advanceDetailClosedClient = URGOOURL_BASE + "/001/001/advance/advanceDetailClosedClient?termType=2&";
    public static final String URL_advanceDetailClosedClient2 = URGOOURL_BASE + "/001/001/advance/advanceDetailClosedClient2?termType=2&";


    //获取到预约信息接口
    public static final String URL_advanceInfoClient = URGOOURL_BASE + "/001/001/advance/advanceInfoClient";
    public static final String URL_advanceInfoClient2 = URGOOURL_BASE + "/001/001/advance/advanceInfoClient2";
    public static final String URL_advanceInfoClient3 = URGOOURL_BASE + "/001/001/advance/advanceInfoClient3";


    // 提交预约接口
    public static final String Action_addAdvanceClient = URGOOURL_BASE + "001/001/advance/addAdvanceClient";

    //详情页的评价
    public static final String URL_advanceComment = URGOOURL_BASE + "/001/001/advance/advanceComment";
    public static final String URL_advanceComment2 = URGOOURL_BASE + "/001/001/advance/advanceComment2";

    public static final String URL_reAdvanceAccept = URGOOURL_BASE + "/001/001/advance/reAdvanceAccept";
    public static final String URL_advanceReason = URGOOURL_BASE + "/001/001/advance/advanceReason";


    public static final String Action_startAdvance = URGOOURL_BASE + "/001/001/advance/startAdvance";
    public static final String Action_updateAdvanceStatus = URGOOURL_BASE + "/001/001/advance/updateAdvanceStatus";
    public static final String Action_advanceCommentClient = URGOOURL_BASE + "/001/001/advance/advanceCommentClient";

    public static final String URL_cancelAdvanceClient = URGOOURL_BASE + "/001/001/advance/cancelAdvanceClient";


    public static final String ACTION_PINGYONG = URGOOURL_BASE + "001/001/client/understandService?";

    // 请求订单详情
//    public static final String ACTION_requestOrderDetails = URGOOURL_BASE + "001/001/order/getParentOrderDetail";
    public static final String ACTION_getOrderBalanceMoney = URGOOURL_BASE + "001/001/payorder/getOrderBalanceMoney";
    //支付结果处理
    public static final String ACTION_insertSubOrderDetail = URGOOURL_BASE + "001/001/payorder/insertSubOrderDetail";
    // 支付宝支付请求
    public static final String ACTION_requestLaunchAliPay = URGOOURL_BASE + "001/001/order/launchAliPay";
    // 微信支付请求
    public static final String ACTION_requestLaunchWechat = URGOOURL_BASE + "001/001/pay/tenPayLaunch2";
    // 银行卡支付请求
    public static final String ACTION_requestHuaRuiPay = URGOOURL_BASE + "001/001/pay/huaRuiPayLaunch";
    // 更新订单接口
    public static final String ACTION_requestUpdateOrder = URGOOURL_BASE + "001/001/order/updateUserOrderAfterLaunchAliPay";


    // 极光推送
    public static final String URL_sendPushForCounselor = URGOOURL_BASE + "001/001/jpush/sendPushForCounselor";


    // zoom接口
    // ZOOM创建邮箱账号和密码
    public static final String URL_requestInsertZoomAccount = URGOOURL_BASE + "001/001/zoom/insertZoomAccount";
    // ZOOM账号申请
    public static final String URL_requestCreateZoomUser = "https://api.zoom.us/v1/user/autocreate";
    // 创建房间
    public static final String URL_requestCreateMeeting = "https://api.zoom.us/v1/meeting/create";
    // ZOOM创建聊天室
    public static final String URL_requestCreateZoomChat = URGOOURL_BASE + "001/001/zoom/createZoomChat";

    //获取banner图
    public static final String URL_requestGetBanner = URGOOURL_BASE + "001/001/banner/selectBannerList";

    //杨德成20160505 end

    //public final static String URGOOURL_BASE = "http://115.28.50.163:8080/urgoo/";

    //public final static String URGOOURL_BASE = "http://10.203.203.129:8080/urgoo/";
    //是否显示join
    public static final String Action_isAdvanceRelationCoun = URGOOURL_BASE + "001/001/advance/isAdvanceRelationCoun";

    public static final String Action_selectZoomChatClientAndConsult = URGOOURL_BASE + "001/001/zoom/selectZoomChatClientAndConsult2";

    public static final String Action_zoomUserOpt = URGOOURL_BASE + "001/001/zoom/zoomUserOpt2";

    //首页顾问类型图标选择（圆圈图标）
    public static final String URL_requestSelectCounselorBannerList = URGOOURL_BASE + "001/001/counselorbanner/selectCounselorBannerList?";
    //首页直播LIVE列表
    public static final String URL_requestSelectZoomLiveList = URGOOURL_BASE + "001/001/live/selectZoomLiveList";
    //首页顾问推荐列表
    public static final String URL_requestGetMyCounselorListTop = URGOOURL_BASE + "001/001/client/getMyCounselorListTop";
    //顾问列表
    public static final String URL_requestGetCounselorList = URGOOURL_BASE + "001/001/nosign/getSearchCounselorList2";
    //筛选条件
    public static final String URL_requestGetCounselorFilter = URGOOURL_BASE + "001/001/nosign/screen2";
    //所有顾问
    public static final String URL_requestGetMyCounselorList = URGOOURL_BASE + "001/001/client/getMyCounselorList";
    //热门搜索
    public static final String URL_requestGetHotFilter = URGOOURL_BASE + "001/001/nosign/findInfo";
    //顾问详情
    public static final String URL_findCounselorDetail = URGOOURL_BASE + "/001/001/attention/findCounselorDetail";
    //查看次数
    public static final String URL_statCounselorCount = URGOOURL_BASE + "/001/001/attention/statCounselorCount";
    //顾问服务
    public static final String URL_selectCounselorServiceList = URGOOURL_BASE + "/001/001/attention/selectCounselorServiceList";
    //Banna
    public static final String URL_selectCounselorDetailSubList = URGOOURL_BASE + "/001/001/attention/selectCounselorDetailSubList";

    //取消关注
    public static final String URL_cancleFollow = URGOOURL_BASE + "/001/001/attention/cancleFollow";

    //取消关注
    public static final String URL_addFollow = URGOOURL_BASE + "/001/001/attention/addFollow";

    //顾问作品
    public static final String URL_selectCounselorWorksList = URGOOURL_BASE + "001/001/attention/selectCounselorWorksList";
    //我报名的活动-未开始
    public static final String URL_selectZoomLiveByParaentIdNoSatrt = URGOOURL_BASE + "001/001/user/selectZoomLiveByParaentIdNoSatrt";

    //Start杨德成 20160718 直播模块接口定义
    public static final String Action_selectZoomLiveNewest = URGOOURL_BASE + "001/001/livePage/selectZoomLiveNewest";
    public static final String Action_selectZoomPassed = URGOOURL_BASE + "001/001/livePage/selectZoomPassed";
    public static final String Action_selectZoomLiveDetail = URGOOURL_BASE + "001/001/livePage/selectZoomLiveDetail2";
    public static final String Action_selectLiveCommentList = URGOOURL_BASE + "001/001/livecomment/selectLiveCommentList";
    public static final String Action_insertLiveCommentContent = URGOOURL_BASE + "001/001/livecomment/insertLiveCommentContent";
    public static final String Action_addActivitySign = URGOOURL_BASE + "001/001/livePage/addActivitySign";
    public static final String Action_selectZoomPassedInDetail = URGOOURL_BASE + "001/001/livePage/selectZoomPassedInDetail";
    //end杨德成 20160718 直播模块接口定义

    //规划标签页任务列表
    public static final String URL_getStudentTaskListNewest = URGOOURL_BASE + "001/001/task/getStudentTaskListNewest";

    public final static String ACTION_selectParentInfo = URGOOURL_BASE + "001/001/user/selectParentInfo";
    public static final String Action_getZoomRoom = URGOOURL_BASE + "001/001/zoom/getZoomRoom";


    // 任务详情
    public static final String Action_getTaskDetailByTaskId = URGOOURL_BASE + "001/001/task/getTaskDetailByTaskId";
    // 我的报告
    public static final String Action_selectMyReport = URGOOURL_BASE + "001/001/task/selectMyReport";
    // 报告详情
    public static final String Action_selectStudentReportById = URGOOURL_BASE + "001/001/task/selectStudentReportById";
    //任务列表
    public static final String Action_getStudentTaskList2 = URGOOURL_BASE + "001/001/task/getStudentTaskList2";
    // 评论列表
    public static final String Action_taskCommentList = URGOOURL_BASE + "001/001/task/taskCommentList";
    // 评论
    public static final String Action_taskReplyComment = URGOOURL_BASE + "001/001/task/taskReplyComment";

    // 获取订单信息
    public static final String Action_confirmService = URGOOURL_BASE + "001/001/order/confirmService";
    // 下单
    public static final String Action_insertOrder = URGOOURL_BASE + "001/001/order/insertOrder";
    //订单timeline
    public static final String URL_requestGetPayTimeDetail = URGOOURL_BASE + "001/001/order/getPayTimeDetail";
    // 返还数据
    public static final String URL_insertOrderProtocol = URGOOURL_BASE + "001/001/order/insertOrderProtocol";
    //问卷调查
    public static final String URL_requestGetQuestionList = URGOOURL_BASE + "001/001/login/selectQuestionListAll";
    //提交问卷调查
    public static final String URL_requestRegistContent = URGOOURL_BASE + "001/001/login/clientRegistContentAndroid";
    // 二维码
    public static final String URL_createQrcode = URGOOURL_BASE + "001/001/cqrcode/createQrcode";

    // 规划列表
    public static final String URL_newTaskList = URGOOURL_BASE + "001/001/task/newTaskList";

    // 规划列表详情
    public static final String URL_newTaskDetail = URGOOURL_BASE + "001/001/task/newTaskDetail";
    // 规划列表详情
    public static final String URL_newTimeLine = URGOOURL_BASE + "001/001/task/newTimeLine";
    // 消息
    public static final String URL_requestInformationPerson = URGOOURL_BASE + "001/001/information/selectInformationPerson";
    //我的一级红点
    public static final String URL_requestSelectRedCount = URGOOURL_BASE + "001/001/information/selectRedCount";
    // 预约2级页面红点
    public static final String URL_requestRedShowAdvance = URGOOURL_BASE + "001/001/advance/redShowAdvance";
    // 预约2级界面红点清除
    public static final String URL_requestCleanRedShowAdvance = URGOOURL_BASE + "001/001/advance/cleanRedShowAdvance";
    // 消息列表
    public static final String URL_requestSelectInformationPerson = URGOOURL_BASE + "001/001/information/selectInformationPerson";
    // 三方协议
    public static final String URL_requestAgreement = URGOOURL_BASE + "001/001/client/xyJz";
    // 专辑列表
    public static final String URL_requestGetAlbumList = URGOOURL_BASE + "001/001/album/selectAlbumList";
    // 直播列表
    public static final String URL_requestGetLiveList = URGOOURL_BASE + "001/001/live/findZoomLiveList";
    // 我关注的顾问
    public static final String URL_requestFollowConsultants = URGOOURL_BASE + "001/001/user/getSearchCounselorListByParaentId";
    // 我收藏的视频
    public static final String URL_requestFollowVideos = URGOOURL_BASE + "001/001/album/selectMyVideo";
    // 个人详情
    public final static String URL_requestGetUserInfo = URGOOURL_BASE + "001/001/user/selectParentInfo";
    //我报名的活动
    public static final String URL_requestMyActivites = URGOOURL_BASE + "001/001/user/selectZoomLiveByParaentId";
}