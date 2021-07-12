/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.common.persistence;

import java.io.Serializable;

/**
 * Ajax 调用返回实例模型
 * @author ThinkGem
 * @version 2013-01-15
 */

public class AjaxJsonEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean success;
	private String message;
	private Object data;
	private Boolean isLogin = true;
	
	public AjaxJsonEntity()
	{
		this.isLogin = true;
	}
	
	public AjaxJsonEntity(Boolean success)
	{
		this();
		this.success = success;
		this.isLogin = true;
	}

	public static AjaxJsonEntity fail(String message, Object data) {
		AjaxJsonEntity entity = new AjaxJsonEntity(false);
		entity.setMessage(message);
		entity.setData(data);
		return entity;
	}

	public static AjaxJsonEntity success(String message, Object data) {
		AjaxJsonEntity entity = new AjaxJsonEntity(true);
		entity.setMessage(message);
		entity.setData(data);
		return entity;
	}

	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Boolean getLogin() {
		return isLogin;
	}

	public void setLogin(Boolean login) {
		isLogin = login;
	}
}
