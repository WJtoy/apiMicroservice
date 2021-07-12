package com.wolfking.jeesite.modules.sd.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.sd.entity.FeedbackItem;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * 问题反馈内容Gson序列化
 */
public class FeedbackItemAdapter extends TypeAdapter<FeedbackItem> {

    private final String  dateFormat = new String("yyyy-MM-dd HH:mm:ss");

    @Override
    public FeedbackItem read(final JsonReader in) throws IOException {
        final FeedbackItem item = new FeedbackItem();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    if(in.peek()==JsonToken.NULL){
                        in.nextNull();
                        item.setId(null);
                    }else {
                        item.setId(Long.valueOf(in.nextString()));
                    }
                    break;
                case "feedbackId":
                    item.setFeedbackId(Long.valueOf(in.nextString()));
                    break;
                case "contentType":
                    item.setContentType(in.nextInt());
                    break;
                case "remarks":
                    item.setRemarks(in.nextString());
                    break;
                case "createBy":
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
                        }
                    }
                    item.setCreateBy(createBy);
                    in.endObject();
                    break;
                case "userType":
                    item.setUserType(in.nextInt());
                    break;
                case "createDate":
                    StringBuilder strd = new StringBuilder();
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        item.setCreateDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            item.setCreateDate(date);
                        } catch (ParseException e) {
                            item.setCreateDate(null);
                            try {
                                LogUtils.saveLog("日期格式错误:", "FeedbackItemAdapter.read", String.format("id:%s,createDate:%s", item.getId(), strd.toString()), e, null);
                            }catch (Exception e1){}
                        }
                    }
                    strd.setLength(0);
                    break;
                case "createDateString":
                    in.nextString();
                    break;
            }
        }
        in.endObject();
        return item;
    }


    @Override
    public void write(final JsonWriter out, final FeedbackItem item) throws IOException {
        out.beginObject();
        if(item.getId() != null) {
            out.name("id").value(item.getId().toString());
        }
        if(item.getFeedbackId() !=null) {
            out.name("feedbackId").value(item.getFeedbackId().toString());
        }
        out.name("contentType").value(item.getContentType());
        out.name("remarks").value(item.getRemarks());
        if(item.getCreateBy() != null) {
            out.name("createBy")
                    .beginObject()
                    .name("id").value(item.getCreateBy().getId())
                    .name("name").value(item.getCreateBy().getName())
                    .endObject();
        }
        out.name("userType").value(item.getUserType());
        if(item.getCreateDate() != null){
            out.name("createDate").value(DateUtils.formatDate(item.getCreateDate(),dateFormat));
            out.name("createDateString").value(DateUtils.formatDate(item.getCreateDate(),dateFormat));
        }

        out.endObject();
    }

    private static FeedbackItemAdapter adapter;
    public FeedbackItemAdapter() {}
    public static FeedbackItemAdapter getInstance() {
        if (adapter == null){
            adapter = new FeedbackItemAdapter();
        }
        return adapter;
    }

    /*
    public static void main(String[] args) throws IOException {
        FeedbackItem item = new FeedbackItem();
        item.setId(121324234l);
        item.setFeedbackId(938298424l);
        item.setContentType(1);
        item.setRemarks("备注");
        item.setCreateBy(new User(1l,"管理员",""));
        item.setUserType(2);
        item.setCreateDate(new Date());

        StringBuilder json = new StringBuilder();
        json.append(FeedbackItemAdapter.getInstance().toJson(item));
        System.out.println(json.toString());

        FeedbackItem m = FeedbackItemAdapter.getInstance().fromJson(json.toString());
        json.setLength(0);
        json.append(FeedbackItemAdapter.getInstance().toJson(m));
        System.out.println(json.toString());
    }
    */
}
