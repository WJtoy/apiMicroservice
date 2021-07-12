package com.wolfking.jeesite.common.persistence;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wolfking.jeesite.common.utils.Reflections;
import com.kkl.kklplus.utils.StringUtils;

public abstract class LongIDTreeEntity <T> extends LongIDDataEntity<T> {

	private static final long serialVersionUID = 1L;

	protected T parent;	// 父级编号
	protected String parentIds = ""; // 所有父级编号
	protected String name = ""; 	// 机构名称
	protected Integer sort = 1;		// 排序
	
	public LongIDTreeEntity() {
		super();
		this.sort = 30;
	}
	
	public LongIDTreeEntity(Long id) {  //zhoucy: public NewTreeEntity(long id) {
		super(id);
	}
	
	/**
	 * 父对象，只能通过子类实现，父类实现mybatis无法读取
	 * @return
	 */
	@JsonBackReference
	@NotNull
	public abstract T getParent();

	/**
	 * 父对象，只能通过子类实现，父类实现mybatis无法读取
	 * @return
	 */
	public abstract void setParent(T parent);

	@Length(max=60)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Length(min=1, max=60)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public long getParentId() {
		long id = 0;
		if (parent != null){
			id = StringUtils.toLong(Reflections.getFieldValue(parent, "id"));
		}
		return id;
	}
	
}
