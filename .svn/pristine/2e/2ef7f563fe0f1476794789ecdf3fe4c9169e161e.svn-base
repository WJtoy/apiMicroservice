/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sys.entity;

import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.Objects;

//import com.wolfking.jeesite.common.persistence.DataEntity;

/**
 * 字典Entity
 *
 * @author ThinkGem
 * @version 2013-05-15
 */
//@JsonAdapter(DictSimpleAdapter.class)
public class Dict extends LongIDDataEntity<Dict> {

    public static final String DICT_TYPE_CANCEL_RESPONSIBLE = "cancel_responsible";
    public static final String DICT_TYPE_ORDER_STATUS = "order_status";
    public static final String DICT_TYPE_DATA_SOURCE = "order_data_source";
    public static final String DICT_TYPE_COMPLAIN_STATUS = "complain_status";
    public static final String DICT_TYPE_PENDING_TYPE = "PendingType";
    public static final String DICT_TYPE_EXPRESS_TYPE = "express_type";
    public static final String DICT_TYPE_SERVICE_POINT_STATUS = "service_point_status";
    public static final String DICT_TYPE_BANK_TYPE = "banktype";
    public static final String DICT_TYPE_ORDER_SERVICE_TYPE = "order_service_type";

    public static final String DICT_TYPE_APP_FORCE_UPDATE_FLAG = "AppForceUpdateFlag"; //App强制更新标记（0 - 不强制、1 - 强制）

    public static final String DICT_TYPE_PAYMENT_TYPE = "PaymentType";
    public static final String DICT_TYPE_APP_WALLET_TRANSACTION_TYPE = "App_Wallet_Transaction_Type"; //App钱包的交易类型


    private static final long serialVersionUID = 1L;
    private String value = "";    // 数据值
    private String label = "";    // 标签名
    @GsonIgnore
    private String type = "";    // 类型
    //	@GsonIgnore
    private String description = "";// 描述
    private Integer sort = 10;    // 排序
    @GsonIgnore
    private Integer parentId = 0;//父Id
    @GsonIgnore
    private Integer aloneManagement = 0; //单独功能维护标记

    public Dict() {
        super();
    }

    public Dict(Long id) {
        super(id);
    }

    public Dict(String value) {
        this.value = value;
    }

    public Dict(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public Dict(Integer value, String label) {
        this.value = String.valueOf(value);
        this.label = label;
    }

    @XmlAttribute
    @Length(min = 1, max = 100)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getIntValue() {
        try {
            return Integer.valueOf(this.value);
        } catch (Exception e) {
            return 0;
        }
    }

    @XmlAttribute
    @Length(min = 1, max = 100)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Length(min = 1, max = 100)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute
    @Length(min = 0, max = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return String.format("%s-%s", value, label);
    }

    public void setAloneManagement(Integer aloneManagement) {
        this.aloneManagement = aloneManagement;
    }

    public Integer getAloneManagement() {
        return aloneManagement;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.value);
        //hash = 79 * hash + Objects.hashCode(this.city);
        //hash = 79 * hash + this.age;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dict other = (Dict) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        //if (!Objects.equals(this.city, other.city)) {
        //	return false;
        //}
        //if (this.age != other.age) {
        //	return false;
        //}
        return true;
    }

    /**
     * 复制一个Dic对象
     */
    public static Dict copyDict(Dict dict) {
        Dict result = null;
        if (dict != null) {
            result = new Dict();
            result.id = dict.id;
            result.value = dict.value;
            result.label = dict.label;
            result.type = dict.type;
            result.sort = dict.sort;
            result.description = dict.description;
        }
        return result;
    }
}
