package com.wolfking.jeesite.ms.tmall.md.entity;

import com.kkl.kklplus.entity.b2b.common.B2BActionType;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.sys.entity.Area;
import lombok.*;

import java.io.Serializable;

@ToString
public class B2BServicePointBatchLog extends LongIDDataEntity<B2BServicePointBatchLog> {

    @Getter
    @Setter
    private Area city;

    @Getter
    @Setter
    private Integer servicePointSuccessCount = 0;

    @Getter
    @Setter
    private Integer servicePointFailureCount = 0;

    @Getter
    @Setter
    private Integer coverServiceSuccessCount = 0;

    @Getter
    @Setter
    private Integer coverServiceFailureCount = 0;

    @Getter
    @Setter
    private Integer capacitySuccessCount = 0;

    @Getter
    @Setter
    private Integer capacityFailureCount = 0;

    @Getter
    @Setter
    private Integer workerSuccessCount = 0;

    @Getter
    @Setter
    private Integer workerFailureCount = 0;

    @Getter
    @Setter
    private String processComment;

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BatchProcessComment implements Serializable {

        @Getter
        @Setter
        private Long servicePointId;

        @Getter
        @Setter
        private Long engineerId;

        @Getter
        @Setter
        private Integer actionType;

        @Getter
        @Setter
        private Integer interfaceType;

        @Getter
        @Setter
        private Integer errorCode;

        @Getter
        @Setter
        private String errorMsg;
    }
}










