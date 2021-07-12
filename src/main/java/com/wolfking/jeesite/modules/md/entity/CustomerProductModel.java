package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.utils.CustomerProductModelSimpleAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@JsonAdapter(CustomerProductModelSimpleAdapter.class)
public class CustomerProductModel extends LongIDDataEntity<CustomerProductModel> {

    @GsonIgnore
    private Customer customer;

    @GsonIgnore
    private Product product;

    private String customerModel = "";

    private String customerModelId = "";

    private String customerProductName = "";

    /**
     * 辅助字段用于缓存保存产品Id
     * */
    private Long productId = 0L;

    public CustomerProductModel(Long id) {
        super(id);
    }

}
