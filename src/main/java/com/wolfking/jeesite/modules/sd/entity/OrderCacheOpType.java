package com.wolfking.jeesite.modules.sd.entity;

/**
 * 工单缓存的操作类型
 */
public enum OrderCacheOpType {
    /**
     * 更新、删除指定字段
     */
    UPDATE,
    /**
     * 删除整个工单
     */
    DELETE_ALL;
}
