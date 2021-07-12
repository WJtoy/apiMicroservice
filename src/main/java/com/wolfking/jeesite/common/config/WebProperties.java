package com.wolfking.jeesite.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("web")
public class WebProperties {
    /**
     * 网点配置
     */
    @Getter
    private final ServicePointProperties servicePoint = new ServicePointProperties();

    /**
     * app属性配置
     */
    @Getter
    private final App app = new App();

    /**
     * 分页配置
     */
    @Getter
    private final PageProperties page = new PageProperties();
    @Getter
    private final UserFilesProperties userFiles = new UserFilesProperties();
    @Getter
    private final CacheProperties cache = new CacheProperties();
    @Getter
    private final SequenceProperties sequence = new SequenceProperties();
    @Getter
    private final ShortMessageProperties shortMessage = new ShortMessageProperties();
    @Getter
    private final SiteProperties site = new SiteProperties();
    @Getter
    private final VoiceServiceProperties voiceService = new VoiceServiceProperties();
    @Getter
    private final LogisticsProperties logistics = new LogisticsProperties();
    @Getter
    @Setter
    private String orderPrefix = "";
    @Getter
    @Setter
    private String goLiveDate = "";
    @Getter
    @Setter
    private String productName = "";
    @Getter
    @Setter
    private Boolean pushEnabled = false;
    @Getter
    @Setter Boolean engineerHealthEnabled = false;



    public static class App {
        /**
         * 启用APP公告
         */
        @Getter
        @Setter
        private Boolean noticeEnabled = false;
        /**
         * 已审核成功的iOS app版本
         */
        @Getter
        @Setter
        private String iosAuditedAppVersion = "0.0.0.0";

        @Getter
        @Setter
        public String noticeVersion="0.0.0.0";

        @Getter
        @Setter
        public Boolean servicePointCooperationTermsEnabled = false;

        @Setter
        @Getter
        public Boolean identityCardEnabled = false;

    }

    public static class ServicePointProperties {

        /**
         * 启用网点保险
         */
        @Getter
        @Setter
        private Boolean insuranceEnabled = false;

        /**
         * 强制网点保险（该配置会强制APP主账号必须同意保险条款）
         */
        @Getter
        @Setter
        private Boolean insuranceForced = false;
    }

    public static class PageProperties {
        /**
         * 页尺寸
         */
        @Getter
        @Setter
        private Integer pageSize = 12;
    }

    public static class UserFilesProperties {
        @Getter
        @Setter
        private String baseDir = "";
        @Getter
        @Setter
        private String uploadDir = "";
        @Getter
        @Setter
        private String host = "";
    }

    public static class CacheProperties {
        /**
         *
         */
        @Getter
        @Setter
        private Long timeout = 12*60*60L;
    }

    public static class SequenceProperties {
        @Getter
        @Setter
        private Integer workerId = 0;
        @Getter
        @Setter
        private Integer dataCenterId = 0;
    }

    public static class ShortMessageProperties {
        @Getter
        @Setter
        private String ignoreDataSources = "";
    }

    public static class SiteProperties {
        @Getter
        @Setter
        private String code = "";
        @Getter
        @Setter
        private String name = "";
    }
    public static class VoiceServiceProperties {
        @Getter
        @Setter
        private Boolean enabled = false;
    }

    public static class LogisticsProperties {
        @Getter
        @Setter
        private Boolean orderFlag = false;
        @Getter
        @Setter
        private Boolean materialFlag = false;
    }
}
