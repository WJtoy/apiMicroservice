package com.wolfking.jeesite.modules.sd.entity;


import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.GradeItem;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单评分
 */

public class OrderGrade extends LongIDDataEntity<OrderGrade> {

	private static final long serialVersionUID = 1L;
	
	// Fields
	private Long orderId;
	private String quarter = "";//数据库分片，与订单相同
	private String orderNo="";//订单编号
	private ServicePoint servicePoint;//网点
	private Engineer engineer;//安维人员
	private Long gradeId = 0l;//评分项目id
	private String gradeName="";//评分项次
	private Long gradeItemId = 0l;//实际评分标准ID
	private String gradeItemName = "";//实际评分标准
	private Integer sort = 0;//排序
	private Integer point = 0;//分数
	private String dictType = "";

	//辅助用
	private List<GradeItem> items = Lists.newArrayList();
	//字典对应的value
	private String dictValue = "";

	// Constructors
	
	public OrderGrade() {
		super();
	}

	public OrderGrade(Long id) {
		super();
		this.id = id;
	}

	@NotNull(message="订单ID不能为空")
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Engineer getEngineer() {
		return engineer;
	}
	public void setEngineer(Engineer engineer) {
		this.engineer = engineer;
	}
	
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	/**
	 * @return 实际评分标准ID
	 */
	public Long getGradeItemId() {
		return gradeItemId;
	}
	public void setGradeItemId(Long gradeItemId) {
		this.gradeItemId = gradeItemId;
	}

	public String getGradeItemName() {
		return gradeItemName;
	}
	public void setGradeItemName(String gradeItemName) {
		this.gradeItemName = gradeItemName;
	}

	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}

	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}


	public Long getGradeId() {
		return gradeId;
	}

	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}

	public List<GradeItem> getItems() {
		return items;
	}

	public void setItems(List<GradeItem> items) {
		this.items = items;
	}

	public ServicePoint getServicePoint() {
		return servicePoint;
	}

	public void setServicePoint(ServicePoint servicePoint) {
		this.servicePoint = servicePoint;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}
}