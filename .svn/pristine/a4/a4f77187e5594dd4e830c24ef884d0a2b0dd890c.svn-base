package com.wolfking.jeesite.ms.tmall.md.entity;

import com.kkl.kklplus.entity.b2b.common.B2BActionType;
import com.kkl.kklplus.entity.b2b.common.B2BProcessFlag;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.ms.enums.YesNo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class MdB2bTmall extends LongIDDataEntity<MdB2bTmall> {

    public static int PROCESS_FLAG_SUCCESS = B2BProcessFlag.PROCESS_FLAG_SUCESS.value;
    public static int ACTION_TYPE_NONE = B2BActionType.ACTION_TYPE_NONE.value;

    public enum InterfaceType {
        /**
         * 基础信息的类型
         */
        INTERFACE_TYPE_SERVICESTORE(0, "服务网点"),
        INTERFACE_TYPE_SERVICESTORECOVERSERVICE(1, "服务覆盖的服务"),
        INTERFACE_TYPE_SERVICESTORECAPACITY(2, "网点容量"),
        INTERFACE_TYPE_WORKER(3, "工人");

        public int value;
        public String name;

        InterfaceType(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    public MdB2bTmall() {
        super();
    }

    public MdB2bTmall(Long id) {
        this();
        this.id = id;
    }

    @Getter
    @Setter
    private ServicePoint servicePoint;

    @Getter
    @Setter
    private Engineer engineer;

    /**
     * 记录是否由批处理操作创建
     */
    @Getter
    @Setter
    private Integer batchFlag = YesNo.NO.value;

    @Getter
    @Setter
    private Integer interfaceType;

    @Getter
    @Setter
    private String infoJson;

    @Getter
    @Setter
    private Integer actionType;

    @Getter
    @Setter
    private Integer processFlag;

    @Getter
    @Setter
    private Integer processTime = 0;

    @Getter
    @Setter
    private String processComment = "";

    @Getter
    @Setter
    private String quarter;

}
