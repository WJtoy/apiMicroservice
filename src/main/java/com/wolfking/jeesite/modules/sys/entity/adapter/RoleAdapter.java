package com.wolfking.jeesite.modules.sys.entity.adapter;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sys.entity.Office;
import com.wolfking.jeesite.modules.sys.entity.Role;

import java.io.IOException;
import java.util.List;

/**
 * 角色 自定义Gson序列化/序列化
 */
public class RoleAdapter extends TypeAdapter<Role> {

    @Override
    public Role read(final JsonReader in) throws IOException {
        final Role model = new Role();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextLong());
                    break;
                case "office":
                    model.setOffice(OfficeSimpleAdapter.getInstance().read(in));
                    break;
                case "officeList":
                    in.beginArray();
                    List<Office> items = Lists.newArrayList();
                    while (in.hasNext()) {
                        items.add(OfficeSimpleAdapter.getInstance().read(in));
                    }
                    model.setOfficeList(items);
                    in.endArray();
                    break;
                case "name":
                    model.setName(in.nextString());
                    break;
                case "enname":
                    model.setEnname(in.nextString());
                    break;
                case "roleType":
                    model.setRoleType(in.nextString());
                    break;
                case "dataScope":
                    model.setDataScope(in.nextInt());
                    break;
                case "delFlag":
                    model.setDelFlag(in.nextInt());
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
        if(model.getOffice() != null && model.getOffice().getId() != null){
            out.name("office");
            OfficeSimpleAdapter.getInstance().write(out,model.getOffice());
        }
        if(model.getOfficeList() != null){
            out.name("officeList").beginArray();
            Office office;
            for (int i=0,size = model.getOfficeList().size();i<size;i++) {
                office = model.getOfficeList().get(i);
                OfficeSimpleAdapter.getInstance().write(out,office);
            }
            out.endArray();
        }
        out.name("name").value(model.getName());
        out.name("enname").value(model.getEnname());
        out.name("roleType").value(model.getRoleType());
        out.name("dataScope").value(model.getDataScope());
        out.name("delFlag").value(model.getDelFlag());
        out.endObject();
    }

    private static RoleAdapter adapter;
    public RoleAdapter() {}
    public static RoleAdapter getInstance() {
        if (adapter == null){
            adapter = new RoleAdapter();
        }
        return adapter;
    }
}
