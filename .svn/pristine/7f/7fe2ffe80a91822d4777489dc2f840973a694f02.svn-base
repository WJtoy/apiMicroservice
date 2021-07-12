package com.wolfking.jeesite.modules.sd.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.UrgentLevel;
import com.wolfking.jeesite.modules.md.utils.CustomerAdapter;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * @version 0.1
 * 订单自定义Gson序列化/序列化
 *
 * @version 0.2
 * @date 2018/09/11
 * by ryan
 * 移除：orderCharge，engineerTotalCharge
 */
@Slf4j
public class OrderConditionAdapter extends TypeAdapter<OrderCondition> {

    private final String  dateFormat = new String("yyyy-MM-dd HH:mm:ss");

    @Override
    public OrderCondition read(final JsonReader in) throws IOException {
        final OrderCondition order = new OrderCondition();
        StringBuilder strd = new StringBuilder();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "orderId":
                    order.setOrderId(Long.valueOf(in.nextString()));
                    break;
                case "orderNo":
                    order.setOrderNo(in.nextString());
                    break;
                case "customer":
                    order.setCustomer(CustomerAdapter.getInstance().read(in));
                    break;
                case "area":
                    in.beginObject();
                    Area area = new Area();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                area.setId(in.nextLong());
                                break;
                            case "name":
                                area.setName(in.nextString());
                                break;
                            case "fullName":
                                area.setFullName(in.nextString());
                                break;
                            default:
                                break;
                        }
                    }
                    order.setArea(area);
                    in.endObject();
                    break;
                case "subArea":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setSubArea(null);
                    }else {
                        in.beginObject();
                        Area subArea = new Area();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "id":
                                    subArea.setId(in.nextLong());
                                    break;
                                case "name":
                                    subArea.setName(in.nextString());
                                    break;
                                default:
                                    break;
                            }
                        }
                        order.setSubArea(subArea);
                        in.endObject();
                    }
                    break;
                case "userName":
                    order.setUserName(in.nextString());
                    break;
                case "phone1":
                    order.setPhone1(in.nextString());
                    break;
                case "phone2":
                    order.setPhone2(in.nextString());
                    break;
                case "phone3":
                    order.setPhone3(in.nextString());
                    break;
                case "servicePhone":
                    order.setServicePhone(in.nextString());
                    break;
                case "address":
                    order.setAddress(in.nextString());
                    break;
                case "serviceAddress":
                    order.setServiceAddress(in.nextString());
                    break;
                case "status":
                    in.beginObject();
                    Dict status = new Dict();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "label":
                                status.setLabel(in.nextString());
                                break;
                            case "value":
                                status.setValue(String.valueOf(in.nextInt()));
                                break;
                            default:
                                break;
                        }
                    }
                    order.setStatus(status);
                    in.endObject();
                    break;
                case "subStatus":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setSubStatus(Order.ORDER_SUBSTATUS_NEW);
                    }else{
                        order.setSubStatus(in.nextInt());
                    }
                    break;
                case "appAbnormalyFlag":
                    order.setAppAbnormalyFlag(in.nextInt());
                    break;
                //停滞
                case "pendingFlag":
                    order.setPendingFlag(in.nextInt());
                    break;
                case "pendingType":
                    in.beginObject();
                    Dict pendingType = new Dict();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "label":
                                pendingType.setLabel(in.nextString());
                                break;
                            case "value":
                                pendingType.setValue(String.valueOf(in.nextInt()));
                                break;
                            default:
                                break;
                        }
                    }
                    order.setPendingType(pendingType);
                    in.endObject();
                    break;
                case "pendingTypeDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if(StringUtils.isBlank(strd)){
                        order.setPendingTypeDate(null);
                    }else{
                        try{
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            order.setPendingTypeDate(date);
                        } catch (ParseException e) {
                            order.setPendingTypeDate(null);
                            log.error("OrderConditionAdapter.pendingTypeDate 日期格式错误:{}",strd.toString());
                            //try {
                            //    LogUtils.saveLog("日期格式错误:", "OrderConditionAdapter.read", String.format("id:%s,pendingTypeDate:%s", order.getOrderId(), strd), e, null);
                            //}catch (Exception e1){}
                        }
                    }
                    break;
                case "appointmentDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if(StringUtils.isBlank(strd)){
                        order.setAppointmentDate(null);
                    }else{
                        try{
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            order.setAppointmentDate(date);
                        } catch (ParseException e) {
                            order.setAppointmentDate(null);
                            log.error("OrderConditionAdapter.appointmentDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                //反馈
                case "feedbackId":
                    order.setFeedbackId(Long.valueOf(in.nextString()));
                    break;
                case "feedbackFlag":
                    order.setFeedbackFlag(in.nextInt());
                    break;
                case "feedbackDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if(StringUtils.isBlank(strd)){
                        order.setFeedbackDate(null);
                    }else{
                        try{
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            order.setFeedbackDate(date);
                        } catch (ParseException e) {
                            order.setFeedbackDate(null);
                            log.error("OrderConditionAdapter.feedbackDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                case "feedbackTitle":
                    order.setFeedbackTitle(in.nextString());
                    break;
                case "feedbackCloseFlag":
                    order.setFeedbackCloseFlag(in.nextInt());
                    break;
                case "replyFlag":
                    order.setReplyFlag(in.nextInt());
                    break;
                case "replyFlagKefu":
                    order.setReplyFlagKefu(in.nextInt());
                    break;
                case "replyFlagCustomer":
                    order.setReplyFlagCustomer(in.nextInt());
                    break;
                case "closeDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if(StringUtils.isBlank(strd)){
                        order.setCloseDate(null);
                    }else{
                        try{
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            order.setCloseDate(date);
                        } catch (ParseException e) {
                            order.setCloseDate(null);
                            log.error("OrderConditionAdapter.closeDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                case "createBy":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setCreateBy(null);
                    }else{
                        User createBy = new User();
                        in.beginObject();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "id":
                                    createBy.setId(in.nextLong());
                                    break;
                                case "name":
                                    createBy.setName(in.nextString());
                                    break;
                                default:
                                    break;
                            }
                        }
                        order.setCreateBy(createBy);
                        in.endObject();
                    }
                    break;
                case "createDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if(StringUtils.isBlank(strd)){
                        order.setCreateDate(null);
                    }else{
                        try{
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            order.setCreateDate(date);
                        } catch (ParseException e) {
                            order.setCreateDate(null);
                            log.error("OrderConditionAdapter.createDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                //配件
                case "partsFlag":
                    order.setPartsFlag(in.nextInt());
                    break;
                case "returnPartsFlag":
                    order.setReturnPartsFlag(in.nextInt());
                    break;
                case "gradeFlag":
                    order.setGradeFlag(in.nextInt());
                    break;
                case "productCategoryId":
                    order.setProductCategoryId(in.nextLong());
                    break;
                case "productCategory":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setProductCategory(null);
                    }else{
                        in.beginObject();
                        ProductCategory productCategory = new ProductCategory();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "id":
                                    productCategory.setId(in.nextLong());
                                    break;
                                case "name":
                                    productCategory.setName(in.nextString());
                                    break;
                                case "appCompleteFlag":
                                    productCategory.setAppCompleteFlag(in.nextInt());
                                    break;
                                case "autoGradeFlag":
                                    productCategory.setAutoGradeFlag(in.nextInt());
                                    break;
                                default:
                                    break;
                            }
                        }
                        in.endObject();
                    }
                    break;
                case "productIds":
                    order.setProductIds(in.nextString());
                    break;
                case "kefu":
                    User kefu = new User();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                kefu.setId(in.nextLong());
                                break;
                            case "name":
                                kefu.setName(in.nextString());
                                break;
                            default:
                                break;
                        }
                    }
                    order.setKefu(kefu);
                    in.endObject();
                    break;
                //安维
                case "servicePoint":
                    in.beginObject();
                    ServicePoint servicePoint = new ServicePoint();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                servicePoint.setId(Long.valueOf(in.nextString()));
                                break;
                            case "name":
                                servicePoint.setName(in.nextString());
                                break;
                            default:
                                break;
                        }
                    }
                    order.setServicePoint(servicePoint);
                    in.endObject();
                    break;
                case "engineer":
                    in.beginObject();
                    User engineer = new User();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                engineer.setId(Long.valueOf(in.nextString()));
                                break;
                            case "name":
                                engineer.setName(in.nextString());
                                break;
                            default:
                                break;
                        }
                    }
                    order.setEngineer(engineer);
                    in.endObject();
                    break;
                case "totalQty":
                    order.setTotalQty(in.nextInt());
                    break;
                case "serviceTimes":
                    order.setServiceTimes(in.nextInt());
                    break;

                case "serviceTypes":
                    order.setServiceTypes(in.nextString());
                    break;
                case "finishPhotoQty":
                    order.setFinishPhotoQty(in.nextInt());
                    break;
                case "version":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setVersion(System.currentTimeMillis());
                    }else {
                        order.setVersion(in.nextLong());
                    }
                    break;
                case "chargeFlag":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setChargeFlag(0);
                    }else{
                        order.setChargeFlag(in.nextInt());
                    }
                    break;
                case "quarter":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setQuarter("");
                    }else{
                        order.setQuarter(in.nextString());
                    }
                    break;
                case "orderServiceType":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setOrderServiceType(0);
                    }else{
                        order.setOrderServiceType(in.nextInt());
                    }
                    break;
                case "appCompleteType":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setAppCompleteType("");
                    }else{
                        order.setAppCompleteType(in.nextString());
                    }
                    break;
                case "appCompleteDate":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setAppCompleteDate(null);
                    }else {
                        strd.setLength(0);
                        strd.append(in.nextString());
                        if (StringUtils.isBlank(strd)) {
                            order.setAppCompleteDate(null);
                        } else {
                            try {
                                Date date = DateUtils.parse(strd.toString(), dateFormat);
                                order.setAppCompleteDate(date);
                            } catch (ParseException e) {
                                order.setAppCompleteDate(null);
                                log.error("OrderConditionAdapter.appCompleteDate 日期格式错误:{}",strd);
                            }
                        }
                    }
                    break;
                    //投诉标识转到orderStatus
                /*case "isComplained":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setIsComplained(0);
                    }else{
                        order.setIsComplained(in.nextInt());
                    }
                    break;*/
                case "rushOrderFlag":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setRushOrderFlag(0);
                    }else{
                        order.setRushOrderFlag(in.nextInt());
                    }
                    break;
                case "timeLiness":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                    }else{
                        order.setTimeLiness(in.nextDouble());
                    }
                    break;
                case "arrivalDate":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setArrivalDate(null);
                    }else {
                        strd.setLength(0);
                        strd.append(in.nextString());
                        if (StringUtils.isBlank(strd)) {
                            order.setArrivalDate(null);
                        } else {
                            try {
                                Date date = DateUtils.parse(strd.toString(), dateFormat);
                                order.setArrivalDate(date);
                            } catch (ParseException e) {
                                order.setArrivalDate(null);
                                log.error("OrderConditionAdapter.arrivalDate 日期格式错误:{}",strd);
                            }
                        }
                    }
                    break;
                case "urgentLevel":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setUrgentLevel(null);
                    }else{
                        in.beginObject();
                         UrgentLevel urgentLevel = new UrgentLevel();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "id":
                                    urgentLevel.setId(Long.valueOf(in.nextString()));
                                    break;
                                case "remarks":
                                    urgentLevel.setRemarks(in.nextString());
                                    break;
                                default:
                                    break;
                            }
                        }
                        order.setUrgentLevel(urgentLevel);
                        in.endObject();
                    }
                    break;
                case "customerOwner":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        order.setCustomerOwner("");
                    }else{
                        order.setCustomerOwner(in.nextString());
                    }
                    break;
                case "canRush":
                    order.setCanRush(in.nextInt());
                    break;
                default:
                    break;
            }
        }
        in.endObject();
        strd.setLength(0);
        return order;
    }

    @Override
    public void write(final JsonWriter out, final OrderCondition order) throws IOException {
        out.beginObject();
        out.name("orderId").value(order.getOrderId().toString())
           .name("orderNo").value(order.getOrderNo());
        //用户
        if(order.getArea()!=null) {
            out.name("area")
                    .beginObject()
                    .name("id").value(order.getArea().getId())
                    .name("name").value(order.getArea().getName())
                    .name("fullName").value(order.getArea().getFullName())
                    .endObject();
        }
        //2018/06/06
        if(order.getSubArea()!=null) {
            out.name("subArea")
                    .beginObject()
                    .name("id").value(order.getSubArea().getId())
                    .name("name").value(order.getSubArea().getName())
                    //.name("fullName").value(order.getSubArea().getFullName())
                    .endObject();
        }
        out.name("userName").value(order.getUserName());
        out.name("phone1").value(order.getPhone1());
        out.name("phone2").value(order.getPhone2());
        out.name("phone3").value(order.getPhone3());
        out.name("servicePhone").value(order.getServicePhone());
        out.name("address").value(order.getAddress());
        out.name("serviceAddress").value(order.getServiceAddress());
        if (order.getCustomer() != null){
            out.name("customer")
                    .beginObject()
                    .name("id").value(order.getCustomer().getId().toString())
                    .name("name").value(order.getCustomer().getName())
                    .endObject();
        }
        if(order.getStatus() != null){
            out.name("status")
                    .beginObject()
                    .name("label").value(order.getStatus().getLabel())
                    .name("value").value(Integer.parseInt(order.getStatus().getValue()))
                    .endObject();
        }
        if(order.getSubStatus() != null){
            out.name("subStatus").value(order.getSubStatus());
        }
        out.name("appAbnormalyFlag").value(order.getAppAbnormalyFlag());
        //停滞
        out.name("pendingFlag").value(order.getPendingFlag());
        if(order.getPendingFlag() > 0){
            out.name("pendingType")
                    .beginObject()
                    .name("label").value(order.getPendingType().getLabel())
                    .name("value").value(Integer.parseInt(order.getPendingType().getValue()))
                    .endObject();
            if(order.getPendingTypeDate() != null){
                out.name("pendingTypeDate").value(DateUtils.formatDate(order.getPendingTypeDate(),dateFormat));
            }
        }
        if(order.getAppointmentDate() != null){
            out.name("appointmentDate").value(DateUtils.formatDate(order.getAppointmentDate(),dateFormat));
        }

        //反馈
        out.name("feedbackId").value(order.getFeedbackId().toString())
                .name("feedbackFlag").value(order.getFeedbackFlag())
                .name("feedbackTitle").value(order.getFeedbackTitle())
                .name("feedbackDate").value(order.getFeedbackDate()==null?"":DateUtils.formatDate(order.getFeedbackDate(),dateFormat))
                .name("feedbackCloseFlag").value(order.getFeedbackCloseFlag())
                .name("replyFlag").value(order.getReplyFlag())
                .name("replyFlagKefu").value(order.getReplyFlagKefu())
                .name("replyFlagCustomer").value(order.getReplyFlagCustomer());

        if(order.getCloseDate() != null){
            out.name("closeDate").value(DateUtils.formatDate(order.getCloseDate(),dateFormat));
        }
        if(order.getCloseDate() != null){
            out.name("closeDate").value(DateUtils.formatDate(order.getCloseDate(),dateFormat));
        }
        if(order.getCreateBy() != null){
            out.name("createBy")
                    .beginObject()
                    .name("id").value(order.getCreateBy().getId())
                    .name("name").value(order.getCreateBy().getName())
                    .endObject();
        }
        if(order.getKefu() != null){
            out.name("kefu")
                    .beginObject()
                    .name("id").value(order.getKefu().getId())
                    .name("name").value(order.getKefu().getName())
                    .endObject();
        }
        //配件
        out.name("partsFlag").value(order.getPartsFlag());
        out.name("returnPartsFlag").value(order.getReturnPartsFlag());

        out.name("gradeFlag").value(order.getGradeFlag());
        out.name("productCategoryId").value(order.getProductCategoryId()==null?0L:order.getProductCategoryId());
        ProductCategory productCategory = order.getProductCategory();
        if(productCategory != null && productCategory.getId() != null){
            out.name("productCategory")
                    .beginObject()
                    .name("id").value(productCategory.getId()==null?0L:productCategory.getId())
                    .name("name").value(StringUtils.trimToEmpty(productCategory.getName()))
                    .name("appCompleteFlag").value(productCategory.getAppCompleteFlag()==null?0:productCategory.getAppCompleteFlag())
                    .name("autoGradeFlag").value(productCategory.getAutoGradeFlag()==null?0:productCategory.getAutoGradeFlag())
                    .endObject();
        }
        out.name("productIds").value(order.getProductIds());
        //安维
        if(order.getServicePoint() != null){
            out.name("servicePoint")
                    .beginObject()
                    .name("id").value(order.getServicePoint().getId().toString())
                    .name("name").value(order.getServicePoint().getName())
                    .endObject();
        }
        if(order.getEngineer() != null) {
            out.name("engineer")
                    .beginObject()
                    .name("id").value(order.getEngineer().getId().toString())
                    .name("name").value(order.getEngineer().getName())
                    .endObject();
        }
        out.name("totalQty").value(order.getTotalQty());
        out.name("serviceTimes").value(order.getServiceTimes());
        out.name("serviceTypes").value(order.getServiceTypes());
        out.name("finishPhotoQty").value(order.getFinishPhotoQty());
        out.name("version").value(order.getVersion());
        out.name("chargeFlag").value(order.getChargeFlag()==null?0:order.getChargeFlag());
        out.name("quarter").value(order.getQuarter());
        out.name("orderServiceType").value(order.getOrderServiceType());
        out.name("appCompleteType").value(order.getAppCompleteType());
        //2018/06/05
        if(order.getAppCompleteDate() != null){
            out.name("appCompleteDate").value(DateUtils.formatDate(order.getAppCompleteDate(),dateFormat));
        }
        //out.name("isComplained").value(order.getIsComplained()); //投诉转移到orderStatus
        out.name("rushOrderFlag").value(order.getRushOrderFlag());//2018/04/13
        out.name("timeLiness").value(order.getTimeLiness());//2018/05/17
        //2018/05/19
        if(order.getArrivalDate() != null){
            out.name("arrivalDate").value(DateUtils.formatDate(order.getArrivalDate(),dateFormat));
        }
        //2018/06/06
        if(order.getUrgentLevel() != null){
            out.name("urgentLevel")
                    .beginObject()
                    .name("id").value(order.getUrgentLevel().getId()==null?"0":order.getUrgentLevel().getId().toString())
                    .name("remarks").value(order.getUrgentLevel().getRemarks())
                    .endObject();
        }
        out.name("customerOwner").value(order.getCustomerOwner());
        out.name("canRush").value(order.getCanRush());
        out.endObject();
    }

    private static OrderConditionAdapter adapter;
    public OrderConditionAdapter() {}
    public static OrderConditionAdapter getInstance() {
        if (adapter == null){
            adapter = new OrderConditionAdapter();
        }
        return adapter;
    }
}
