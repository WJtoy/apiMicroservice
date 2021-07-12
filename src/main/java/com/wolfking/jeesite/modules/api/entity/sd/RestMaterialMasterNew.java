package com.wolfking.jeesite.modules.api.entity.sd;


import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 配件申请单(for list)
 */
public class RestMaterialMasterNew {

    /**
     * 配件申请单id
     */
    @Getter
    @Setter
    private String id;
    /**
     * 数据库分片，与订单相同
     */
    @Getter
    @Setter
    private String quarter = "";
    @Getter
    @Setter
    private String orderId;
    @Getter
    @Setter
    private String orderDetailId;
//    /**
//     * 配件类型：1:配件 2:返件
//     */
//    @Getter
//    @Setter
//    private String materialType;
//    @Getter
//    @Setter
//    private String materialTypeValue;
    /**
     * 类型：1:向师傅购买(自购) 2:厂家寄发
     */
    @Getter
    @Setter
    private String applyType;
    @Getter
    @Setter
    private String applyTypeValue;
    @Getter
    @Setter
    private String status;
    @Getter
    @Setter
    private String statusName;
    @Getter
    @Setter
    private String expressCompany;
    @Getter
    @Setter
    private String expressNo;
    @Getter
    @Setter
    private String remarks;
    //    @Getter
//    @Setter
//    private String details;
    @Getter
    @Setter
    private Double totalPrice = 0.0;
    @Getter
    @Setter
    private Long createDate;
    /**
     * 有无返件标识 1:有返件
     */
    @Getter
    @Setter
    private Integer returnFlag;

    /**
     * 有无返件标识 1:有返件
     */
    @Getter
    @Setter
    private Integer recycleFlag;

    /**
     * 收件人姓名
     */
    @Getter
    @Setter
    private String receiver = "";
    /**
     * 收件人手机
     */
    @Getter
    @Setter
    private String receiverPhone = "";
    /**
     * 地址类型
     */
    @Getter
    @Setter
    private Integer receiverType = 0;
    /**
     * 收件地址
     */
    @Getter
    @Setter
    private String receiverAddress = "";

    /**
     * 图片
     */
    @Getter
    @Setter
    private List<Photo> photos = Lists.newArrayList();

    @Getter
    @Setter
    private List<Product> products = Lists.newArrayList();

    public static class Product {
        @Getter
        @Setter
        private Long productId;
        @Getter
        @Setter
        private String productName;
        @Getter
        @Setter
        private List<Material> items = Lists.newArrayList();
    }

    public static class Photo {
        @Getter
        @Setter
        private String photoId;
        @Getter
        @Setter
        private String filePath;
        @Getter
        @Setter
        private String remarks;
    }

    public static class Material {
        @Getter
        @Setter
        private String materialId;
        @Getter
        @Setter
        private String materialName;
        @Getter
        @Setter
        private Integer qty;
        @Getter
        @Setter
        private Double price;
        @Getter
        @Setter
        private Double totalPrice;
        @Getter
        @Setter
        private Integer recycleFlag;
        @Getter
        @Setter
        private Double recyclePrice;
        @Getter
        @Setter
        private Double totalRecyclePrice;
//        @Getter
//        @Setter
//        private String unit;
    }
}
