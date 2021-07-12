package com.wolfking.jeesite.modules.sd.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.entity.OrderStatus;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * 订单自定义Gson序列化/序列化
 */
@Slf4j
public class OrderStatusAdapter extends TypeAdapter<OrderStatus> {

    private final String  dateFormat = new String("yyyy-MM-dd HH:mm:ss");

    @Override
    public OrderStatus read(final JsonReader in) throws IOException {
        final OrderStatus status = new OrderStatus();
        StringBuilder strd = new StringBuilder();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "customerApproveFlag":
                    status.setCustomerApproveFlag(in.nextInt());
                    break;
                case "customerApproveBy":
                    User customerApproveBy = new User();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                customerApproveBy.setId(in.nextLong());
                                break;
                            case "name":
                                customerApproveBy.setName(in.nextString());
                                break;
                        }
                    }
                    status.setCustomerApproveBy(customerApproveBy);
                    in.endObject();
                    break;
                case "customerApproveDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        status.setCustomerApproveDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            status.setCustomerApproveDate(date);
                        } catch (ParseException e) {
                            status.setCustomerApproveDate(null);
                            log.error("OrderConditionAdapter.customerApproveDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                //接单(客服)
                case "acceptDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        status.setAcceptDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            status.setAcceptDate(date);
                        } catch (ParseException e) {
                            status.setAcceptDate(null);
                            log.error("OrderConditionAdapter.acceptDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                //派单(客服)
                case "planBy":
                    User planBy = new User();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                planBy.setId(in.nextLong());
                                break;
                            case "name":
                                planBy.setName(in.nextString());
                                break;
                        }
                    }
                    status.setPlanBy(planBy);
                    in.endObject();
                    break;
                case "planDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        status.setPlanDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            status.setPlanDate(date);
                        } catch (ParseException e) {
                            status.setPlanDate(null);
                            log.error("OrderConditionAdapter.planDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                case "planComment":
                    status.setPlanComment(in.nextString());
                    break;
                case "firstContactDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        status.setFirstContactDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            status.setFirstContactDate(date);
                        } catch (ParseException e) {
                            status.setFirstContactDate(null);
                            log.error("OrderConditionAdapter.firstContactDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                //安维
                //上门服务
                case "serviceFlag":
                    status.setServiceFlag(in.nextInt());
                    break;
                case "serviceDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        status.setServiceDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            status.setServiceDate(date);
                        } catch (ParseException e) {
                            status.setServiceDate(null);
                            log.error("OrderConditionAdapter.serviceDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                case "serviceComment":
                    status.setServiceComment(in.nextString());
                    break;
                case "serviceTimes":
                    status.setServiceTimes(in.nextInt());
                    break;
                //关闭,客服关闭或者用户回复自动关闭
                case "closeFlag":
                    status.setCloseFlag(in.nextInt());
                    break;
                case "closeBy":
                    User closeBy = new User();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                closeBy.setId(in.nextLong());
                                break;
                            case "name":
                                closeBy.setName(in.nextString());
                                break;
                        }
                    }
                    status.setCloseBy(closeBy);
                    in.endObject();
                    break;
                //取消订单
                case "cancelSponsor":
                    status.setCancelSponsor(in.nextInt());
                    break;
                case "cancelResponsible":
                    in.beginObject();
                    Dict cancelResponsible = new Dict();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "label":
                                cancelResponsible.setLabel(in.nextString());
                                break;
                            case "value":
                                cancelResponsible.setValue(String.valueOf(in.nextInt()));
                                break;
                        }
                    }
                    status.setCancelResponsible(cancelResponsible);
                    in.endObject();
                    break;
                case "cancelApplyBy":
                    User cancelApplyBy = new User();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                cancelApplyBy.setId(in.nextLong());
                                break;
                            case "name":
                                cancelApplyBy.setName(in.nextString());
                                break;
                        }
                    }
                    status.setCancelApplyBy(cancelApplyBy);
                    in.endObject();
                    break;
                case "cancelApplyDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        status.setCancelApplyDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            status.setCancelApplyDate(date);
                        } catch (ParseException e) {
                            status.setCancelApplyDate(null);
                            log.error("OrderConditionAdapter.cancelApplyDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                case "cancelApplyComment":
                    status.setCancelApplyComment(in.nextString());
                    break;
                case "cancelApproveFlag":
                    status.setCancelApproveFlag(in.nextInt());
                    break;
                case "cancelApproveBy":
                    User cancelApproveBy = new User();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                cancelApproveBy.setId(in.nextLong());
                                break;
                            case "name":
                                cancelApproveBy.setName(in.nextString());
                                break;
                        }
                    }
                    status.setCancelApproveBy(cancelApproveBy);
                    in.endObject();
                    break;
                case "cancelApproveDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        status.setCancelApproveDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            status.setCancelApproveDate(date);
                        } catch (ParseException e) {
                            status.setCancelApproveDate(null);
                            log.error("OrderConditionAdapter.cancelApproveDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                //结帐
                case "chargeBy":
                    User chargeBy = new User();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                chargeBy.setId(in.nextLong());
                                break;
                            case "name":
                                chargeBy.setName(in.nextString());
                                break;
                        }
                    }
                    status.setChargeBy(chargeBy);
                    in.endObject();
                    break;
                case "chargeDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        status.setChargeDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            status.setChargeDate(date);
                        } catch (ParseException e) {
                            status.setChargeDate(null);
                            log.error("OrderConditionAdapter.chargeDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                case "engineerInvoiceDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        status.setEngineerInvoiceDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            status.setEngineerInvoiceDate(date);
                        } catch (ParseException e) {
                            status.setEngineerInvoiceDate(null);
                            log.error("OrderConditionAdapter.engineerInvoiceDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                case "customerInvoiceDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        status.setCustomerInvoiceDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            status.setCustomerInvoiceDate(date);
                        } catch (ParseException e) {
                            status.setCustomerInvoiceDate(null);
                            log.error("OrderConditionAdapter.customerInvoiceDate 日期格式错误:{}",strd);
                        }
                    }
                    break;
                case "urgentDate":
                    if(in.peek() == JsonToken.NULL){
                        status.setUrgentDate(null);
                        in.nextNull();
                    }else {
                        strd.setLength(0);
                        strd.append(in.nextString());
                        if (StringUtils.isBlank(strd)) {
                            status.setUrgentDate(null);
                        } else {
                            try {
                                Date date = DateUtils.parse(strd.toString(), dateFormat);
                                status.setUrgentDate(date);
                            } catch (ParseException e) {
                                status.setUrgentDate(null);
                                log.error("OrderConditionAdapter.urgentDate 日期格式错误:{}", strd);
                            }
                        }
                    }
                    break;
                case "reminderStatus":
                    if(in.peek() == JsonToken.NULL){
                        status.setReminderStatus(0);
                    }else{
                        status.setReminderStatus(in.nextInt());
                    }
                    break;
                case "complainStatus":
                    if(in.peek() == JsonToken.NULL){
                        status.setComplainStatus(0);
                    }else{
                        status.setComplainStatus(in.nextInt());
                    }
                    break;
                case "complainFlag":
                    if(in.peek() == JsonToken.NULL){
                        status.setComplainFlag(0);
                    }else{
                        status.setComplainFlag(in.nextInt());
                    }
                    break;
            }
        }
        in.endObject();
        strd.setLength(0);
        return status;
    }

    @Override
    public void write(final JsonWriter out, final OrderStatus status) throws IOException {
        out.beginObject();

        //订单开单审批,由客户主账号审批子账号订单
        out.name("customerApproveFlag").value(status.getCancelApproveFlag());
        if(status.getCustomerApproveBy() != null ){
            out.name("customerApproveBy")
                    .beginObject()
                    .name("id").value(status.getCustomerApproveBy().getId())
                    .name("name").value(status.getCustomerApproveBy().getName())
                    .endObject();
        }
        if(status.getCustomerApproveDate() != null){
            out.name("customerApproveDate").value(DateUtils.formatDate(status.getCustomerApproveDate(),dateFormat));
        }
        //接单(客服)
        if(status.getAcceptDate() != null){
            out.name("acceptDate").value(DateUtils.formatDate(status.getAcceptDate(),dateFormat));
        }
        //派单(客服)
        if(status.getPlanBy() != null){
            out.name("planBy")
                    .beginObject()
                    .name("id").value(status.getPlanBy().getId())
                    .name("name").value(status.getPlanBy().getName())
                    .endObject();
        }
        if(status.getPlanDate() != null){
            out.name("planDate").value(DateUtils.formatDate(status.getPlanDate(),dateFormat));
        }
        out.name("planComment").value(status.getPlanComment());
        // 首次联系用户时间
        if(status.getFirstContactDate() != null){
            out.name("firstContactDate").value(DateUtils.formatDate(status.getFirstContactDate(),dateFormat));
        }

        //安维
        //上门服务
        out.name("serviceFlag").value(status.getServiceFlag());
        if(status.getServiceDate() != null){
            out.name("serviceDate").value(DateUtils.formatDate(status.getServiceDate(),dateFormat));
        }
        out.name("serviceComment").value(status.getServiceComment());
        out.name("serviceTimes").value(status.getServiceTimes());

        //关闭,客服关闭或者用户回复自动关闭
        out.name("closeFlag").value(status.getCloseFlag());
        if(status.getCloseBy() != null){
            out.name("closeBy")
                    .beginObject()
                    .name("id").value(status.getCloseBy().getId())
                    .name("name").value(status.getCloseBy().getName())
                    .endObject();
        }

        //取消订单
        out.name("cancelSponsor").value(status.getCancelSponsor());
        if(status.getCancelResponsible() !=null) {
            out.name("cancelResponsible")
                    .beginObject()
                    .name("label").value(status.getCancelResponsible().getLabel())
                    .name("value").value(status.getCancelResponsible().getValue())
                    .endObject();
        }
        if(status.getCancelApplyBy() != null){
            out.name("cancelApplyBy")
                    .beginObject()
                    .name("id").value(status.getCancelApplyBy().getId())
                    .name("name").value(status.getCancelApplyBy().getName())
                    .endObject();
        }
        if(status.getCancelApplyDate() != null){
            out.name("cancelApplyDate").value(DateUtils.formatDate(status.getCancelApplyDate(),dateFormat));
        }
        out.name("cancelApplyComment").value(status.getCancelApplyComment());
        //取消审核
        out.name("cancelApproveFlag").value(status.getCancelApproveFlag());
        if(status.getCancelApproveBy() != null){
            out.name("cancelApproveBy")
                    .beginObject()
                    .name("id").value(status.getCancelApproveBy().getId())
                    .name("name").value(status.getCancelApproveBy().getName())
                    .endObject();
        }
        if(status.getCancelApproveDate() != null){
            out.name("cancelApproveDate").value(DateUtils.formatDate(status.getCancelApproveDate(),dateFormat));
        }
        //结帐
        if(status.getChargeBy() != null){
            out.name("chargeBy")
                    .beginObject()
                    .name("id").value(status.getChargeBy().getId())
                    .name("name").value(status.getChargeBy().getName())
                    .endObject();
        }
        if(status.getChargeDate() != null){
            out.name("chargeDate").value(DateUtils.formatDate(status.getChargeDate(),dateFormat));
        }
        if(status.getEngineerInvoiceDate() != null){
            out.name("engineerInvoiceDate").value(DateUtils.formatDate(status.getEngineerInvoiceDate(),dateFormat));
        }
        if(status.getCustomerInvoiceDate() != null){
            out.name("customerInvoiceDate").value(DateUtils.formatDate(status.getCustomerInvoiceDate(),dateFormat));
        }
        //2018/06/06
        if(status.getUrgentDate() != null){
            out.name("urgentDate").value(DateUtils.formatDate(status.getUrgentDate(),dateFormat));
        }else{
            out.name("urgentDate").value("");
        }
        //2019/08/15
        if (status.getReminderStatus() != null){
            out.name("reminderStatus").value(status.getReminderStatus());
        }
        //投诉 2019-08-28
        if(status.getComplainStatus() != null){
            out.name("complainStatus").value(status.getComplainStatus());
        }
        if(status.getComplainFlag() != null){
            out.name("complainFlag").value(status.getComplainFlag());
        }
        out.endObject();
    }
    
    private static OrderStatusAdapter adapter;
    public OrderStatusAdapter() {}
    public static OrderStatusAdapter getInstance() {
        if (adapter == null){
            adapter = new OrderStatusAdapter();
        }
        return adapter;
    }

    public static void main(String[] args) throws IOException {
        String json = "{\"customerApproveFlag\":1,\"customerApproveBy\":{\"id\":1,\"name\":\"管理员\"},\"customerApproveDate\":\"2020-02-27 05:13:54\",\"acceptDate\":\"2020-02-27 07:13:54\",\"planBy\":{\"id\":1,\"name\":\"管理员\"},\"planDate\":\"2020-02-27 09:13:54\",\"planComment\":\"派单备注\",\"firstContactDate\":\"2020-02-27 09:43:54\",\"serviceFlag\":1,\"serviceDate\":\"2020-02-27 11:13:54\",\"serviceComment\":\"上门备注\",\"serviceTimes\":1,\"closeFlag\":1,\"closeBy\":{\"id\":1,\"name\":\"管理员\"},\"cancelSponsor\":2,\"cancelResponsible\":{\"label\":\"取消原因\",\"value\":\"2\"},\"cancelApplyBy\":{\"id\":1,\"name\":\"管理员\"},\"cancelApplyDate\":\"2020-02-27 13:13:54\",\"cancelApplyComment\":\"取消单申请原因\",\"cancelApproveFlag\":1,\"cancelApproveBy\":{\"id\":1,\"name\":\"管理员\"},\"cancelApproveDate\":\"2020-02-27 13:13:54\",\"chargeBy\":{\"id\":1,\"name\":\"管理员\"},\"chargeDate\":\"2020-02-27 13:13:54\",\"engineerInvoiceDate\":\"2020-02-27 13:13:54\",\"customerInvoiceDate\":\"2020-02-27 13:13:54\",\"urgentDate\":\"2020-02-27 13:13:54\",\"reminderStatus\":2,\"complainStatus\":1,\"complainFlag\":1}";
        OrderStatus status = OrderStatusAdapter.getInstance().fromJson(json);
        System.out.printf(
                "complainStatus:%d ,complainFlag: %d",
                status.getComplainStatus(),
                status.getComplainFlag()
        );
    }
}
