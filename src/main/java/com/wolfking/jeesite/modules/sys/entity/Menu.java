/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sys.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.sys.entity.adapter.MenuAdapter;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Range;

/**
 * 菜单Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
@JsonAdapter(MenuAdapter.class)
public class Menu extends LongIDDataEntity<Menu> {

	private static final long serialVersionUID = 1L;

//	@JsonAdapter(MenuSimpleAdapter.class)
	private Menu parent;	// 父级菜单
//	@GsonIgnore
	private String parentIds = ""; // 所有父级编号
	private String name = ""; 	// 名称
	private String href = ""; 	// 链接
	private String target = ""; 	// 目标（ mainFrame、_blank、_self、_parent、_top）
	private String icon = ""; 	// 图标
	private Integer sort = 0; 	// 排序
	private Integer isShow = 1; 	// 是否在菜单中显示（1：显示；0：不显示）
	private String permission=""; // 权限标识
	
	private Long userId;

	//辅助字段
	private Long pId;
	
	public Menu(){
		super();
		this.sort = 30;
		this.isShow = 1;
	}
	
	public Menu(Long id){
		super(id);
	}

	@JsonBackReference
	@NotNull
	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	@Length(max=60)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(max=120)
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Length(max=20)
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	@Length(max=30)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@NotNull
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
//	@Length(min=1, max=1)
	@Range(min = 0,max = 1)
	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	@Length(max=60)
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Long getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : 0L;
	}

	@JsonIgnore
	public static void sortList(List<Menu> list, List<Menu> sourcelist, Long parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			Menu e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().longValue() == parentId.longValue()){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						Menu child = sourcelist.get(j);
						if (child.getParent()!=null && child.getParent().getId()!=null
								&& child.getParent().getId().longValue() == e.getId().longValue()){
							sortList(list, sourcelist, e.getId(), true);
							break;
						}
					}
				}
			}
		}
	}

	@JsonIgnore
	public static Long getRootId(){
		return 1L;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return name;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}
}