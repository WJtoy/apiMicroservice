package com.wolfking.jeesite.modules.sys.entity.adapter;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sys.entity.Menu;

import java.io.IOException;
import java.util.List;

/**
 * 服务类型（简单）自定义Gson序列化/序列化
 */
public class MenuListAdapter extends TypeAdapter<List<Menu>> {

    @Override
    public List<Menu> read(final JsonReader in) throws IOException {
        final List<Menu> list = Lists.newArrayList();
        in.beginArray();
        while (in.hasNext()) {
            list.add(MenuAdapter.getInstance().read(in));//调用ServiceType的序列化类
        }
        in.endArray();
        return list;
    }

    @Override
    public void write(final JsonWriter out, final List<Menu> menus) throws IOException {
        out.beginArray();
        for (final Menu item : menus) {
            MenuAdapter.getInstance().write(out, item);
        }
        out.endArray();
    }

    private static MenuListAdapter adapter;
    public MenuListAdapter() {}
    public static MenuListAdapter getInstance() {
        if (adapter == null){
            adapter = new MenuListAdapter();
        }
        return adapter;
    }
}
