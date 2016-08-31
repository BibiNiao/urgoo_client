package com.urgoo.service.model;

import java.util.ArrayList;

/**
 * Created by dff on 2016/8/23.
 */
public class PlanTaskEntity {

    private String insertDatetime;
    private String replyContent;
    private String userName;
    private String userIcon;
    private int userInfoId;
    private String type;
    private ArrayList<AttachedFile> attachedFile;

    public ArrayList<AttachedFile> getAttachedFile() {
        return attachedFile;
    }

    public void setAttachedFile(ArrayList<AttachedFile> mAttachedFile) {
        attachedFile = mAttachedFile;
    }

    public String getInsertDatetime() {
        return insertDatetime;
    }

    public void setInsertDatetime(String mInsertDatetime) {
        insertDatetime = mInsertDatetime;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String mReplyContent) {
        replyContent = mReplyContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String mUserName) {
        userName = mUserName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String mUserIcon) {
        userIcon = mUserIcon;
    }

    public int getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(int mUserInfoId) {
        userInfoId = mUserInfoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String mType) {
        type = mType;
    }


    public class AttachedFile {
        private String fileName;
        private String fileSize;
        private String iconUrl;
        private String type;
        private String url;

        @Override
        public String toString() {
            return "AttachedFile{" +
                    "fileName='" + fileName + '\'' +
                    ", fileSize='" + fileSize + '\'' +
                    ", iconUrl='" + iconUrl + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String mFileName) {
            fileName = mFileName;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String mFileSize) {
            fileSize = mFileSize;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String mIconUrl) {
            iconUrl = mIconUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String mType) {
            type = mType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String mUrl) {
            url = mUrl;
        }
    }

    @Override
    public String toString() {
        return "PlanTaskEntity{" +
                "insertDatetime='" + insertDatetime + '\'' +
                ", replyContent='" + replyContent + '\'' +
                ", userName='" + userName + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", userInfoId=" + userInfoId +
                ", type='" + type + '\'' +
                ", attachedFile=" + attachedFile +
                '}';
    }
}
