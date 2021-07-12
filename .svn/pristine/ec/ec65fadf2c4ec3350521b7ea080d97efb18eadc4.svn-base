package com.wolfking.jeesite.modules.sys.entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sys.entity.Menu;

import java.io.IOException;

/**
 * 菜单自定义Gson序列化/序列化
 */
public class MenuAdapter extends TypeAdapter<Menu> {

    @Override
    public Menu read(final JsonReader in) throws IOException {
        final Menu model = new Menu();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextLong());
                    break;
                case "parentIds":
                    model.setParentIds(in.nextString());
                    break;
                case "parent":
                    Menu parent = new Menu();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                parent.setId(in.nextLong());
                                break;
                            case "name":
                                if(in.peek()== JsonToken.NULL) {
                                    in.nextNull();
                                    parent.setName("");
                                }else {
                                    parent.setName(in.nextString());
                                }
                                break;
                        }
                    }
                    model.setParent(parent);
                    in.endObject();
                    break;
                case "name":
                    model.setName(in.nextString());
                    break;
                case "href":
                    model.setHref(in.nextString());
                    break;
                case "target":
                    model.setTarget(in.nextString());
                    break;
                case "icon":
                    model.setIcon(in.nextString());
                    break;
                case "permission":
                    model.setPermission(in.nextString());
                    break;
                case "sort":
                    model.setSort(in.nextInt());
                    break;
                case "isShow":
                    model.setIsShow(in.nextInt());
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final Menu model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        out.name("parentIds").value(StringUtils.isBlank(model.getParentIds())?"":model.getParentIds());
        if(model.getParent() != null && model.getParent().getId() != null){
            out.name("parent")
                    .beginObject()
                    .name("id").value(model.getParent().getId())
                    .name("name").value(model.getParent().getName())
                    .endObject();
        }
        out.name("name").value(model.getName());
        out.name("href").value(model.getHref());
        out.name("target").value(model.getTarget());
        out.name("icon").value(model.getIcon());
        out.name("permission").value(model.getPermission());
        out.name("sort").value(model.getSort());
        out.name("isShow").value(model.getIsShow());
        out.endObject();
    }

    private static MenuAdapter adapter;
    public MenuAdapter() {}
    public static MenuAdapter getInstance() {
        if (adapter == null){
            adapter = new MenuAdapter();
        }
        return adapter;
    }
}
