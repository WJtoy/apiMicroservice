package com.wolfking.jeesite.common.config.redis;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Gson序列化为json时忽略属性注解类
 * ryan
 * 2017/4/13.
 */
public class GsonIgnoreStrategy implements ExclusionStrategy {
    /*
    * (non-Javadoc)
    *
    * @see com.google.gson.ExclusionStrategy#shouldSkipClass(java.lang.Class)
    */
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.google.gson.ExclusionStrategy#shouldSkipField(com.google.gson.
     * FieldAttributes)
     */
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        Collection<Annotation> annotations = fieldAttributes.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == GsonIgnore.class) {
                return true;
            }
        }
        return false;
    }
}
