package com.wolfking.jeesite.modules.sd.entity;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * 工单完成上传图片
 * @date 2018-08-16
 */
@Slf4j
@Data
public class OrderItemComplete extends LongIDDataEntity<OrderItemComplete>{

	/**
	 * 订单id
	 */
	private Long orderId;

	/**
	 * 产品
	 */
	private Product product;

	/**
	 * 产品项次
	 */
	private Integer itemNo;

	/**
	 * 图片路径
	 */
	private String picJson;

	/**
	 * 已上传图片数量
	 */
	private Integer uploadQty = 0;

	/**
	 * 必要上传图片检查标记
	 */
	private Integer completeFlag = 0;

	/**
	 *整机条码
	 */
	private String unitBarcode;

	/**
	 *外机条码
	 */
	private String outBarcode;

	/**
	 *分片
	 */
	private String quarter;

	/**
	 *辅助字段
	 */
	private List<ProductCompletePicItem> itemList = Lists.newArrayList();

}