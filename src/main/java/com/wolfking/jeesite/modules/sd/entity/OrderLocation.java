package com.wolfking.jeesite.modules.sd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDBaseEntity;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sd.utils.OrderAdapter;
import com.wolfking.jeesite.modules.sd.utils.OrderLocationAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import com.wolfking.jeesite.ms.tmall.md.entity.B2bCustomerMap;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单地理信息表
 */
@Data
@NoArgsConstructor
@JsonAdapter(OrderLocationAdapter.class)
public class OrderLocation implements Serializable {

    private static final long serialVersionUID = 1556099758000L;

    public OrderLocation(long orderId,String quarter){
        this.orderId = orderId;
        this.quarter = quarter;
    }
    // Fields
    private Long orderId;
    private String quarter = "";//20171,用于按季度分片，目前采用分库
    private Area area;// 区县
    private double longitude; //经度
    private double latitude; //维度
    private double distance = 0.0;// 距离,单位:公里

}
