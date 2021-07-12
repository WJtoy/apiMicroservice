package com.wolfking.jeesite.modules.api.entity.md;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.api.entity.md.adapter.RestAppFeedbackTypeAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * App问题反馈
 */
@Accessors(chain = true)
@Getter
@Setter
@JsonAdapter(RestAppFeedbackTypeAdapter.class)
public class RestAppFeedbackType implements Serializable {
    private int id;
    private int parentId;
    private int feedbackType;
    private int value;
    private String name;
    private String label;
    private int hasChildren;
    private List<RestAppFeedbackType> children;
}
