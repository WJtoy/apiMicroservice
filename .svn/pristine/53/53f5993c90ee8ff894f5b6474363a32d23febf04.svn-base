package com.wolfking.jeesite.modules.sd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.sd.utils.FeedbackItemAdapter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 问题反馈答复
 */
@JsonAdapter(FeedbackItemAdapter.class)
public class FeedbackItem extends LongIDDataEntity<FeedbackItem> {

	private static final long serialVersionUID = 1L;
	
	// Fields
	private Long feedbackId;//问题反馈
	private String quarter = "";//数据库分片，与订单相同
	private Integer floor = 0;//楼层
	private String createName;//回复人
	private Integer userType;//用户类型 0:客户 1:KKL
	private Integer contentType = 0;//0-文本 1：图片
	private String type="item";//辅助属性，不同步到数据库

	private String createDateString = "";//前端使用
	// Constructors
	
	public FeedbackItem() {
	}
	
	public FeedbackItem(Long id) {
		this();
		this.id = id;
	}

	@Min(value=1,message="楼层必须大于0")
	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	@JsonIgnore
	@NotNull(message="问题反馈不能为空")
	public Long getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	//日期字符串
	public String getCreateDateString() {
		if (this.createDate != null){
			return DateUtils.formatDate(this.createDate,"yyyy-MM-dd HH:mm");
		}else{
			return this.createDateString;
		}
	}

	public void setCreateDateString(String createDateString) {
		this.createDateString = createDateString;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
}