package com.wolfking.jeesite.modules.mq.entity;

import lombok.Data;


/**
 * 每日下单,退单,取消单,客评发送消息队列实体类
 */
@Data
public class RPTOrderProcessModel {

    /**
     * 工单处理类型
      */
    private Integer processType = 0;

    /**
     * 工单id
     */
    private Long orderId = 0L;

    /**
     * 工单所在分片
     */
    private String quarter = "";

    /**
     * 省id
     */
    private Long provinceId = 0L;

    /**
     * 市id
     */
    private Long cityId = 0L;

    /**
     * 区id
     */
    private Long countId = 0L;

    /**
     * 品类id
     */
    private Long productCategoryId = 0L;

    /**
     * 客户id
     */
    private Long customerId = 0L;

    /**
     * 客服id
     */
    private Long keFuId = 0L;

    /**
     * 数据源id
     */
    private Integer dataSource = 0;

    /**
     * 下单时间
     */
    private Long orderCreateDate = 0L;

    /**
     * 工单客评/取消/退单时间
     */
    private Long orderCloseDate = 0L;

    /**
     * 工单状态
     */
    private Integer orderStatus = 0;

    /**
     *  工单类型(1:安装单,2:维修单)
     */

    private Integer orderServiceType = 0;


    /**
     * 网点id
     */
    private Long servicePointId = 0L;







}
