package com.wolfking.jeesite.modules.sd.entity;


import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDBaseEntity;
import com.wolfking.jeesite.modules.sd.utils.WebOrderProcessLogAdapter;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.VisibilityFlagEnum;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 订单处理日志
 * @author Ryan Lu
 * @version 2014-09-24
 * 
 * 特别说明:
 * 	actionComment 是客服可见的类容
 *	remark 是同步对客户可见的类容
 *	close_flag  安维添加的跟踪进度则用close_flag = 2 来区分
 *	安维只能看到close_flag = 2 的actionComment类容
 */
@JsonAdapter(WebOrderProcessLogAdapter.class)
public class OrderProcessLog extends LongIDBaseEntity<OrderProcessLog> {

	private static final long serialVersionUID = 1L;

	public static final Integer OPL_SF_NOT_CHANGE_STATUS = 0;//未更改订单状态
	public static final Integer OPL_SF_CHANGED_STATUS = 1;//更改了订单状态
	public static final Integer OPL_SF_PENDDING = 2;//未更改订单状态,但是设置了pendding_flag为异常
	public static final Integer OPL_SF_CHARGE = 3;//未更改订单状态,但是生成了对帐单
	public static final Integer OPL_SF_TRACKING = 4;//进度跟踪
	public static final Integer OPL_SF_COMPLAIN = 5;//用户投诉
	public static final Integer OPL_SF_PENDINGED = 6;//订单异常处理
	public static final Integer OPL_SF_INVOICE = 7;//未更改订单状态,更新对帐时间

	
	// Fields
	@GsonIgnore
//	private Order order;
	private Long orderId;
	private String quarter = "";//数据库分片，与订单相同
	private String action = "";
	private String actionComment="";
	private Integer statusFlag = 0;//状态标识
	private String status = "";//状态
	private Integer statusValue = 0;//状态值
	private Integer closeFlag=0;//关闭标识
	private Integer visibilityFlag = VisibilityFlagEnum.NONE.getValue(); //可见性标识
	private Integer dataSourceId = 0; //工单的数据源ID，某个B2B厂商需要获取工单日志
	@GsonIgnore
	private Long customerId = 0L; //工单的客户ID，某个客户需要获取工单日志

	public OrderProcessLog() {
		super();
	}

	public OrderProcessLog(Long id) {
		super();
		this.id = id;
	}

	/**
	 * 插入之前执行方法，需要手动调用
	 */
	@Override
	public void preInsert(){
//		User user = UserUtils.getUser();
//		if (user.getId()!=null) {
//			this.createBy = user;
//		}
	}

	/**
	 * 更新之前执行方法，需要手动调用
	 */
	@Override
	public void preUpdate(){}

//	@JsonIgnore
//	@NotNull(message="订单不能为空")
//	public Order getOrder() {
//		return order;
//	}
//
//	public void setOrder(Order order) {
//		this.order = order;
//	}

	@NotNull(message="处理方式不能为空")
	@Length(min=1,max = 100,message = "处理方式长度在1~10个汉字之间")
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}


	@NotNull(message="处理内容不能为空")
	@Length(min=1,max = 512,message = "处理内容长度在1~256个汉字之间")
	public String getActionComment() {
		return actionComment;
	}

	public void setActionComment(String actionComment) {
		this.actionComment = actionComment;
	}

	@Range(min = 0,max = 7,message = "状态标识超出范围")
	public Integer getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}

	@NotNull(message="订单状态不能为空")
	@Length(min=1,max = 20,message = "订单状态长度在1~10个汉字之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Range(min = 0,max = 1,message = "关闭标识超出范围")
	public Integer getCloseFlag() {
		return closeFlag;
	}

	public void setCloseFlag(Integer closeFlag) {
		this.closeFlag = closeFlag;
	}

	public Integer getVisibilityFlag() {
		return visibilityFlag;
	}

	public void setVisibilityFlag(Integer visibilityFlag) {
		this.visibilityFlag = visibilityFlag;
	}

	@NotNull(message="订单不能为空")
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(Integer statusValue) {
		this.statusValue = statusValue;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public Integer getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(Integer dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
}