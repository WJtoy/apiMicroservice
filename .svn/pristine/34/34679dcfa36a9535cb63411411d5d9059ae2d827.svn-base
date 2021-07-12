package com.wolfking.jeesite.modules.sd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Material;
import com.wolfking.jeesite.modules.md.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 返件单明细表
 *
 * @author Ryan
 * @date 2019-06-28
 */
@Data
@NoArgsConstructor
public class MaterialReturnItem extends LongIDDataEntity<MaterialReturnItem>
{
	private static final long serialVersionUID = 1L;
	//数据库分片，与订单相同
	private String quarter = "";
	//单头id
	private Long formId = 0l;
	//配件
	private Material material;
	//数量
	private int qty = 1 ;
	//单价
	private Double price = 0.00;
	//小计
	private Double totalPrice = 0.00;
	//region 新增字段 2019/05/31
	// 产品
	private Product product;
}
