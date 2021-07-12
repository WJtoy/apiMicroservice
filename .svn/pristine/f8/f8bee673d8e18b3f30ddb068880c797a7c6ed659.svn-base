package com.wolfking.jeesite.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.redis.GsonIgnoreStrategy;
import com.wolfking.jeesite.common.mapper.adapters.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;


@Slf4j
public class GsonRedisUtils {

    private static final Gson gson = new GsonBuilder()
            .serializeNulls()//序列化null
            .registerTypeAdapter(String.class, new StringConverter())//null <-> String
            .disableHtmlEscaping()//禁止转义html标签
            .addSerializationExclusionStrategy(new GsonIgnoreStrategy())
            .create();


    /**
     * Object可以是POJO，也可以是Collection或数组。
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    public static String toJson(Object object) {
        String json;
        try {
            json = gson.toJson(object);
        } catch (Exception e) {
            json = null;
        }
        return json;
    }


    /**
     * JSON转对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        T obj;
        try {
            obj = gson.fromJson(json, clazz);
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    /**
     * 字节数组转对象
     */
    public static <T> T fromBytes(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String json = new String(bytes, StandardCharsets.UTF_8);
        return fromJson(json, clazz);
    }


    public static byte[] toBytes(Object obj) throws SerializationException {
        if (obj == null) {
            return new byte[0];
        }
        String json = toJson(obj);
        if (StringUtils.isBlank(json)) {
            return new byte[0];
        }
        return json.getBytes(StandardCharsets.UTF_8);
    }
}
