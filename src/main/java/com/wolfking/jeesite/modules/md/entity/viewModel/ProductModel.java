package com.wolfking.jeesite.modules.md.entity.viewModel;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.utils.ProductSimpleAdapter;
import lombok.Data;

@Data
public class ProductModel {
    @JsonAdapter(ProductSimpleAdapter.class)
    private Product product;
    private String models;
}
