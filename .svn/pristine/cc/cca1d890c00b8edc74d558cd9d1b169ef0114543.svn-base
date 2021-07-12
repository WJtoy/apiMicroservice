package com.wolfking.jeesite.modules.md.utils;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServicePointFinance;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.adapter.DictSimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 网点简单内容 Gson序列化/序列化
 */
public class ServicePointSimpleAdapter extends TypeAdapter<ServicePoint> {
    
    @Override
    public ServicePoint read(final JsonReader in) throws IOException {
        final ServicePoint servicePoint = new ServicePoint();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    servicePoint.setId(in.nextLong());
                    break;
                case "name":
                    servicePoint.setName(in.nextString());
                    break;
                case "contactInfo1":
                    servicePoint.setContactInfo1(in.nextString());
                    break;
                case "contactInfo2":
                    servicePoint.setContactInfo2(in.nextString());
                    break;
                case "servicePointNo":
                    servicePoint.setServicePointNo(in.nextString());
                    break;
                case "address":
                    servicePoint.setAddress(in.nextString());
                    break;
                case "level":
                    in.beginObject();
                    Dict level = new Dict();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "label":
                                level.setLabel(in.nextString());
                                break;
                            case "value":
                                level.setValue(String.valueOf(in.nextInt()));
                                break;
                        }
                    }
                    in.endObject();
                    servicePoint.setLevel(level);
                    break;
                case "shortMessageFlag":
                    if(in.peek()== JsonToken.NULL){
                        in.nextNull();
                        servicePoint.setShortMessageFlag(0);
                    }else {
                        servicePoint.setShortMessageFlag(in.nextInt());
                    }
                    break;
                case "orderCount":
                    servicePoint.setOrderCount(in.nextInt());
                    break;
                case "planCount":
                    servicePoint.setPlanCount(in.nextInt());
                    break;
                case "breakCount":
                    servicePoint.setBreakCount(in.nextInt());
                    break;
                case "remarks":
                    servicePoint.setRemarks(in.nextString());
                    break;
                case "developer":
                    servicePoint.setDeveloper(in.nextString());
                    break;
                case "primary":
                    servicePoint.setPrimary(ServicePointPrimaryAdapter.getInstance().read(in));
                    break;
                case "finance":
                    servicePoint.setFinance(ServicePointFinanceAdapter.getInstance().read(in));
                    break;
                case "subEngineerCount":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        servicePoint.setSubEngineerCount(0);
                    }else{
                        servicePoint.setSubEngineerCount(in.nextInt());
                    }
                    break;
            }
        }

        in.endObject();

        return servicePoint;
    }

    @Override
    public void write(final JsonWriter out, final ServicePoint servicePoint) throws IOException {
        out.beginObject()
                .name("id").value(servicePoint.getId())
                .name("name").value(servicePoint.getName())
                .name("contactInfo1").value(servicePoint.getContactInfo1())
                .name("contactInfo2").value(servicePoint.getContactInfo2())
                .name("servicePointNo").value(servicePoint.getServicePointNo())
                .name("address").value(servicePoint.getAddress());
        out.name("level");
        DictSimpleAdapter.getInstance().write(out,servicePoint.getLevel());
        out.name("shortMessageFlag").value(servicePoint.getShortMessageFlag());
        out.name("orderCount").value(servicePoint.getPlanCount());
        out.name("planCount").value(servicePoint.getPlanCount());
        out.name("breakCount").value(servicePoint.getBreakCount());
        out.name("remarks").value(servicePoint.getRemarks());
        out.name("developer").value(servicePoint.getDeveloper());
        if(servicePoint.getPrimary() != null) {
            out.name("primary");
            ServicePointPrimaryAdapter.getInstance().write(out,servicePoint.getPrimary());
        }
        if(servicePoint.getFinance() != null){
            out.name("finance");
            ServicePointFinanceAdapter.getInstance().write(out,servicePoint.getFinance());
        }
        out.name("subEngineerCount").value(servicePoint.getSubEngineerCount());
        out.endObject();
    }

    private static ServicePointSimpleAdapter adapter;
    public ServicePointSimpleAdapter() {}
    public static ServicePointSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new ServicePointSimpleAdapter();
        }
        return adapter;
    }

    /*
    public static void main(String[] args) throws IOException {
        StringBuilder json = new StringBuilder();
        ServicePoint servicePoint = new ServicePoint(1l);
        servicePoint.setName("网点1");
        servicePoint.setContactInfo1("13700000001");
        servicePoint.setContactInfo2("13700000001");
        servicePoint.setServicePointNo("YH13700000001");
        servicePoint.setAddress("网点地址");
        servicePoint.setLevel(new Dict("1","一星"));
        servicePoint.setShortMessageFlag(1);
        servicePoint.setAutoCompleteOrder(1);
        servicePoint.setOrderCount(100);
        servicePoint.setPlanCount(105);
        servicePoint.setBreakCount(5);
        servicePoint.setRemarks("备注");
        servicePoint.setDeveloper("开发人员");
        //engineer
        Engineer engineer = new Engineer(101l);
        engineer.setName("刘师傅");
        engineer.setAppFlag(1);
        engineer.setContactInfo("13700000001");
        engineer.setAppLoged(1);
        json.setLength(0);
        json.append(ServicePointPrimaryAdapter.getInstance().toJson(engineer));
        System.out.println("primary:" + json.toString());
        servicePoint.setPrimary(engineer);

        //finance
        ServicePointFinance finance = new ServicePointFinance();
        finance.setId(1l);
        finance.setPaymentType(new Dict("1","月结"));
        finance.setBank(new Dict("jh","建行"));
        finance.setBranch("建行分行");
        finance.setBankNo("12312313");
        finance.setBankOwner("开户人");
        finance.setBankIssue(new Dict("0","无"));
        finance.setInvoiceFlag(1);
        finance.setDiscountFlag(1);
        finance.setDiscount(100.0);
        finance.setLastPayDate(new Date());
        finance.setLastPayAmount(10.0);
        finance.setBalance(50);
        finance.setDebtsAmount(10);
        finance.setDebtsDescrption("描述");
        finance.setInsuranceAmount(15);
        json.setLength(0);
        json.append(ServicePointFinanceAdapter.getInstance().toJson(finance));
        System.out.println("finance:" + json.toString());

        servicePoint.setFinance(finance);

        servicePoint.setSubEngineerCount(0);
        servicePoint.setPlanRemark("派单备注");
        servicePoint.setInsuranceFlag(1);
        servicePoint.setAppInsuranceFlag(1);
        servicePoint.setTimeLinessFlag(1);
        servicePoint.setForTmall(1);
        servicePoint.setStatus(new Dict("1","状态"));
        servicePoint.setAutoPlanFlag(1);
        servicePoint.setUseDefaultPrice(10);
        servicePoint.setCapacity(100);
        servicePoint.setProductCategoryIds(Lists.newArrayList(1L,2L));
        List<ProductCategory> productCategories = new ArrayList<>();
        ProductCategory category1 = new ProductCategory(1l);
        category1.setName("厨电");
        category1.setCode("01");
        productCategories.add(category1);
        ProductCategory category2 = new ProductCategory(2l);
        category2.setName("空调");
        category2.setCode("02");
        productCategories.add(category2);
        servicePoint.setProductCategories(productCategories);

        //output
        json.setLength(0);
        json.append(ServicePointSimpleAdapter.getInstance().toJson(servicePoint));
        System.out.println("servicepoint:" + json.toString());
        ServicePoint s = ServicePointSimpleAdapter.getInstance().fromJson(json.toString());
        //engineer
        //Engineer e = ServicePointPrimaryAdapter.getInstance().fromJson(json.toString());
        Engineer e = s.getPrimary();
        System.out.println("id:" + e.getId() + ",name:" + e.getName() + ",appFlag:" + e.getAppFlag() + ",contactInfo:" + e.getContactInfo() + ",appLoged:" + e.getAppLoged());

        //finance
        //ServicePointFinance f = ServicePointFinanceAdapter.getInstance().fromJson(json.toString());
        ServicePointFinance f = s.getFinance();
        System.out.printf(
                "id:%d,paymentype:%s , bank:%s ,branch: %s ,bankno: %s,bankowner: %s,bankIssue: %s,invoiceFlag: %d,discountFlag:%d, discount: %f, lastPayDate: %s, lastPayAmount:%f, balance:%f ,debtsAmount:%f ,debtsDescrption:%s",
                f.getId(),
                f.getPaymentType(),
                f.getBank(),
                f.getBranch(),
                f.getBankNo(),
                f.getBankOwner(),
                f.getBankIssue().getLabel(),
                f.getInvoiceFlag(),
                f.getDiscountFlag(),
                f.getDiscount(),
                f.getLastPayDate(),
                f.getLastPayAmount(),
                f.getBalance(),
                f.getDebtsAmount(),
                f.getDebtsDescrption()
        );
    }
    */
}
