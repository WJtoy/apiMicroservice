package com.wolfking.jeesite.modules.sd.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.entity.OrderProcessLog;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * 订单Gson序列化
 */
@Slf4j
public class WebOrderProcessLogAdapter extends TypeAdapter<OrderProcessLog> {

    private final String  dateFormat = new String("yyyy-MM-dd HH:mm:ss");

    @Override
    public OrderProcessLog read(final JsonReader in) throws IOException {
        final OrderProcessLog item = new OrderProcessLog();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "statusFlag":
                    item.setStatusFlag(in.nextInt());
                    break;
                case "visibilityFlag":
                    item.setVisibilityFlag(in.nextInt());
                    break;
                case "action":
                    item.setAction(in.nextString());
                    break;
                case "actionComment":
                    item.setActionComment(in.nextString());
                    break;
                case "remarks":
                    item.setRemarks(in.nextString());
                    break;
                case "createBy":
                    in.beginObject();
                    User user = new User(0L);
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                user.setId(in.nextLong());
                                break;
                            case "name":
                                user.setName(in.nextString());
                                break;
                        }
                    }
                    in.endObject();
                    item.setCreateBy(user);
                    break;
                case "createDate":
                    StringBuilder strd = new StringBuilder();
                    strd.append(in.nextString());
                    if(StringUtils.isBlank(strd)){
                        item.setCreateDate(null);
                    }else{
                        try{
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            item.setCreateDate(date);
                        } catch (ParseException e) {
                            item.setCreateDate(null);
                            try {
                                log.error("日期格式错误 id:{} ,createDate:{}", item.getId(), strd, e);
                            }catch (Exception e1){}
                        }
                    }
                    strd.setLength(0);
                    break;
            }
        }
        in.endObject();
        return item;
    }

    @Override
    public void write(final JsonWriter out, final OrderProcessLog item) throws IOException {
        out.beginObject();
        out.name("statusFlag").value(item.getStatusFlag())
        .name("visibilityFlag").value(item.getVisibilityFlag())
        .name("action").value(item.getAction())
        .name("actionComment").value(item.getActionComment())
        .name("remarks").value(item.getRemarks())
        .name("createBy")
        .beginObject()
            .name("id").value(item.getCreateBy().getId())
            .name("name").value(item.getCreateBy().getName())
        .endObject();
        if(item.getCreateDate() != null){
            out.name("createDate").value(DateUtils.formatDate(item.getCreateDate(),dateFormat));
        }
        out.endObject();
    }

    private static WebOrderProcessLogAdapter adapter;
    public WebOrderProcessLogAdapter() {}
    public static WebOrderProcessLogAdapter getInstance() {
        if (adapter == null){
            adapter = new WebOrderProcessLogAdapter();
        }
        return adapter;
    }
}
