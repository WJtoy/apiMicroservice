package com.wolfking.jeesite.modules.api.entity.sd;

import com.wolfking.jeesite.modules.api.entity.common.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 添加/修改订单实际服务维修内容
 */
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class RestRepairInfo
{
	//上门服务id
	private String id = "";
	//订单id
	private String orderId = "";
	//分片
	private String quarter="";
	//产品
	private String productId = "";
	private String productName = "";
	//服务项目
	private String serviceTypeId;
	private String serviceTypeName = "";
	//备注
	private String remarks = "";

	//region 故障维修
	// 服务类型<id,name>
	private Pair<Integer,String> serviceCategory;
	// 故障分类<id,name>
	private Pair<String,String> errorType;
	// 故障现象<id,name>
	private Pair<String,String> errorCode;
	// 故障分析&处理<id,name>
	private Pair<String,String> actionCode;
	// 其他故障说明
	private String otherActionRemark;
	// 是否有故障分类
	private int hasErrorType;
	//endregion

	//region 辅助属性

	// 服务项目
	List<Pair<Long,String>> serviceTypes;

	//endregion


}