/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sd.entity;


import com.wolfking.jeesite.common.persistence.LongIDDataEntity;

/**
 * 投诉单附件表
 * 
 * @author ryan
 * @date 2018-01-27
 */
public class OrderComplainAttachment extends LongIDDataEntity<OrderComplainAttachment>
{

	private static final long serialVersionUID = 1L;

	public static int ATTACHMENTTYPE_APPLY=0;
	public static int ATTACHMENTTYPE_JUDEG=1;
	public static int ATTACHMENTTYPE_COMPLETE=2;

	private Long complainId;
	private String quarter = "";//数据库分片，与订单相同
	private String fileName = "";
	private String filePath = "";
	private int attachmentType = 0;//附件类型，0-投诉附件 1-判定附件 2-处理附件

	//辅助字段
	private int index = 0;
	private String strId = "";//因js对Long支持问题，客户端用此字段回传id

	public OrderComplainAttachment()
	{
		super();
	}

	public OrderComplainAttachment(String filePath)
	{
		super();
		this.filePath = filePath;
	}

	@Override
	public void setId(Long id){
		super.setId(id);
		if(id != null) {
			this.strId = String.valueOf(id);
		}
	}

	public Long getComplainId() {
		return complainId;
	}

	public void setComplainId(Long complainId) {
		this.complainId = complainId;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(int attachmentType) {
		this.attachmentType = attachmentType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getStrId() {
		return strId;
	}

	public void setStrId(String strId) {
		this.strId = strId;
	}
}
