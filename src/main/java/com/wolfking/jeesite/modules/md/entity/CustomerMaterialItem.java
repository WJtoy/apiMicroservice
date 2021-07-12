package com.wolfking.jeesite.modules.md.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerMaterialItem implements Serializable {

    private Long materialId = 0L;
    /**
     * (是否反件)
     * */
    private Integer isReturn = 0;

    private Double price = 0.00;

    private String remarks = "";

}
