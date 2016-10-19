package com.urgoo.live.biz;

import android.content.Context;

import com.urgoo.common.ZWConfig;
import com.urgoo.data.SPManager;
import com.urgoo.net.EventCode;
import com.urgoo.net.HttpEngine;
import com.urgoo.net.StringRequestCallBack;

import java.util.HashMap;

/**
 * Created by bb on 2016/8/18.
 */
public class LiveManager {
    private static LiveManager sInstance;
    private Context mContext;

    private LiveManager(Context context) {
        this.mContext = context;
    }


    public static LiveManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new LiveManager(context);
        }
        return sInstance;
    }

    /**
     * 直播最新动态
     *
     * @param type
     * @param termType
     * @param pageNo
     * @param pageSize
     * @param callback
     */
    public void getZoomLiveNewest(String type, String termType, int pageNo, int pageSize, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("type", type);
        params.put("termType", termType);
        params.put("page", String.valueOf(pageNo));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeZoomLiveNewest, ZWConfig.Action_selectZoomLiveNewest, params, callback);
    }

    /**
     * 往期直播
     *
     * @param type
     * @param termType
     * @param pageNo
     * @param pageSize
     * @param callback
     */
    public void getZoomPassed(String type, String termType, int pageNo, int pageSize, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("type", type);
        params.put("termType", termType);
        params.put("page", String.valueOf(pageNo));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeZoomPassed, ZWConfig.Action_selectZoomPassed, params, callback);
    }

    /**
     * 获取直播详情
     *
     * @param liveId
     * @param callback
     */
    public void getZoomLiveDetail(String liveId, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("liveId", liveId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeZoomLiveDetail, ZWConfig.Action_selectZoomLiveDetail, params, callback);
    }

    /**
     * 直播评论
     *
     * @param liveId
     * @param type
     * @param termType
     * @param pageNo
     * @param callback
     */
    public void getZhiBoPinglunList(String liveId, String type, String termType, int pageNo, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("type", type);
        params.put("termType", termType);
        params.put("liveId", liveId);
        params.put("page", String.valueOf(pageNo));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetZhiBoPinglunList, ZWConfig.Action_selectLiveCommentList, params, callback);
    }

    /**
     * 获取专辑列表
     *
     * @param pageNo
     * @param callback
     */
    public void getAlbumList(int pageNo, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("page", String.valueOf(pageNo));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetAlbumList, ZWConfig.URL_requestGetAlbumList, params, callback);
    }

    /**
     * 获取直播列表
     *
     * @param pageNo
     * @param callback
     */
    public void getLiveList(int pageNo, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("page", String.valueOf(pageNo));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetLiveList, ZWConfig.URL_requestGetLiveList, params, callback);
    }

    /**
     * 获取专辑详情
     *
     * @param albumTypeId
     * @param page
     * @param callback
     */
    public void getAlbumDetail(int albumTypeId, int page, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("albumTypeId", String.valueOf(albumTypeId));
        params.put("page", String.valueOf(page));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetAlbumDetail, ZWConfig.URL_requestGetAlbumDetailList, params, callback);
    }

    /**
     * 获取视频详情
     *
     * @param albumId
     * @param callback
     */
    public void getVideoDetail(String albumId, StringRequestCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("albumId", albumId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetVideoDetail, ZWConfig.URL_requestGetVideoDetail, params, callback);
    }

    /**
     * 获取评论
     *
     * @param liveId
     * @param page
     * @param flag     1 直播 2 视频
     * @param callBack
     */
    public void getCommentList(String liveId, int page, String flag, StringRequestCallBack callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("liveId", liveId);
        params.put("flag", flag);
        params.put("page", String.valueOf(page));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeGetCommentList, ZWConfig.URL_requestGetCommentList, params, callBack);
    }

    /**
     * 发送评论
     *
     * @param liveId
     * @param content
     * @param flag
     * @param targetId 0 评论   非0 回复
     * @param callBack
     */
    public void postComment(String liveId, String content, String flag, String targetId, StringRequestCallBack callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("liveId", liveId);
        params.put("content", content);
        params.put("flag", flag);
        params.put("targetId", targetId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodePostComment, ZWConfig.URL_requestPostComment, params, callBack);
    }

    /**
     * 二级回复列表
     *
     * @param liveId
     * @param flag
     * @param targetId
     * @param page
     * @param callBack
     */
    public void getReplayList(String liveId, String flag, String targetId, int page, StringRequestCallBack callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("liveId", liveId);
        params.put("flag", flag);
        params.put("targetId", targetId);
        params.put("page", String.valueOf(page));
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeReplyList, ZWConfig.URL_requestGetReplyList, params, callBack);
    }

    /**
     * 删除评论
     *
     * @param targetId
     * @param callBack
     */
    public void delComment(String targetId, StringRequestCallBack callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SPManager.getInstance(mContext).getToken());
        params.put("targetId", targetId);
        HttpEngine.getInstance(mContext).sendPostRequest(EventCode.EventCodeDelComment, ZWConfig.URL_requestDelComment, params, callBack);
    }
}
