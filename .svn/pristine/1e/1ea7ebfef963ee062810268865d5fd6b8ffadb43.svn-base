package com.wolfking.jeesite.modules.api.entity.sd.request;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RestSaveOrderAuxiliaryMaterialsRequest {

    @Getter
    @Setter
    private Long orderId = 0L;

    @Getter
    @Setter
    private String quarter = "";

    @Getter
    @Setter
    private Double actualTotalCharge = 0.0;

    @Getter
    @Setter
    private String remarks = "";

    @Getter
    @Setter
    private List<Product> products = Lists.newArrayList();

    public static class Product {
        @Getter
        @Setter
        private Long productId = 0L;

        @Getter
        @Setter
        private List<Item> items = Lists.newArrayList();
    }

    public static class Item {
        @Getter
        @Setter
        private Long itemId = 0L;

        @Getter
        @Setter
        private Integer qty = 0;

        @Getter
        @Setter
        private Double charge = 0.0;
    }
}
