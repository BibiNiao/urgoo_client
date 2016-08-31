package com.urgoo.Interface;

import android.os.Handler;

/**
 * Created by Administrator on 2016/7/18.
 */
public interface ZhiBoInterface {

    void getList(final Handler handler, String token, String type, String termType, int pageNo,
                 int pageSize, int MSGTYPE_QB_FAIL, int MSGTYPE_QBINTERACT_SUCC);

    void getoldDataList(final Handler handler, String token, String type, String termType, int pageNo,
                 int pageSize, int MSGTYPE_QB_FAIL, int MSGTYPE_QBINTERACT_SUCC);
}
