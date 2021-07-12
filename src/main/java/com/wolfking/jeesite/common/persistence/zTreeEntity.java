/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.common.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wolfking.jeesite.common.utils.Reflections;
import com.kkl.kklplus.utils.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * zTree数据Entity类
 */
public class zTreeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long pId;
	private String name;
	private Integer type;

	public zTreeEntity() {}

	public zTreeEntity(Long id,Long pId,String name) {
		this.id = id;
		this.pId = pId;
		this.name = name;
	}

	public zTreeEntity(Long id,Long pId,String name,Integer type) {
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.type = type;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		zTreeEntity test = (zTreeEntity) obj;
		return id == test.id;
	}

	 public int hashCode() {
		 return id.hashCode();
	 }
}
