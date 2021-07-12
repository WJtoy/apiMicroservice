package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.utils.MaterialCategoryAdapter;
import lombok.Data;

/**
 * 配件类别
 * Created on 2019-06-01.
 */
@Data
@JsonAdapter(MaterialCategoryAdapter.class)
public class MaterialCategory extends LongIDDataEntity<MaterialCategory> {
     private String name = "";

     public MaterialCategory(){
          super();
     }

     public MaterialCategory(long id){
          this.id = id;
     }
}
