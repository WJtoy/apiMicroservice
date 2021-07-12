package com.wolfking.jeesite.common.mapper.adapters;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by yanshenglu on 2017/5/18.
 */
public class StringConverter implements JsonSerializer<String>, JsonDeserializer<String> {

    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return new JsonPrimitive("");
        } else {
            return new JsonPrimitive(src.toString());
        }
    }

    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return json.getAsJsonPrimitive().getAsString();
    }
}
