package com.asiainfo.orderdishes.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.List;

public class ConvertToJson {
    public static String toJsonWithGson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static String toJsonWithGson(Object obj, Type type) {
        Gson gson = new Gson();
        return gson.toJson(obj, type);
    }

    @SuppressWarnings("unchecked")
    public static String toJsonWithGson(List list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @SuppressWarnings("unchecked")
    public static String toJsonWithGson(List list, Type type) {
        Gson gson = new Gson();
        return gson.toJson(list, type);
    }

    public static String toJsonWithGsonBuilder(Object obj) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy()).serializeNulls().create();
        return gson.toJson(obj);
    }

    public static String toJsonWithGsonBuilder(Object obj, Type type) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy()).serializeNulls().create();
        return gson.toJson(obj, type);
    }

    @SuppressWarnings("unchecked")
    public static String toJsonWithGsonBuilder(List list) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy()).serializeNulls().create();
        return gson.toJson(list);
    }

    @SuppressWarnings("unchecked")
    public static String toJsonWithGsonBuilder(List list, Type type) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy()).serializeNulls().create();
        return gson.toJson(list, type);
    }
}
