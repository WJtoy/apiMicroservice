package com.wolfking.jeesite.modules.sd.entity.viewModel;


import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sys.entity.Area;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author Ryan Lu
 * @version 1.0.0
 * 历史派单记录视图模型
 * @date 2019-10-30 09:06
 */
@Data
@NoArgsConstructor
public class HistoryPlanOrderModel implements Serializable {

	private static final long serialVersionUID = 1572340528099L;

	private String quarter = "";
	private long orderId;
	private String orderNo="";

	private Date createDate;
	private long servicePointId;
	private String servicePointName;

	private String userName;
	private String userPhone;
	private String userAddress; //详细地址
	private Area area;// 区县
	private Area subArea;// 4级街道

	private long productCategoryId;
	private String productCategoryName;

	private int statusValue;
	private String statusName;

	//费用
    private String chargeText;

    //订单产品信息
    //private String itemJson;
	private byte[] itemsPb;
    private List<OrderItem> items;
    //实际上门明细
	private List<HistoryPlanOrderServiceItem> serviceItems;

}