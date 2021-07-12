package com.wolfking.jeesite.modules.api.wx.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wechat")
public class WeChatConfig {

    @Getter
    private final WeChatDataConfig weChatDataConfig = new WeChatDataConfig();

    public static class WeChatDataConfig {

        @Getter
        @Setter
        private String appId;

        @Getter
        @Setter
        private String appSecret;

        @Setter
        @Getter
        private String weChatUrl;

        @Getter
        @Setter
        private String authorizationCode;
    }

}
