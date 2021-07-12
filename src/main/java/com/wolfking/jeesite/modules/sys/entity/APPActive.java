package com.wolfking.jeesite.modules.sys.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;

/**
 * @author F1053038
 * 
 */
public class APPActive extends LongIDDataEntity<APPActive>
{

	private static final long serialVersionUID = 1L;

	/**
	 * 0: iOS, 1: Android, 2: WindowsPhone
	 */
	private Integer platform;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 版本号
	 */
	private String ver;


	public APPActive() {}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}
}