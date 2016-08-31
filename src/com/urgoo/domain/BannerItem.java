package com.urgoo.domain;

/**
 * banner项 实体
 *
 */
public class BannerItem {
	/** 打开h5 */
	public static final String ACTION_OPEN_H5 = "1";
	/** 打开直播详情 */
	public static final String ACTION_OPEN_ZOOM = "2";
	/** 打开顾问详情 */
	public static final String ACTION_OPEN_GUWEN = "3";

	/** 点击动作类型【1、打开帖子；2、打开指定网页】 */
	private String type;
	/** 点击后的目标【当action问1时，打开h5；当action为2时，打开直播详情】 */
	private String targetId;
	/** 图片地址 */
	private String picUrl;
	
	public BannerItem() {
		super();
	}

	public BannerItem(String type, String targetId, String picUrl) {
		super();
		this.type = type;
		this.targetId = targetId;
		this.picUrl = picUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@Override
	public String toString() {
		return "BannerItem{" +
				"type='" + type + '\'' +
				", targetId=" + targetId +
				", picUrl='" + picUrl + '\'' +
				'}';
	}
}