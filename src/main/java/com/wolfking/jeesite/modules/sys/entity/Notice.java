package com.wolfking.jeesite.modules.sys.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.common.utils.DateUtils;

import java.util.Date;

/**
 * 通知
 * 
 */
public class Notice extends LongIDDataEntity<Notice>
{

	private static final long serialVersionUID = 1L;

	public static final int NOTICE_TYPE_FEEDBACK = 1; // 问题反馈
	public static final int NOTICE_TYPE_APPABNORMALY = 2; // app异常
	public static final int NOTICE_TYPE_PARTS   = 3;// 配件
	public static final int NOTICE_TYPE_RETURN_PARTS   = 4; // 返件
	public static final int NOTICE_TYPE_CUSTOMER   = 9; // 客户公告
	public static final int NOTICE_TYPE_NOTICE   = 10; // 公告

	//用户ID
	private Long userId = 0l;
	//标题
	private String title;
	//内容
	private String context;
	//关联id，如订单id
	private Long referId = 0l;
	//关联单号
	private String referNo;
	//连接地址
	private String link;
	//消息类型
	private int noticeType = 0;
	//单据数据库分片
	private String quarter = "";
	//发送时间
	private String sendDate = "";

	public Notice() {}

	public Notice(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Long getReferId() {
		return referId;
	}

	public void setReferId(Long referId) {
		this.referId = referId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}

	public String getReferNo() {
		return referNo;
	}

	public void setReferNo(String referNo) {
		this.referNo = referNo;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getSendDate() {
		return this.createDate==null?"": DateUtils.formatDate(this.createDate,"MM-dd HH:mm");
	}

	@Override
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
		this.sendDate = createDate==null?"": DateUtils.formatDate(createDate,"MM-dd HH:mm");
	}
}