package com.wolfking.jeesite.common.mapper.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.kkl.kklplus.utils.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * 长日期序列化(年月日 小时：分：秒)
 * Created by RyanLu on 2018/2/6.
 */
public class DateTimeAdapter extends TypeAdapter<Date> {
    private final String format ="yyyy-MM-dd HH:mm:ss";
        @Override
        public Date read(final JsonReader in) throws IOException {
            if(in.peek() == JsonToken.NULL){
                return null;
            }else{
                String strDate = in.nextString();
                if(StringUtils.isBlank(strDate)){
                    return null;
                }else {
                    Date date = null;
                    try {
                        date = DateUtils.parse(strDate, format);
                    } catch (Exception e) {
                    }
                    return date;
                }
            }
        }


        @Override
        public void write(final JsonWriter out, final Date model) throws IOException {
            if (model == null) {
                //out.nullValue();//设置null值
                out.value("");
                return;
            }
            String dateFormatAsString = DateUtils.formatDate(model,format);
            out.value(dateFormatAsString);
        }
}
