package com.wolfking.jeesite.modules.api.entity.sd.request;

import com.google.common.collect.Lists;
import com.kkl.kklplus.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 保存配件申请单请求
 */
public class RestSaveMaterialApplicationRequest {

    /**
     * 工单ID
     */
    @Getter
    @Setter
    private Long orderId;

    /**
     * 数据库分片
     */
    @Getter
    @Setter
    private String quarter;

    /**
     * 申请类型：1:向师傅购买(自购) 2:厂家寄发
     */
    @Getter
    @Setter
    private Integer applyType = 2;

    /**
     * 收件人姓名
     */
    @Getter
    @Setter
    private String receiver = "";
    /**
     * 收件人电话
     */
    @Getter
    @Setter
    private String receiverPhone = "";
    /**
     * 地址类型标识
     */
    /**
     * 收件人类型：1 - 用户收件、2 - 师傅收件
     */
    @Getter
    @Setter
    private Integer receiverType = 0;
    /**
     * 收件地址信息
     */
    @Getter
    @Setter
    private String receiverAddress = "";
    /**
     * 区域ID
     */
    @Getter
    @Setter
    private Long receiverAreaId = 0L;
    /**
     * 详细地址
     */
    @Getter
    @Setter
    private String receiverSubAddress = "";

    /**
     * 备注
     */
    @Getter
    @Setter
    private String remarks;

    /**
     * 配件单中包含的产品
     */
    @Getter
    @Setter
    private List<Product> products = Lists.newArrayList();

    public int getMaterialQty() {
        int qty = 0;
        for (Product p : products) {
            qty = qty + p.items.size();
        }
        return qty;
    }

    public static class Product {

        /**
         * 产品ID
         */
        @Getter
        @Setter
        private Long productId;

        /**
         * 配件单中的产品下包含的配件
         */
        @Getter
        @Setter
        private List<Material> items = Lists.newArrayList();
    }

    public static class Material {

        /**
         * 配件ID
         */
        @Getter
        @Setter
        private Long materialId;

        /**
         * 配件数量
         */
        @Getter
        @Setter
        private Integer qty = 1;

        /**
         * 配件的单价
         */
        @Getter
        @Setter
        private Double price = 0.0;
    }

    public boolean hasReceiverAddress() {
        if (getReceiverAreaId() != null && getReceiverAreaId() > 0 && StringUtils.isNotBlank(getReceiverSubAddress())) {
            return true;
        }
        if (StringUtils.isNotBlank(getReceiverAddress())) {
            return true;
        }
        return false;
    }
}
