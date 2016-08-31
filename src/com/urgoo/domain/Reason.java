package com.urgoo.domain;

import java.util.ArrayList;

/**
 * Created by urgoo_01 on 2016/7/14.
 */
public class Reason {
    private ArrayList<reasonBean> reasonList;

    public static class reasonBean {
        private String reason;
        private String advanceReasonId;
        private String status;

        @Override
        public String toString() {
            return "reasonBean{" +
                    "reason='" + reason + '\'' +
                    ", advanceReasonId='" + advanceReasonId + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String mReason) {
            reason = mReason;
        }

        public String getAdvanceReasonId() {
            return advanceReasonId;
        }

        public void setAdvanceReasonId(String mAdvanceReasonId) {
            advanceReasonId = mAdvanceReasonId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String mStatus) {
            status = mStatus;
        }
    }
}
