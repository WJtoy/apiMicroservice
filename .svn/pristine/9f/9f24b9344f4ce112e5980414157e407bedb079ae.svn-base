package com.wolfking.jeesite.modules.api.entity.sd;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class RestProductAuxiliaryMaterials {

    @Getter
    @Setter
    private List<Product> products = Lists.newArrayList();

    public static class Product {
        @Getter
        @Setter
        private Long productId = 0L;

        @Getter
        @Setter
        private String productName = "";

        @Getter
        @Setter
        private List<Category> categories = Lists.newArrayList();
    }

    public static class Category {
        @Getter
        @Setter
        private Long categoryId = 0L;

        @Getter
        @Setter
        private String categoryName = "";

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
        private String itemName = "";

        @Getter
        @Setter
        private Long categoryId = 0L;

        @Getter
        @Setter
        private Long productId = 0L;

        @Getter
        @Setter
        private Double charge = 0.0;

        @Getter
        @Setter
        private Integer customPriceFlag = 0;

        @Getter
        @Setter
        private String unit = "";
    }
}