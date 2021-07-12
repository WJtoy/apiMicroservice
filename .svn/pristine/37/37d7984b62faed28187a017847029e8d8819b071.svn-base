package com.wolfking.jeesite.common.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.adapter.UserSimpleAdapter;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.wolfking.jeesite.common.config.Global;
import org.hibernate.validator.constraints.Length;
//import com.wolfking.jeesite.common.supcan.annotation.treelist.SupTreeList;
//import com.wolfking.jeesite.common.supcan.annotation.treelist.cols.SupCol;

/**
 * NewEntity支持类
 * @author ThinkGem
 * @version 2014-05-16
 */
//@SupTreeList
public abstract class LongIDBaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 实体编号（唯一标识）
	 */
	protected Long id;

//	@GsonIgnore
	protected String remarks = "";	// 备注

	@JsonAdapter(UserSimpleAdapter.class)
	protected User createBy;	// 创建者

	protected Date createDate;	// 创建日期

	/**
	 * 当前用户
	 */
	@GsonIgnore
	protected User currentUser;
	
	/**
	 * 当前实体分页对象
	 */
	@GsonIgnore
	protected Page<T> page;
	
	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	@GsonIgnore
	protected Map<String, String> sqlMap;
	
	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 */
	@GsonIgnore
	protected boolean isNewRecord = false;

	public LongIDBaseEntity() {
		
	}
	
	public LongIDBaseEntity(Long id) {
		this();
		this.id = id;
	}

	//@SupCol(isUnique="true", isHide="true")
	public Long getId() {  //zhoucy: public long getId() {
		return id;
	}

	public void setId(Long id) {  //zhoucy: public void setId(long id) {
		this.id = id;
	}

	@Length(min=0, max=255)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@JsonIgnore
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	@XmlTransient
	public User getCurrentUser() {
//		if(currentUser == null){
//			currentUser = UserUtils.getUser();
//		}
		return currentUser;
	}
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	@JsonIgnore
	@XmlTransient
	public Page<T> getPage() {
		if (page == null){
			page = new Page<T>();
		}
		return page;
	}
	
	public Page<T> setPage(Page<T> page) {
		this.page = page;
		return page;
	}

	@JsonIgnore
	@XmlTransient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null){
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
	
	/**
	 * 插入之前执行方法，子类实现
	 */
	public abstract void preInsert();
	
	/**
	 * 更新之前执行方法，子类实现
	 */
	public abstract void preUpdate();
	
    /**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     * @return
     */
	public boolean getIsNewRecord() {   
		return isNewRecord || getId()==null;
    }

	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 */
	public void setIsNewRecord(boolean isNewRecord) {
		this.isNewRecord = isNewRecord;
	}

	/**
	 * 全局变量对象
	 */
	@JsonIgnore
	public Global getGlobal() {
		return Global.getInstance();
	}

	/**
	 * 获取数据库名称,该方法至关重要,在所有的mapper里面都是用
	 */
	@JsonIgnore
	public String getDbName() {
		return Global.getJdbcType();
	}
	
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
		LongIDBaseEntity<?> that = (LongIDBaseEntity<?>) obj;
        if(null == this.getId() || 0l == this.getId().longValue()){
        	return false;
		}else{
        	return this.getId().equals(that.getId());
		}
        //return null == this.getId() || 0l == this.getId().longValue() ? false : this.getId().equals(that.getId());
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    

}
