package com.wolfking.jeesite.modules.md.utils;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务类型（简单）自定义Gson序列化/序列化
 */
public class ServiceTypeListAdapter extends TypeAdapter<List<ServiceType>> {

    @Override
    public List<ServiceType> read(final JsonReader in) throws IOException {
        final List<ServiceType> list = Lists.newArrayList();
        in.beginArray();
        while (in.hasNext()) {
            list.add(ServiceTypeSimpleAdapter.getInstance().read(in));//调用ServiceType的序列化类
        }
        in.endArray();
        return list;
    }

    @Override
    public void write(final JsonWriter out, final List<ServiceType> serviceTypes) throws IOException {
        out.beginArray();
        for (final ServiceType item : serviceTypes) {
            ServiceTypeSimpleAdapter.getInstance().write(out, item);
        }
        out.endArray();
    }

    private static ServiceTypeListAdapter adapter;
    public ServiceTypeListAdapter() {}
    public static ServiceTypeListAdapter getInstance() {
        if (adapter == null){
            adapter = new ServiceTypeListAdapter();
        }
        return adapter;
    }
}
