package com.wolfking.jeesite.modules.api.entity.md;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.api.entity.md.adapter.RestDictAdapter;

/**
 * 字典基础类
 */
@JsonAdapter(RestDictAdapter.class)
public class RestDict {
    private String Label;
    private String value;

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
