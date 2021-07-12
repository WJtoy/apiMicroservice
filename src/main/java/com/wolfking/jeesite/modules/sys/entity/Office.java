/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sys.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDTreeEntity;
import com.wolfking.jeesite.modules.md.utils.AreaAdapter;
import com.wolfking.jeesite.modules.md.utils.AreaSimpleAdapter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 机构Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Office extends LongIDTreeEntity<Office> {

	private static final long serialVersionUID = 1L;
	@JsonAdapter(AreaAdapter.class)
	private Area area;		// 归属区域
	private String code = ""; 	// 机构编码
	private Integer type = 2; 	// 机构类型（1：公司；2：部门；3：小组）
	private Integer grade = 1; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	@GsonIgnore
	private String address = ""; // 联系地址
	@GsonIgnore
	private String zipCode = ""; // 邮政编码
	@GsonIgnore
	private String master = ""; 	// 负责人
	@GsonIgnore
	private String phone = ""; 	// 电话
	@GsonIgnore
	private String fax =""; 	// 传真
	@GsonIgnore
	private String email=""; 	// 邮箱
	@GsonIgnore
	private String useable = "";//是否可用
//	private User primaryPerson;//主负责人
//	private User deputyPerson;//副负责人
	@GsonIgnore
	private List<String> childDeptList;//快速添加子部门
	
	public Office(){
		super();
		//this.type = 2;  //mark on 2017-4-13
	}

	public Office(Long id){
		super(id);
	}

	public Office(Long id,String name){
		super(id);
		this.name = name;
	}

	public List<String> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<String> childDeptList) {
		this.childDeptList = childDeptList;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

//	public User getPrimaryPerson() {
//		return primaryPerson;
//	}
//
//	public void setPrimaryPerson(User primaryPerson) {
//		this.primaryPerson = primaryPerson;
//	}
//
//	public User getDeputyPerson() {
//		return deputyPerson;
//	}
//
//	public void setDeputyPerson(User deputyPerson) {
//		this.deputyPerson = deputyPerson;
//	}

//	@JsonBackReference
//	@NotNull
	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}


	@NotNull
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@Range(min=1, max=3)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Range(min=1, max=10)
	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=10)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(min=0, max=20)
	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Length(min=0, max=20)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=20)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Length(min=0, max=100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=20)
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
}