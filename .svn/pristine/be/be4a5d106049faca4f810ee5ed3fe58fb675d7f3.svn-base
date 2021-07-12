package com.wolfking.jeesite.modules.md.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * 评价项目 数据模型
 * @author Ryan Lu
 * @version 2014-09-24
 */
public class Grade extends LongIDDataEntity<Grade> {

	private static final long serialVersionUID = 1L;
	
	// Fields
	private String name = "";//评价项目
	private Integer point = 0;//分值
	private Integer sort = 0;	// 排序
	//关联字典type
	private String dictType = "";
	@GsonIgnore
	private String type="grade";
	
	private List<GradeItem> itemList = Lists.newArrayList(); // 评价标准列表

	// Constructors
	public Grade() {
	}

	
	public Grade(Long id) {
		this();
		this.id = id;
	}


	@Length(min=1, max=30)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Min(value=1,message="分值必须大于0")
	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	@Min(value=0,message="排序值必须大于等于0")
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public List<GradeItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<GradeItem> itemList) {
		this.itemList = itemList;
	}

	/*
	 * 辅助属性，获得评价标准的Id列表
	 */
	@JsonIgnore
	public List<Long> getItemIdList() {
		List<Long> itemIdList = Lists.newArrayList();
		for (GradeItem item : itemList) {
			itemIdList.add(item.getId());
		}
		return itemIdList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}
}