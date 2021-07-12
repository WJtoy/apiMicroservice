/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sys.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDTreeEntity;
import com.wolfking.jeesite.common.persistence.zTreeEntity;
import com.wolfking.jeesite.modules.md.utils.AreaAdapter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.util.Objects;

/**
 * 区域Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
@JsonAdapter(AreaAdapter.class)
public class Area extends LongIDTreeEntity<Area> {

	private static final long serialVersionUID = 1L;

	public static final Integer TYPE_VALUE_COUNTRY = 1; 	//国家
	public static final Integer TYPE_VALUE_PROVINCE = 2; 	//省份类型
	public static final Integer TYPE_VALUE_CITY = 3;		//地市类型
	public static final Integer TYPE_VALUE_COUNTY = 4;		//区县类型
	public static final Integer TYPE_VALUE_TOWN = 5;		//镇乡类型


	private String code = ""; 	// 区域编码
	private Integer type = 2; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县;5:街道，乡镇）
	@GsonIgnore
	private String typeName = ""; //区域类型名称，为切换微服务器新增
	private String fullName = ""; //区域完整名称,包含上级区域,中间用空格分隔
	
	public Area(){
		super();
		this.sort = 30;
	}

	public Area(Long id){
		super(id);
	}

	public Area(Long id,String name){
		super(id);
		this.name = name;
	}

	public Area(Long id,String name,Integer type){
		super(id);
		this.name = name;
		this.type = type;
	}

//	@JsonBackReference
//	@NotNull
	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	@Range(min=1, max=5)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Length(max=20)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Length(max=100)
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Area area = (Area) o;
		return Objects.equals(area.id,id);
		/*
		if (!id.equals(area.id)) return false;
		return name.equals(area.name);
		*/
	}

	@Override
	public int hashCode() {
		/*
		int result = id.hashCode();
		return result;
		*/
		//result = 31 * result + name.hashCode();

		int result = 7;
		result = 79 * result + Objects.hashCode(this.name);
		result = 79 * result + Objects.hashCode(this.code);
		result = 79 * result + Objects.hashCode(this.type);
		return result;
	}
}