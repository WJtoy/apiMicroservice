package com.wolfking.jeesite.modules.sd.entity;

import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import lombok.*;

import java.io.Serializable;

/**
 *
 * 配件申产品信息表
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MaterialProduct implements Serializable
{
	private static final long serialVersionUID = 1566375681290L;

	//主键
	private Long id;
	//数据分片
	private String quarter;
	//配件单主表id
	private Long materialMasterId = 0L;
	//产品
	private Product product;
	//产品项次
	private int  itemNo;
	//品牌
	private String brand;
	//规格
	private String productSpec;
	//服务类型
	private ServiceType serviceType;
	//质保类型
	private String warrantyType;
}
