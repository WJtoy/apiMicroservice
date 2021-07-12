package com.wolfking.jeesite.modules.sys.entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sys.entity.Role;

import java.io.IOException;

/**
 * 角色（简单）自定义Gson序列化/序列化
 */
public class RoleSimpleAdapter extends TypeAdapter<Role> {

    @Override
    public Role read(final JsonReader in) throws IOException {
        final Role model = new Role();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextLong());
                    break;
                case "name":
                    model.setName(in.nextString());
                    break;
                case "enname":
                    model.setEnname(in.nextString());//英文名称
                    break;
                case "roleType":
                    model.setRoleType(in.nextString());// 权限类型
                    break;
                case "dataScope":
                    model.setDataScope(in.nextInt());
                    break;
            }
        }
        in.endObject();
        return model;
    }


    @Override
    public void write(final JsonWriter out, final Role model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        out.name("name").value(model.getName());
        out.name("enname").value(model.getEnname());
        out.name("roleType").value(model.getRoleType());
        out.name("dataScope").value(model.getDataScope());
        out.endObject();
    }

    private static RoleSimpleAdapter adapter;
    public RoleSimpleAdapter() {}
    public static RoleSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new RoleSimpleAdapter();
        }
        return adapter;
    }
}
