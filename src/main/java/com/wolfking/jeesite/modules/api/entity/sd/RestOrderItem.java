package com.wolfking.jeesite.modules.api.entity.sd;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RestOrderItem
{
	private String itemNo = "";
	private Long productId;
	private String productName = "";
	private String brand = "";
	private String productSpec = "";
	private Long serviceTypeId;
	private String serviceTypeName = "";
	private String warrantyStatus = "";//18/01/24
	private int qty = 1;
	private String unit = "台";
	private String remarks = "";
	private int hasFixSpec = 0;//是否有安装规范
	/**
	 * 产品图片
	 */
	@Getter
	@Setter
	private List<PicItem> pics = Lists.newArrayList();

	public static class PicItem {

		/**
		 * 图片的URL地址
		 */
		@Getter
		@Setter
		private String url;

		public PicItem() {}

		public PicItem(String url) {
			this.url = url;
		}
	}


	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProductSpec() {
		return productSpec;
	}

	public void setProductSpec(String productSpec) {
		this.productSpec = productSpec;
	}

	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getWarrantyStatus() {
		return warrantyStatus;
	}

	public void setWarrantyStatus(String warrantyStatus) {
		this.warrantyStatus = warrantyStatus;
	}

	public int getHasFixSpec() {
		return hasFixSpec;
	}

	public void setHasFixSpec(int hasFixSpec) {
		this.hasFixSpec = hasFixSpec;
	}
}
