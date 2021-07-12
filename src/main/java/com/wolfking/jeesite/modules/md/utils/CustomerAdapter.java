package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;

import java.io.IOException;
import java.util.Date;

/**
 * 客户自定义Gson序列化/序列化
 */
public class CustomerAdapter extends TypeAdapter<Customer> {

    private final String  dateFormat = new String("yyyy-MM-dd HH:mm:ss");

    @Override
    public Customer read(final JsonReader in) throws IOException {
        final Customer customer = new Customer();
        StringBuilder strd = new StringBuilder();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    customer.setId(in.nextLong());
                    break;
                case "code":
                    customer.setCode(in.nextString());
                    break;
                case "name":
                    customer.setName(in.nextString());
                    break;
                case "sales":
                    in.beginObject();
                    User sales = new User(0L,"");
                    if(in.peek() == JsonToken.NULL){
                        sales.setQq("");
                        sales.setMobile("");
                    }else {
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "id":
                                    sales.setId(Long.valueOf(in.nextString()));
                                    break;
                                case "name":
                                    sales.setName(in.nextString());
                                    break;
                                case "qq":
                                    sales.setQq(in.nextString());
                                    break;
                                case "mobile":
                                    sales.setMobile(in.nextString());
                                    break;
                            }
                        }
                    }
                    customer.setSales(sales);
                    in.endObject();
                    break;
                case "master":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        customer.setMaster(StringUtils.EMPTY);
                    }else {
                        customer.setMaster(in.nextString());
                    }
                    break;
                case "phone":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        customer.setPhone(StringUtils.EMPTY);
                    } else {
                        customer.setPhone(in.nextString());
                    }
                    break;
                case "contractDate":
                    if(in.peek()==JsonToken.NULL){
                        in.nextNull();
                        customer.setContractDate(null);
                    }else {
                        strd.setLength(0);
                        strd.append(in.nextString());
                        if (StringUtils.isBlank(strd) || strd.toString().equals("0")) {
                            customer.setContractDate(null);
                        } else {
                            try {
                                Date date = DateUtils.parse(strd.toString(),dateFormat);
                                customer.setContractDate(date);
                            } catch (Exception e) {
                                customer.setContractDate(null);
                                try {
                                    LogUtils.saveLog("日期格式错误:", "CustomerAdapter.read", String.format("id:%s,contractDate:%s", customer.getId(), strd.toString()), e, null);//2017/10/26
                                }catch (Exception e1){}
                            }
                        }
                    }
                    break;
                case "minUploadNumber":
                    if(in.peek()==JsonToken.NULL){
                        customer.setMinUploadNumber(0);
                        in.nextNull();
                    }else{
                        customer.setMinUploadNumber(in.nextInt());
                    }
                    break;
                case "maxUploadNumber":
                    customer.setMaxUploadNumber(in.nextInt());
                    break;
                case "returnAddress":
                    customer.setReturnAddress(in.nextString());
                    break;
                case "finance":
                    customer.setFinance(CustomerFinanceAdapter.getInstance().read(in));
                    break;
                case "remarks":
                    customer.setRemarks(in.nextString());
                    break;
                case "defaultBrand":
                    if(in.peek() == JsonToken.NULL){
                     in.nextNull();
                     customer.setDefaultBrand("");
                    }else {
                        customer.setDefaultBrand(in.nextString());
                    }
                    break;
                case "effectFlag"://2018/04/08
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        customer.setEffectFlag(1);
                    }else {
                        customer.setEffectFlag(in.nextInt());
                    }
                    break;
                case "shortMessageFlag"://2018/04/12
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        customer.setShortMessageFlag(1);
                    }else {
                        customer.setShortMessageFlag(in.nextInt());
                    }
                    break;
                case "timeLinessFlag"://2018/06/06
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        customer.setTimeLinessFlag(0);
                    }else {
                        customer.setTimeLinessFlag(in.nextInt());
                    }
                    break;
                case "urgentFlag"://2018/06/06
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        customer.setUrgentFlag(0);
                    }else {
                        customer.setUrgentFlag(in.nextInt());
                    }
                    break;
                case "vipFlag":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        customer.setVipFlag(0);
                    }else {
                        customer.setVipFlag(in.nextInt());
                    }
                    break;
            }
        }

        in.endObject();
        strd.setLength(0);
        return customer;
    }

    @Override
    public void write(final JsonWriter out, final Customer customer) throws IOException {
        out.beginObject();
        out.name("id").value(customer.getId())
            .name("code").value(customer.getCode())
            .name("name").value(customer.getName());
        User sales = customer.getSales();
        if(sales !=null){
            out.name("sales")
                    .beginObject()
                    .name("id").value(sales.getId().toString())
                    .name("name").value(sales.getName())
                    .name("qq").value(sales.getQq())
                    .name("mobile").value(sales.getMobile())
                    .endObject();
        }
        out.name("master").value(customer.getMaster())//负责任/主帐号姓名
            .name("phone").value(customer.getPhone())//主帐号手机号
            .name("contractDate").value(customer.getContractDate()==null?"":DateUtils.formatDate(customer.getContractDate(),dateFormat))
            .name("minUploadNumber").value(customer.getMinUploadNumber())
            .name("maxUploadNumber").value(customer.getMaxUploadNumber())
            .name("returnAddress").value(customer.getReturnAddress());
        if(customer.getFinance() != null) {
            out.name("finance");
            CustomerFinanceAdapter.getInstance().write(out, customer.getFinance());
        }
        out.name("remarks").value(StringUtils.isBlank(customer.getRemarks())?"":customer.getRemarks())
            .name("defaultBrand").value(StringUtils.isBlank(customer.getDefaultBrand())?"":customer.getDefaultBrand().trim())
            .name("effectFlag").value(customer.getEffectFlag())//2018/04/08
            .name("shortMessageFlag").value(customer.getShortMessageFlag())//2018/04/12
            .name("timeLinessFlag").value(customer.getTimeLinessFlag())
            .name("urgentFlag").value(customer.getUrgentFlag())
            .name("vipFlag").value(customer.getVipFlag());
        out.endObject();
    }

    private static CustomerAdapter adapter;
    public CustomerAdapter() {}
    public static CustomerAdapter getInstance() {
        if (adapter == null){
            adapter = new CustomerAdapter();
        }
        return adapter;
    }

}
