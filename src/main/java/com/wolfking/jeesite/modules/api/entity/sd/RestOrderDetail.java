package com.wolfking.jeesite.modules.api.entity.sd;

import com.wolfking.jeesite.modules.md.entity.Engineer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 订单实际服务记录
 * 用于APP显示
 */
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class RestOrderDetail
{

	private String id = "";
	private String orderId = "";//订单id
	private String quarter="";//分片
	private Integer serviceTimes = 1;
	private String productName = "";
	private String productId = "";
	private String serviceTypeName = "";
	private Long servicePointId; //网点id
	private Long engineerId; //安维id
	private Engineer engineer;//安维
	private Integer qty = 1;//数量
	private String unit;//单位
	//费用
	private Double engineerServiceCharge = 0.00;// 安维服务费(应付)
	private Double engineerTravelCharge = 0.00;// 安维远程费(应付)
	private String travelNo = "";// 远程费审核单号
	private Double engineerExpressCharge = 0.00;// 快递费（应付）
	private Double engineerMaterialCharge = 0.00;// 安维配件费(应付)
	private Double engineerOtherCharge = 0.00;// 安维其它费用(应付)
	//安维应收= engineerServiceCharge+engineerTravelCharge+engineerExpressCharge+engineerMaterialCharge+engineerOtherCharge
	private Double engineerChage = 0.00;
	private String remarks = ""; //备注

	//region 故障维修
	// 服务类型
	private Long serviceCategoryId;
	private String serviceCategoryName;
	// 故障分类
	private String errorTypeName;
	// 故障现象
	private String errorCodeName;
	// 故障处理
	private String actionCodeName;
	// 其他故障维修说明
	private String otherActionRemark;
	// 是否添加了维修故障信息 1-已添加
	private int hasRepaired;
	//endregion
}