package com.wolfking.jeesite.common.persistence;

import java.util.Date;

import com.wolfking.jeesite.common.config.redis.GsonIgnore;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;

public abstract class LongIDDataEntity<T> extends LongIDBaseEntity<T> {

	private static final long serialVersionUID = 1L;


	@GsonIgnore
	protected User updateBy;	// 更新者
	@GsonIgnore
	protected Date updateDate;	// 更新日期
//	@GsonIgnore
	protected Integer delFlag=0; 	// 删除标记（0：正常；1：删除；2：审核）
	
	public LongIDDataEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	public LongIDDataEntity(Long id) {  
		super(id);
	}
	
	/**
	 * 插入之前执行方法，需要手动调用
	 */
	@Override
	public void preInsert(){
		
//		User user = UserUtils.getUser();
//		if (user.getId()!=null) {
//			this.updateBy = user;
//			this.createBy = user;
//		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}
	
	/**
	 * 更新之前执行方法，需要手动调用
	 */
	@Override
	public void preUpdate(){
//		User user = UserUtils.getUser();
//		if (user.getId()!=null){
//			this.updateBy = user;
//		}
		this.updateDate = new Date();
	}
	


	@JsonIgnore
	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@JsonIgnore
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * 逻辑删除
	 */
	public static final Integer DEL_FLAG_NORMAL = 0;
	public static final Integer DEL_FLAG_DELETE = 1;
	public static final Integer DEL_FLAG_AUDIT = 2;
}
