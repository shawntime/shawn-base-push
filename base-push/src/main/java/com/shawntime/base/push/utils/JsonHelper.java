package com.shawntime.base.push.utils;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

public class JsonHelper {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private JsonHelper() {
        // Utility classes should always be final and have a private constructor
    }

    public static <T> String serialize(T object) {
        JSON.DEFFAULT_DATE_FORMAT = DEFAULT_DATE_FORMAT;
        return serialize(object, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static <T> String serialize(T object, SerializerFeature... features) {
        return JSON.toJSONString(object, features);
    }

    public static <T> String serializeExcludes(T object, String... fieldNames) {
        JSON.DEFFAULT_DATE_FORMAT = DEFAULT_DATE_FORMAT;
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().addAll(Arrays.asList(fieldNames));
        return JSON.toJSONString(object, filter);
    }

    public static <T> T deSerialize(String string, Class<T> clazz) {
        return JSON.parseObject(string, clazz);
    }

    public static <T> T deSerialize(String string, Type type) {
        return JSON.parseObject(string, type);
    }

    public static <T> List<T> deSerializeList(String string, Class<T> clazz) {
        return JSON.parseArray(string, clazz);
    }

}
