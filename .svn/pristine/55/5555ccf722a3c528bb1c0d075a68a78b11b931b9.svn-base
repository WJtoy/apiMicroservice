package com.wolfking.jeesite.modules.sd.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 工单保险单
 * @author Ryan
 * @date 2018-01-27
 */
@NoArgsConstructor
@Data
public class OrderInsurance extends LongIDDataEntity<OrderInsurance>
{
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "数据库分片不能为空")
	private String quarter="";
	private Long orderId;
	@NotNull(message = "工单号不能为空")
	private String orderNo="";
	private String insuranceNo="";
	private Long servicePointId;
	private String assured = "";//被保险人
	private String phone = "";
	private String address = "";
	private Date insureDate;//投保日期
	private Integer insuranceDuration=0;//投保期限（月）
	private double amount;//保险费
}