package com.wolfking.jeesite.modules.api.entity.fi.mapper;

import com.google.common.collect.Lists;
import com.kkl.kklplus.entity.sys.IMNoticeInfo;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.api.entity.fi.RestServicePointNotice;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RestServicePointNoticeMapper extends CustomMapper<RestServicePointNotice, IMNoticeInfo> {
    /**
     * 得到网页中图片的地址
     */
    private static List<String> getImgSrc(String htmlStr) {
        List<String> pics = Lists.newArrayList();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            //得到<img />数据
            img = m_image.group();
            //匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    private static String replace(String content) {
        String result = content;
        if (StringUtils.isNotBlank(content)) {
            result = result.replaceAll("\"", "'");
            result = result.replaceAll("\r\n", "");
        }
        return result;
    }

    @Override
    public void mapAtoB(RestServicePointNotice a, IMNoticeInfo b, MappingContext context) {

    }

    @Override
    public void mapBtoA(IMNoticeInfo b, RestServicePointNotice a, MappingContext context) {
        a.setId(b.getId());
        a.setTitle(b.getTitle());
        a.setSubTitle(StringUtils.isNotBlank(b.getSubTitle()) ? b.getSubTitle() : b.getTitle());
        a.setContent(replace(b.getContent()));
        List<String> imageList = getImgSrc(b.getContent());
        a.setImageList(imageList);
        a.setCreateByName("");
        a.setCreateDt(b.getCreateAt());
    }
}
