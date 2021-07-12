/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sys.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.sys.entity.adapter.SequenceAdapter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

/**
 * 单据编号规则 数据模型
 * @author Ryan Lu
 * @version 2014-09-24
 */
@JsonAdapter(SequenceAdapter.class)
public class Sequence extends LongIDDataEntity<Sequence> {

	private static final long serialVersionUID = 1L;
	private String code = ""; 		// 编号代码
	private String prefix = ""; 		// 前缀
	private String dateFormat = ""; 	// 日期格式
	private String dateSeparator = ""; 	// 日期分隔符
	private Integer digitBit = 1; 	// 顺序号位数
	private String suffix = ""; 		// 后缀
	private String separator = ""; 	// 单号分隔符
	private String previousDate =""; 	// 前一编号日期值
	private Integer previousDigit = 0; 	// 前一顺序号
	

	public Sequence(){
		super();
	}
	
	public Sequence(Long id){
		this();
		this.id = id;
	}
	
	@Length(min=1, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=0, max=5)
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@Length(min=0, max=1)
	public String getDateSeparator() {
		return dateSeparator;
	}

	public void setDateSeparator(String dateSeparator) {
		this.dateSeparator = dateSeparator;
	}
	
	@Max(value=10,message="顺序号位数最多10位")
	public Integer getDigitBit() {
		return digitBit;
	}

	public void setDigitBit(Integer digitBit) {
		this.digitBit = digitBit;
	}
	
	@Length(min=0, max=5)
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getPreviousDate() {
		return previousDate;
	}

	public void setPreviousDate(String previousDate) {
		this.previousDate = previousDate;
	}

	public Integer getPreviousDigit() {
		return previousDigit;
	}

	public void setPreviousDigit(Integer previousDigit) {
		this.previousDigit = previousDigit;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
}