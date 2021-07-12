package com.wolfking.jeesite.modules.sd.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.mapper.adapters.DateAdapter;
import com.wolfking.jeesite.common.mapper.adapters.DateTimeAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.utils.CustomerSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.VisibilityFlagEnum;
import com.wolfking.jeesite.modules.sys.entity.adapter.DictSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.adapter.UserSimpleAdapter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 投诉单
 * @author Ryan
 * @date 2018-01-27
 */
public class OrderComplainLog extends LongIDDataEntity<OrderComplainLog>
{

	private static final long serialVersionUID = 1L;

	public static int VISIBILITY_FLAG_ALL = VisibilityFlagEnum.or(Sets.newHashSet(VisibilityFlagEnum.KEFU, VisibilityFlagEnum.CUSTOMER));


	private String quarter = "";//数据库分片，与工单相同
	private Long complainId;
	private Dict status;
	private String content = "";
	private Integer visibilityFlag = VisibilityFlagEnum.NONE.getValue(); //可见性标识

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public Long getComplainId() {
		return complainId;
	}

	public void setComplainId(Long complainId) {
		this.complainId = complainId;
	}

	public Dict getStatus() {
		return status;
	}

	public void setStatus(Dict status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getVisibilityFlag() {
		return visibilityFlag;
	}

	public void setVisibilityFlag(Integer visibilityFlag) {
		this.visibilityFlag = visibilityFlag;
	}
}