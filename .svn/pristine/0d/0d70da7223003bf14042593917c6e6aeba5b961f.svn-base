package com.wolfking.jeesite.modules.md.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 评价标准 数据模型
 * @author Ryan Lu
 * @version 2014-09-24
 */
public class GradeItem extends LongIDDataEntity<GradeItem> {

	private static final long serialVersionUID = 1L;

	//@GsonIgnore
	private Grade grade;//评价项目
	private Integer point = 0;//分值

	@GsonIgnore
	private String type="item";
	@GsonIgnore
	private boolean isSelected=false;
	//字典对应的value
	private String dictValue = "";
	
	public GradeItem() {
	}
	
	public GradeItem(Long id) {
		this();
		this.id = id;
	}

	@Min(value=1,message="分值必须大于0")
	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}


	@JsonIgnore
	@NotNull(message="评价项目不能为空")
	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}
}