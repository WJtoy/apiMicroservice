package com.wolfking.jeesite.modules.sys.entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sys.entity.Sequence;

import java.io.IOException;

/**
 * 产品（简单）自定义Gson序列化/序列化
 */
public class SequenceAdapter extends TypeAdapter<Sequence> {

    @Override
    public Sequence read(final JsonReader in) throws IOException {
        final Sequence model = new Sequence();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "code":
                    model.setCode(in.nextString());
                    break;
                case "prefix":
                    model.setPrefix(in.nextString());
                    break;
                case "dateFormat":
                    model.setDateFormat(in.nextString());
                    break;
                case "dateSeparator":
                    model.setDateSeparator(in.nextString());
                    break;
                case "digitBit":
                    model.setDigitBit(in.nextInt());
                    break;
                case "suffix":
                    model.setSuffix(in.nextString());
                    break;
                case "separator":
                    model.setSeparator(in.nextString());
                    break;
                case "previousDate":
                    model.setPreviousDate(in.nextString());
                    break;
                case "previousDigit":
                    model.setPreviousDigit(in.nextInt());
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final Sequence model) throws IOException {
        out.beginObject();
        out.name("code").value(model.getCode());
        out.name("prefix").value(model.getPrefix());
        out.name("dateFormat").value(model.getDateFormat());
        out.name("dateSeparator").value(model.getDateSeparator());
        out.name("digitBit").value(model.getDigitBit());
        out.name("suffix").value(model.getSuffix());
        out.name("separator").value(model.getSeparator());
        out.name("previousDate").value(model.getPreviousDate());
        out.name("previousDigit").value(model.getPreviousDigit());
        out.endObject();
    }

    private static SequenceAdapter adapter;
    public SequenceAdapter() {}
    public static SequenceAdapter getInstance() {
        if (adapter == null){
            adapter = new SequenceAdapter();
        }
        return adapter;
    }
}
