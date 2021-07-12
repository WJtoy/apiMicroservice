package com.wolfking.jeesite.modules.api.entity.md;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.api.entity.md.adapter.RestRepairActionAdapter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Ryan Lu
 * @version 1.0.0
 * APP维修用故障处理
 * @date 2019/12/30 11:02 上午
 */
@JsonAdapter(RestRepairActionAdapter.class)
@Getter
@Setter
public class RestRepairAction implements Serializable {
    private String key;
    private String value;
    private Long serviceTypeId;
    private String serviceTypeName;
}
