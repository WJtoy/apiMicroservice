/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.md.entity;


import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.kkl.kklplus.utils.StringUtils;

/**
 * 产品Entity
 * 
 * @author ryan
 * @version 2015-03-10
 */
public class MdAttachment extends LongIDDataEntity<MdAttachment>
{

	private static final long serialVersionUID = 1L;

	private String filePath;

	private int count;                  //为saveorupdate数据操作

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public MdAttachment()
	{
		super();
	}

	public MdAttachment(Long id)
	{
		super(id);
	}

	public MdAttachment(String filePath)
	{

		super();
		this.filePath = filePath;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String file_path)
	{
		this.filePath = file_path;
	}

	public Boolean canAdd(){
		return StringUtils.isNoneBlank(this.filePath) && (this.id == null || this.id==0);
	}

}
