package com.wolfking.jeesite.modules.md.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import lombok.Data;

import java.util.Date;

@Data
public class SystemNotice extends LongIDDataEntity<SystemNotice> {

    /**
     * 通知发送给全体
     */
    public final static int NOTICE_TYPE_ALL = 10;
    /**
     * 通知发送给网点
     */
    public final static int NOTICE_TYPE_SERVICE_POINT=20;
    /**
     * 通知发送给客户
     */
    public final static int NOTICE_TYPE_CUSTOMER = 30;

    private String title;
    private String content;
    private Integer noticeType;
    private String subtitle = "";

    private Date startDate;
    private Date endDate;

    public SystemNotice() {
    }

    public SystemNotice(Long id) {
        this.id = id;
    }

}


