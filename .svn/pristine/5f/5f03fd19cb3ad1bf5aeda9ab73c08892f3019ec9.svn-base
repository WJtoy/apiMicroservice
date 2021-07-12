package com.wolfking.jeesite.modules.sd.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;

/**
 * 订单派单记录表
 * 
 */
public class OrderPlan extends LongIDDataEntity<OrderPlan>
{

	private static final long serialVersionUID = 1L;

	// Fields
	private long orderId;
	private String quarter = "";//数据库分片，与订单相同
	private ServicePoint servicePoint = null;// 网点
	private Engineer engineer = null;// 师傅
	private int planTimes = 0;//派单次序
	private int isMaster = 0;//主帐号标识
	private double estimatedServiceCost = 0.0;//预估服务费用
	private double estimatedDistance = 0.0;//预估上门距离，单位：公里
	private double estimatedTravelCost = 0.0;//预估远程费
	private double estimatedOtherCost = 0.0;//预估其它费用
	private int isComplained = 0;//投诉标识
	private int serviceFlag;//上门标记

	public OrderPlan() {
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public ServicePoint getServicePoint() {
		return servicePoint;
	}

	public void setServicePoint(ServicePoint servicePoint) {
		this.servicePoint = servicePoint;
	}

	public Engineer getEngineer() {
		return engineer;
	}

	public void setEngineer(Engineer engineer) {
		this.engineer = engineer;
	}

	public int getPlanTimes() {
		return planTimes;
	}

	public void setPlanTimes(int planTimes) {
		this.planTimes = planTimes;
	}

	public int getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(int isMaster) {
		this.isMaster = isMaster;
	}


	public double getEstimatedDistance() {
		return estimatedDistance;
	}

	public void setEstimatedDistance(double estimatedDistance) {
		this.estimatedDistance = estimatedDistance;
	}

	public int getIsComplained() {
		return isComplained;
	}

	public void setIsComplained(int isComplained) {
		this.isComplained = isComplained;
	}

	public double getEstimatedTravelCost() {
		return estimatedTravelCost;
	}

	public void setEstimatedTravelCost(double estimatedTravelCost) {
		this.estimatedTravelCost = estimatedTravelCost;
	}

    public double getEstimatedServiceCost() {
        return estimatedServiceCost;
    }

    public void setEstimatedServiceCost(double estimatedServiceCost) {
        this.estimatedServiceCost = estimatedServiceCost;
    }

	public double getEstimatedOtherCost() {
		return estimatedOtherCost;
	}

	public void setEstimatedOtherCost(double estimatedOtherCost) {
		this.estimatedOtherCost = estimatedOtherCost;
	}

	public int getServiceFlag() {
		return serviceFlag;
	}

	public void setServiceFlag(int serviceFlag) {
		this.serviceFlag = serviceFlag;
	}
}