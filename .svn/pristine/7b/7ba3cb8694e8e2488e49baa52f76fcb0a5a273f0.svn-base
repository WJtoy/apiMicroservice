package com.wolfking.jeesite.modules.sd.utils;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.adapter.DictSimpleAdapter;
import com.wolfking.jeesite.ms.tmall.md.adapter.B2bShopAdapter;

import java.io.IOException;
import java.util.List;

/**
 * 订单自定义Gson序列化/序列化
 */
public class OrderAdapter extends TypeAdapter<Order> {

    @Override
    public Order read(final JsonReader in) throws IOException {
        final Order order = new Order();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "dataSource":
                    if(in.peek()== JsonToken.NULL){
                        order.setDataSource(new Dict("1","快可立"));
                    }else {
                        order.setDataSource(DictSimpleAdapter.getInstance().read(in));
                    }
                    break;
                case "workCardId":
                    if(in.peek()== JsonToken.NULL){
                        order.setWorkCardId("");
                    }else {
                        order.setWorkCardId(in.nextString());
                    }
                    break;
                case "parentBizOrderId":
                    if(in.peek()== JsonToken.NULL){
                        order.setParentBizOrderId("");
                    }else {
                        order.setParentBizOrderId(in.nextString());
                    }
                    break;
                case "b2bShop":
                    order.setB2bShop(B2bShopAdapter.getInstance().read(in));
                    break;
                case "quarter":
                    order.setQuarter(in.nextString());
                    break;
                case "id":
                    order.setId(Long.valueOf(in.nextString()));
                    break;
                case "orderNo":
                    order.setOrderNo(in.nextString());
                    break;
                case "orderType":
                    in.beginObject();
                    Dict orderType = new Dict();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "label":
                                orderType.setLabel(in.nextString());
                                break;
                            case "value":
                                orderType.setValue(String.valueOf(in.nextInt()));
                                break;
                        }
                    }
                    order.setOrderType(orderType);
                    in.endObject();
                    break;
                case "totalQty":
                    order.setTotalQty(in.nextInt());
                    break;
                case "verificationCode":
                    order.setVerificationCode(in.nextString());
                    break;
                case "confirmDoor":
                    order.setConfirmDoor(in.nextInt());
                    break;
                case "description":
                    order.setDescription(in.nextString());
                    break;
                case "orderFee":
                    order.setOrderFee(OrderFeeAdapter.getInstance().read(in));
                    break;
                case "orderStatus":
                    order.setOrderStatus(OrderStatusAdapter.getInstance().read(in));
                    break;
                case "orderCondition":
                    order.setOrderCondition(OrderConditionAdapter.getInstance().read(in));
                    break;
                case "orderLocation":
                    order.setOrderLocation(OrderLocationAdapter.getInstance().read(in));
                    break;
                //Items
                case "items":
                    in.beginArray();
                    final List items = Lists.newArrayList();
                    while (in.hasNext()) {
                        items.add(OrderItemAdapter.getInstance().read(in));//调用OrderItem的序列化类
                    }
                    order.setItems(items);
                    in.endArray();
                    break;
            }
        }
        in.endObject();
        return order;
    }

    @Override
    public void write(final JsonWriter out, final Order order) throws IOException {
        out.beginObject();
        out.name("dataSource");
        if(order.getDataSource() == null){
            order.setDataSource(new Dict("1","快可立"));
        }
        DictSimpleAdapter.getInstance().write(out,order.getDataSource());
        out.name("workCardId").value(order.getWorkCardId())
                .name("parentBizOrderId").value(order.getParentBizOrderId())
            .name("id").value(order.getId().toString())
            .name("quarter").value(order.getQuarter())
            .name("orderNo").value(order.getOrderNo());
        if(order.getB2bShop() != null){
            out.name("b2bShop");
            B2bShopAdapter.getInstance().write(out,order.getB2bShop());
        }
        if(order.getOrderType() != null){
            out.name("orderType")
                    .beginObject()
                    .name("label").value(order.getOrderType().getLabel())
                    .name("value").value(Integer.parseInt(order.getOrderType().getValue()))
                    .endObject();
        }
        out.name("totalQty").value(order.getTotalQty())
            .name("verificationCode").value(order.getVerificationCode())
            .name("confirmDoor").value(order.getConfirmDoor())
            .name("description").value(order.getDescription().replace("\r\n","<br>"));
        if(order.getOrderFee() != null){
            out.name("orderFee");
            OrderFeeAdapter.getInstance().write(out,order.getOrderFee());
        }

        if(order.getOrderStatus() != null){
            out.name("orderStatus");
            OrderStatusAdapter.getInstance().write(out,order.getOrderStatus());
        }

        if(order.getOrderCondition() != null){
            out.name("orderCondition");
            OrderConditionAdapter.getInstance().write(out,order.getOrderCondition());
        }
        //2019-04-24
        if(order.getOrderLocation() != null){
            out.name("orderLocation");
            OrderLocationAdapter.getInstance().write(out,order.getOrderLocation());
        }

        if(order.getItems() != null && order.getItems().size()>0) {
            out.name("items").beginArray();
            for (final OrderItem item : order.getItems()) {
                OrderItemAdapter.getInstance().write(out, item);
            }
            out.endArray();
        }

        out.endObject();
    }

    private static OrderAdapter adapter;
    public OrderAdapter() {}
    public static OrderAdapter getInstance() {
        if (adapter == null){
            adapter = new OrderAdapter();
        }
        return adapter;
    }
    /*
    public static void main(String[] args) throws IOException {
        //region entity
        long orderId = 1234567890l;
        User user = new User(1l,"管理员","");
        Order order = new Order();
        order.setDataSource(new Dict("1","快可立"));
        order.setWorkCardId("workcardid");
        order.setParentBizOrderId("parent biz id");
        order.setId(orderId);
        order.setQuarter("20201");
        String orderNo = "K202002260000001";
        order.setOrderNo(orderNo);
        B2bCustomerMap b2bCustomerMap = new B2bCustomerMap();
        b2bCustomerMap.setShopName("shopName");
        b2bCustomerMap.setShopId("shopId");
        order.setB2bShop(b2bCustomerMap);
        order.setOrderType(new Dict("1","安装"));
        order.setTotalQty(1);
        order.setVerificationCode("1sdfasf");
        order.setConfirmDoor(1);
        order.setDescription("定的描述");
        //region fee
        OrderFee fee = new OrderFee();
        fee.setExpectCharge(10.0);
        fee.setBlockedCharge(1.0);
        fee.setRebateFlag(1);
        fee.setOrderPaymentType(new Dict("1","月付"));
        fee.setServiceCharge(100.0);
        fee.setMaterialCharge(10.0);
        fee.setExpectCharge(1.0);
        fee.setTravelCharge(2.0);
        fee.setOtherCharge(3.0);
        fee.setCustomerUrgentCharge(5.0);
        fee.setCustomerTimeLiness(6.0);
        fee.setCustomerTimeLinessCharge(7.0);
        fee.setOrderCharge(8.0);
        fee.setCustomerPlanTravelCharge(9.0);
        fee.setCustomerPlanOtherCharge(10.0);
        fee.setEngineerPaymentType(new Dict("1","月付"));
        fee.setEngineerServiceCharge(1.0);
        fee.setEngineerTravelCharge(2.0);
        fee.setEngineerExpressCharge(3.0);
        fee.setEngineerMaterialCharge(4.0);
        fee.setEngineerOtherCharge(5.0);
        fee.setTimeLinessCharge(6.0);
        fee.setSubsidyTimeLinessCharge(7.0);
        fee.setInsuranceCharge(8.0);
        fee.setEngineerUrgentCharge(1.0);
        fee.setEngineerTotalCharge(2.0);
        fee.setPlanTravelCharge(3.0);
        fee.setPlanTravelNo("1231");
        fee.setPlanOtherCharge(8.0);
        fee.setPlanDistance(9.0);
        //endregion
        order.setOrderFee(fee);

        //region status
        OrderStatus status = new OrderStatus();
        status.setCustomerApproveFlag(1);
        status.setCustomerApproveBy(user);
        status.setCustomerApproveDate(new DateTime().plusHours(-8).toDate());
        status.setAcceptDate(new DateTime().plusHours(-6).toDate());
        status.setPlanBy(user);
        status.setPlanDate(new DateTime().plusHours(-4).toDate());
        status.setPlanComment("派单备注");
        status.setFirstContactDate(new DateTime().plusHours(-4).plusMinutes(30).toDate());
        status.setServiceFlag(1);
        status.setServiceDate(new DateTime().plusHours(-2).toDate());
        status.setServiceComment("上门备注");
        status.setServiceTimes(1);
        status.setCloseFlag(1);
        status.setCloseBy(user);
        status.setCloseDate(new DateTime().plusHours(-2).plusMinutes(30).toDate());
        status.setCancelApplyDate(new Date());
        status.setCancelApproveDate(new Date());
        status.setCancelResponsible(new Dict("2","取消原因"));
        status.setCancelApplyBy(user);
        status.setCancelApplyComment("取消单申请原因");
        status.setCancelApproveBy(user);
        status.setCancelApproveFlag(1);
        status.setCancelSponsor(2);

        status.setChargeDate(new Date());
        status.setChargeBy(user);
        status.setEngineerInvoiceDate(new Date());
        status.setCustomerInvoiceDate(new Date());
        status.setUrgentDate(new Date());
        status.setReminderStatus(2);
        status.setComplainStatus(1);
        status.setComplainFlag(1);
        //endregion
        order.setOrderStatus(status);

        //region condition

        OrderCondition condition = new OrderCondition();
        condition.setOrderId(orderId);
        condition.setOrderNo(orderNo);
        //region customer
        Customer customer = new Customer();
        customer.setId(1l);
        customer.setCode("C001");
        customer.setName("客户");
        //sale
        User sale = new User(10022l);
        sale.setName("业务员");
        sale.setQq("191713113");
        sale.setMobile("13084935562");
        customer.setSales(sale);
        customer.setMaster("主账号");
        customer.setPhone("13394952345");
        customer.setContractDate(new DateTime().plusYears(-3).toDate());
        customer.setMinUploadNumber(1);
        customer.setMaxUploadNumber(5);
        customer.setReturnAddress("退件地址");
        //finance
        CustomerFinance finance = new CustomerFinance();
        finance.setInvoiceFlag(1);
        finance.setPaymentType(new Dict("1","月结"));
        customer.setFinance(finance);
        customer.setRemarks("客户备注");
        customer.setDefaultBrand("品牌");
        customer.setEffectFlag(1);
        customer.setShortMessageFlag(1);
        customer.setTimeLinessFlag(1);
        customer.setUrgentFlag(1);
        customer.setVipFlag(1);

        condition.setCustomer(customer);
        //endregion
        Area area = new Area();
        area.setId(3403l);
        area.setName("龙华区");
        area.setFullName("广东省深圳市龙华区");
        condition.setArea(area);
        Area subArea = new Area();
        subArea.setId(34035l);
        subArea.setName("龙华街道");
        condition.setSubArea(subArea);
        condition.setUserName("用户");
        condition.setPhone1("13601018383");
        condition.setPhone2("");
        condition.setServicePhone("13601018383");
        condition.setAddress("用户地址");
        condition.setServiceAddress("服务地址");
        condition.setStatus(new Dict("40","已上门"));
        condition.setSubStatus(40);
        condition.setAppAbnormalyFlag(1);
        condition.setPendingFlag(1);
        condition.setPendingType(new Dict("3","预约日期"));
        condition.setPendingTypeDate(new DateTime().plusDays(-1).toDate());
        condition.setAppointmentDate(new DateTime().plusDays(-1).toDate());
        condition.setFeedbackId(84848l);
        condition.setFeedbackFlag(1);
        condition.setFeedbackDate(new DateTime().plusDays(-3).toDate());
        condition.setFeedbackTitle("标题");
        condition.setFeedbackCloseFlag(1);
        condition.setReplyFlag(1);
        condition.setReplyFlagKefu(1);
        condition.setReplyFlagCustomer(2);
        condition.setCloseDate(new Date());
        condition.setCreateBy(user);
        condition.setCreateDate(new DateTime().plusDays(-5).withHourOfDay(10).withMinuteOfHour(0).toDate());
        condition.setPartsFlag(1);
        condition.setReturnPartsFlag(1);
        condition.setGradeFlag(1);
        condition.setProductCategoryId(1L);
        condition.setProductIds("10");
        condition.setKefu(new User(234243l,"客服",""));
        //servicepoint
        ServicePoint sp = new ServicePoint();
        sp.setId(11111l);
        sp.setName("网点");
        condition.setServicePoint(sp);
        condition.setEngineer(new User(838383838l,"师傅",""));
        condition.setTotalQty(1);
        condition.setServiceTimes(1);
        condition.setServiceTypes("1");
        condition.setFinishPhotoQty(3);
        condition.setVersion(999780000l);
        condition.setChargeFlag(1);
        condition.setQuarter("20201");
        condition.setOrderServiceType(1);
        condition.setAppCompleteType("complete_all");
        condition.setAppCompleteDate(new DateTime().plusHours(-2).toDate());
        condition.setRushOrderFlag(1);
        condition.setTimeLiness(35.0);
        condition.setArrivalDate(new DateTime().plusDays(-3).toDate());
        condition.setCustomerOwner("owner");
        //endregion
        order.setOrderCondition(condition);

        //region orderLocation
        OrderLocation location = new OrderLocation();
        location.setOrderId(orderId);
        location.setQuarter("20201");
        location.setArea(area);
        location.setLongitude(101.38);
        location.setLatitude(84.283);
        location.setDistance(100.0);

        //endregion
        order.setOrderLocation(location);

        //items
        //product
        Product p = new Product();
        p.setId(10l);
        p.setName("产品");
        p.setSetFlag(0);
        ProductCategory pc = new ProductCategory();
        pc.setId(100l);
        pc.setName("品类");
        p.setCategory(pc);
        //serviceType
        ServiceType st = new ServiceType();
        st.setId(1l);
        st.setName("安装");

        OrderItemModel item = new OrderItemModel();
        item.setId(1l);
        item.setOrderId(orderId);
        item.setDelFlag(0);
        item.setItemNo(1);
        item.setProduct(p);
        item.setBrand("品牌");
        item.setProductSpec("规格");
        item.setServiceType(st);
        item.setStandPrice(10.0);
        item.setDiscountPrice(8.0);
        item.setQty(1);
        item.setCharge(10.0);
        item.setBlockedCharge(0.0);
        item.setExpressCompany(new Dict("yt","圆通"));
        item.setExpressNo("快递单号");
        item.setRemarks("item备注");
        item.setB2bProductCode("b2b product code");
        item.setCurrentUser(user);

        order.setItems(Lists.newArrayList(item));

        //endregion entity

        StringBuilder json = new StringBuilder();
        json.append(OrderAdapter.getInstance().toJson(order));
        System.out.println(json.toString());

        Order m = OrderAdapter.getInstance().fromJson(json.toString());
        json.setLength(0);
        json.append(OrderAdapter.getInstance().toJson(m));
        System.out.println(json.toString());

    }
    */
}
