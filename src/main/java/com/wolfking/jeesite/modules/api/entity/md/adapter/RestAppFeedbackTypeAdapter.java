package com.wolfking.jeesite.modules.api.entity.md.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.md.RestAppFeedbackType;
import org.springframework.util.CollectionUtils;

import java.io.IOException;

/**
 * App反馈
 * Gson序列化实现
 */
public class RestAppFeedbackTypeAdapter extends TypeAdapter<RestAppFeedbackType> {

    @Override
    public RestAppFeedbackType read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestAppFeedbackType feedbackType) throws IOException {
        out.beginObject()
                .name("id").value(feedbackType.getId())
                .name("feedbackType").value(feedbackType.getFeedbackType())
                .name("name").value(feedbackType.getName())
                .name("label").value(feedbackType.getLabel())
                .name("value").value(feedbackType.getValue())
                .name("hasChildren").value(feedbackType.getHasChildren());
        if (feedbackType.getParentId() > 0) {
            out.name("parentId").value(feedbackType.getParentId());
        }
        if (feedbackType.getHasChildren() == 1 && !CollectionUtils.isEmpty(feedbackType.getChildren())) {
            out.name("children")
                    .beginArray();
            for (final RestAppFeedbackType item : feedbackType.getChildren()) {
                RestAppFeedbackTypeAdapter.getInstance().write(out, item);
            }
            out.endArray();
        }
        out.endObject();
    }

    private static RestAppFeedbackTypeAdapter adapter;

    public RestAppFeedbackTypeAdapter() {
    }

    public static RestAppFeedbackTypeAdapter getInstance() {
        if (adapter == null) {
            adapter = new RestAppFeedbackTypeAdapter();
        }
        return adapter;
    }

}
