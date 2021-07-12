package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.utils.BrandSimpleAdapter;
import lombok.Data;

@Data
@JsonAdapter(BrandSimpleAdapter.class)
public class Brand  extends LongIDDataEntity<Brand> {
    private String code;
    private String name;
    private Integer sort = 10;
    public Brand(){
    }
    public Brand(Long id){
        this.id=id;
    }
}
