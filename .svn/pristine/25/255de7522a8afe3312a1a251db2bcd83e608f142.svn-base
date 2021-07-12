package com.wolfking.jeesite.modules.md.entity.viewModel;

import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ryan Lu
 * @version 1.0.0
 * md缓存的网点价格
 * @date 2020/3/6 4:49 下午
 */
@JsonAdapter(MDServicePointPriceVMAdapter.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MDServicePointPriceVM implements Serializable {
    private long id;
    private long serviceTypeId;
    private long productId;
    private double price;
    private double discountPrice;
    private int delFlag;
}
