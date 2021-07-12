package com.wolfking.jeesite.modules.sys.entity.adapter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.CustomerAccountProfile;
import com.wolfking.jeesite.modules.md.utils.CustomerAccountProfileAdapter;
import com.wolfking.jeesite.modules.md.utils.CustomerSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.Role;
import com.wolfking.jeesite.modules.sys.entity.User;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户自定义Gson序列化/序列化
 */
public class UserAdapter extends TypeAdapter<User> {

    private final String  dateFormat = new String("yyyy-MM-dd HH:mm:ss");

    @Override
    public User read(final JsonReader in) throws IOException {
        final User model = new User();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextLong());
                    break;
                case "company":
                    model.setCompany(OfficeSimpleAdapter.getInstance().read(in));
                    break;
                case "office":
                    model.setOffice(OfficeSimpleAdapter.getInstance().read(in));
                    break;
                case "name":
                    model.setName(in.nextString());
                    break;
                case "loginName":
                    model.setLoginName(in.nextString());
                    break;
                case "password":
                    model.setPassword(in.nextString());
                    break;
                case "mobile":
                    model.setMobile(in.nextString());
                    break;
                case "qq":
                    model.setQq(in.nextString());
                    break;
                case "email":
                    model.setEmail(in.nextString());
                    break;
                case "phone":
                    model.setPhone(in.nextString());
                    break;
                case "userType":
                    model.setUserType(in.nextInt());
                    break;
                case "subFlag":
                    model.setSubFlag(in.nextInt());
                    break;
                case "engineerId":
                    model.setEngineerId(in.nextLong());
                    break;
                case "appLoged":
                    model.setAppLoged(in.nextInt());
                    break;
                case "appFlag":
                    model.setAppFlag(in.nextInt());
                    break;
                case "shortMessageFlag":
                    model.setShortMessageFlag(in.nextInt());
                    break;
                case "customerList":
                    in.beginArray();
                    List<Customer> items = Lists.newArrayList();
                    while (in.hasNext()) {
                        items.add(CustomerSimpleAdapter.getInstance().read(in));
                    }
                    in.endArray();
                    model.setCustomerList(items);
                    break;
                case "customerIds":
                    in.beginArray();
                    Set<Long> cids = Sets.newHashSet();
                    while (in.hasNext()) {
                        cids.add(in.nextLong());
                    }
                    in.endArray();
                    model.setCustomerIds(cids);
                    break;
                case "roleList":
                    in.beginArray();
                    List<Role> roles = Lists.newArrayList();
                    while (in.hasNext()) {
                        roles.add(RoleSimpleAdapter.getInstance().read(in));
                    }
                    in.endArray();
                    model.setRoleList(roles);
                    break;
                //case "areaList":
                //    in.beginArray();
                //    items.clear();
                //    while (in.hasNext()) {
                //        items.add(customerAdapter.read(in));
                //    }
                //    model.setAreaList(items);
                //    in.endArray();
                //    break;
                case "customerAccountProfile":
                    if(in.peek()== JsonToken.NULL){
                        in.nextNull();
                        model.setCustomerAccountProfile(new CustomerAccountProfile());
                    }else {
                        model.setCustomerAccountProfile(CustomerAccountProfileAdapter.getInstance().read(in));
                    }
                    break;
                case "loginDate":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        model.setLoginDate(null);
                    }else{
                        String strd = in.nextString();
                        if(StringUtils.isBlank(strd)){
                            model.setLoginDate(null);
                        }else {
                            try {
                                Date date = DateUtils.parse(strd, dateFormat);
                                model.setLoginDate(date);
                            } catch (ParseException e) {
                                model.setLoginDate(null);
                            }
                        }
                    }
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final User model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        if(model.getCompany() != null){
            out.name("company");
            OfficeSimpleAdapter.getInstance().write(out,model.getCompany());
        }
        if(model.getOffice() != null){
            out.name("office");
            OfficeSimpleAdapter.getInstance().write(out,model.getOffice());
        }
        out.name("name").value(model.getName());
        out.name("loginName").value(model.getLoginName());
        out.name("password").value(model.getPassword());
        out.name("mobile").value(model.getMobile());
        out.name("phone").value(model.getPhone());
        out.name("qq").value(model.getQq());
        out.name("email").value(model.getEmail());
        out.name("userType").value(model.getUserType());
        out.name("subFlag").value(model.getSubFlag());
        out.name("engineerId").value(model.getEngineerId());
        out.name("appLoged").value(model.getAppLoged());
        out.name("appFlag").value(model.getAppFlag());
        out.name("shortMessageFlag").value(model.getShortMessageFlag());
        if(model.getCustomerList() != null){
            out.name("customerList").beginArray();
            Customer customer;
            for (int i=0,size = model.getCustomerList().size();i<size;i++) {
                customer = model.getCustomerList().get(i);
                CustomerSimpleAdapter.getInstance().write(out,customer);
            }
            out.endArray();
        }
        if(model.getCustomerIds() != null && model.getCustomerIds().size()>0) {
            //customerIds
            out.name("customerIds").beginArray();
            for(Long cid:model.getCustomerIds()){
                out.value(cid);
            }
            out.endArray();
        }
        if(model.getRoleList() != null){
            out.name("roleList").beginArray();
            Role role;
            for (int i=0,size = model.getRoleList().size();i<size;i++) {
                role = model.getRoleList().get(i);
                RoleSimpleAdapter.getInstance().write(out,role);
            }
            out.endArray();
        }
        if(true == model.isCustomer() && model.getCustomerAccountProfile() != null && model.getCustomerAccountProfile().getId() != null){
            out.name("customerAccountProfile");
            CustomerAccountProfileAdapter.getInstance().write(out,model.getCustomerAccountProfile());
        }
        if(model.getLoginDate() != null){
            out.name("loginDate").value(DateUtils.formatDate(model.getLoginDate(),dateFormat));
        }
        out.endObject();
    }

    private static UserAdapter adapter;
    public UserAdapter() {}
    public static UserAdapter getInstance() {
        if (adapter == null){
            adapter = new UserAdapter();
        }
        return adapter;
    }
}
