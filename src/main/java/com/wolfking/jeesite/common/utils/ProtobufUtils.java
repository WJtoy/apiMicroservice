package com.wolfking.jeesite.common.utils;

import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.kkl.kklplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 * Protobuf工具类
 *
 * @author Ryan Lu
 * @version 2020-03-15
 */
@Slf4j
public class ProtobufUtils {

    /**
     * 将字符串转换为protobuf3对象
     *
     * @param str  字符(base64处理过的，类似消息队列中传递的内容)
     * 示例: ChQIbxIM5pyA5aSn55u05b6EGgIBAgoTCHASDOeBr+WktOaVsOmHjxoBAxITCAESD+eugOaYk+WQuOmhtueBrxITCAISD+awtOaZtuWQuOmhtueBrxIQCAMSDOawtOaZtuWQiueBrw==
     */
    public static <M extends Message> M transStringToProtobuf(Parser<M> parse, String str){
        if (StringUtils.isNotBlank(str)) {
            byte[] baseBytes = Base64.getDecoder().decode(str);
            InputStream is = new ByteArrayInputStream(baseBytes);
            try {
                return (M)parse.parseFrom(is);
            } catch (Exception e) {
                log.error("字符转proto对象错误:{}",str,e);
            }
        } else {
            return null;
        }
        return null;
    }

}
