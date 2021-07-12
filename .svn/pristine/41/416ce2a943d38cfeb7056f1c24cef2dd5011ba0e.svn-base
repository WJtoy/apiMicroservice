package com.wolfking.jeesite.modules.sd.entity.viewModel;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 订单提醒
 * 
 */
public class NoticeMessageItemVM implements Serializable
{

	private static final long serialVersionUID = 1L;

	public final static String URL_FEEDBACK_LINK = "sd/order/%s/alllist?%s";
	public final static String URL_FEEDBACK_PENDING_LINK = "sd/order/%s/alllist?%s";
	public final static String URL_APPABNORMALY_LINK = "sd/order/%s/alllist?%s";

	public static final int NOTICE_TYPE_FEEDBACK = 1; // 未读问题反馈(未读消息)
	public static final int NOTICE_TYPE_FEEDBACK_PENDING = 2; // 待处理问题反馈（反馈未处理）
	public static final int NOTICE_TYPE_APPABNORMALY = 3; // app异常(异常反馈)

	//标题
	private String name;
	//连接地址
	private String link = new String("");
	//消息类型
	private int noticeType = NOTICE_TYPE_FEEDBACK;

	private long qty = 0;
	private String icon = "";

	//以下是辅助字段
	private long customerId = 0;
	private long areaId = 0;
	private long createBy = 0;

	public NoticeMessageItemVM() {}

	public NoticeMessageItemVM(int noticeType) {
		this.noticeType = noticeType;
	}

	public NoticeMessageItemVM(String name, int noticeType, String link, String icon) {
		this.name = name;
		this.noticeType = noticeType;
		this.link = link;
		this.icon = icon;
		this.qty = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}


	public static HashMap<Integer,NoticeMessageItemVM> getDefaultMessages(Integer userType){
		HashMap<Integer,NoticeMessageItemVM> maps = Maps.newHashMap();
		switch (userType){
			case 2:
				maps.put(NOTICE_TYPE_FEEDBACK,new NoticeMessageItemVM("未读消息",NOTICE_TYPE_FEEDBACK,String.format(URL_FEEDBACK_LINK,"kefuOrderList","messageType=1"),"icon-feedback.png"));
				maps.put(NOTICE_TYPE_FEEDBACK_PENDING,new NoticeMessageItemVM("反馈未处理",NOTICE_TYPE_FEEDBACK_PENDING,String.format(URL_FEEDBACK_PENDING_LINK,"kefuOrderList","messageType=2"),"icon-feedback-pending.png"));
				maps.put(NOTICE_TYPE_APPABNORMALY,new NoticeMessageItemVM("异常反馈",NOTICE_TYPE_APPABNORMALY,String.format(URL_APPABNORMALY_LINK,"kefuOrderList","appAbnormalyFlag=1"),"icon-mobile.png"));
				break;
			case 3:
				maps.put(NOTICE_TYPE_FEEDBACK,new NoticeMessageItemVM("未读消息",NOTICE_TYPE_FEEDBACK,String.format(URL_FEEDBACK_LINK,"customerNew","messageType=1"),"icon-feedback.png"));
				maps.put(NOTICE_TYPE_FEEDBACK_PENDING,new NoticeMessageItemVM("反馈未处理",NOTICE_TYPE_FEEDBACK_PENDING,String.format(URL_FEEDBACK_PENDING_LINK,"customerNew","messageType=2"),"icon-feedback-pending.png"));
				break;
			case 4:
				maps.put(NOTICE_TYPE_FEEDBACK,new NoticeMessageItemVM("未读消息",NOTICE_TYPE_FEEDBACK,String.format(URL_FEEDBACK_LINK,"customerNew","messageType=1"),"icon-feedback.png"));
				maps.put(NOTICE_TYPE_FEEDBACK_PENDING,new NoticeMessageItemVM("反馈未处理",NOTICE_TYPE_FEEDBACK_PENDING,String.format(URL_FEEDBACK_PENDING_LINK,"customerNew","messageType=2"),"icon-feedback-pending.png"));
				break;
			case 7:
				maps.put(NOTICE_TYPE_FEEDBACK,new NoticeMessageItemVM("未读消息",NOTICE_TYPE_FEEDBACK,String.format(URL_FEEDBACK_LINK,"salesNew","messageType=1"),"icon-feedback.png"));
				maps.put(NOTICE_TYPE_FEEDBACK_PENDING,new NoticeMessageItemVM("反馈未处理",NOTICE_TYPE_FEEDBACK_PENDING,String.format(URL_FEEDBACK_PENDING_LINK,"salesNew","messageType=2"),"icon-feedback-pending.png"));
				break;
			default:
				break;
		}
		//maps.put(NOTICE_TYPE_FEEDBACK,new NoticeMessageItemVM("未读消息",NOTICE_TYPE_FEEDBACK,URL_FEEDBACK_LINK,"icon-feedback.png"));
		//maps.put(NOTICE_TYPE_FEEDBACK_PENDING,new NoticeMessageItemVM("反馈未处理",NOTICE_TYPE_FEEDBACK_PENDING,URL_FEEDBACK_PENDING_LINK,"icon-feedback-pending.png"));
		/*
		return Lists.newArrayList(new NoticeMessageVM[]{
				new NoticeMessageVM("未读消息",NOTICE_TYPE_FEEDBACK),
				new NoticeMessageVM("反馈未处理",NOTICE_TYPE_FEEDBACK_PENDING)
		});
		*/
		return maps;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}
}