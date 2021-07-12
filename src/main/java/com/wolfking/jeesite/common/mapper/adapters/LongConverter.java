package com.wolfking.jeesite.common.mapper.adapters;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by yanshenglu on 2017/11/28.
 */
public class LongConverter implements JsonSerializer<Long>, JsonDeserializer<Long> {

    public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return new JsonPrimitive("0");
        } else {
            return new JsonPrimitive(src.toString());
        }
    }

    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Long.valueOf(json.getAsJsonPrimitive().getAsString());
    }
}
