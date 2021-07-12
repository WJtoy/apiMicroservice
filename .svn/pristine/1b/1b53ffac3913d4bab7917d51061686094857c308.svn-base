package com.wolfking.jeesite.modules.md.entity;

import com.google.common.collect.Ordering;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.adapter.DictSimpleAdapter;

import java.util.Comparator;
import java.util.Objects;

/**
 * 客户实体类
 * Created on 2017-04-12.
 */
public class ServiceType extends LongIDDataEntity<ServiceType> {
    public static String WARRANTY_STATUS_IW = "IW"; //在质保期内
    public static String WARRANTY_STATUS_OOT = "OOT"; //在质保期外

    public static int OPEN_FOR_CUSTOMER_YES = 1; // 服务类型客户可见
    public static final long INSTALLATION_SERVICE_TYPE_ID = 1;

    private static final long serialVersionUID = 1L;

    private String code;            //编码
    private String name;            //名称
    private int openForCustomer;
    @JsonAdapter(DictSimpleAdapter.class)
    private Dict warrantyStatus; //质保状态
    private double price;
    private double discountPrice;
    private double blockedPrice;
    private double engineerPrice;
    private double engineerDiscountPrice;
    private int sort;
    private int unit;

    /**
     * 订单类型   1:安装单   2:维修单 0:检测 (数据字典:order_service_type)
     * */
    private Integer orderServiceType = 0;
    @GsonIgnore
    private Dict orderServiceTypeDict;

    /**
     * 自动客评标记(0:不自动客评,1:自动客评)
     * */
    private Integer autoGradeFlag = 0;

    /**
     * 自动对账标记 (0:不自动对账,1:自动对账)
     * */
    private Integer autoChargeFlag = 0;

    /**
     * 是否需要关联故障类别
     */
    private Integer relateErrorTypeFlag = 0;

    /**
     * 扣点开关,0-不扣点,1-扣点
     */
    private Integer taxFeeFlag = 0;
    /**
     * 平台信息费开关,0-不收平台信息费,1-收平台信息费
     */
    private Integer infoFeeFlag = 0;

    public ServiceType() {
        super();
    }

    public ServiceType(Long id) {
        super(id);
    }

    public ServiceType(Long id, String code, String name) {
        super(id);
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dict getWarrantyStatus() {
        return warrantyStatus;
    }

    public void setWarrantyStatus(Dict warrantyStatus) {
        this.warrantyStatus = warrantyStatus;
    }

    public int getOpenForCustomer() {
        return openForCustomer;
    }

    public void setOpenForCustomer(int openForCustomer) {
        this.openForCustomer = openForCustomer;
    }

    public double getPrice() {

        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountPrice() {

        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getBlockedPrice() {

        return blockedPrice;
    }

    public void setBlockedPrice(double blockedPrice) {
        this.blockedPrice = blockedPrice;
    }

    public double getEngineerPrice() {

        return engineerPrice;
    }

    public void setEngineerPrice(double engineerPrice) {
        this.engineerPrice = engineerPrice;
    }

    public double getEngineerDiscountPrice() {

        return engineerDiscountPrice;
    }

    public void setEngineerDiscountPrice(double engineerDiscountPrice) {
        this.engineerDiscountPrice = engineerDiscountPrice;
    }

    public int getSort() {

        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getUnit() {

        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public final static Ordering<ServiceType> ServcieTypeOrdering = Ordering.from(new Comparator<ServiceType>() {
        @Override
        public int compare(ServiceType o1, ServiceType o2) {
            if (o1.getId().longValue() == o2.getId().longValue()) {
                return 0;
            }
            return o1.getId().longValue() > o2.getId().longValue() ? 1 : -1;
        }
    });

    public Integer getOrderServiceType() {
        return orderServiceType;
    }

    public void setOrderServiceType(Integer orderServiceType) {
        this.orderServiceType = orderServiceType;
    }

    public Integer getAutoGradeFlag() {
        return autoGradeFlag;
    }

    public void setAutoGradeFlag(Integer autoGradeFlag) {
        this.autoGradeFlag = autoGradeFlag;
    }

    public Integer getAutoChargeFlag() {
        return autoChargeFlag;
    }

    public void setAutoChargeFlag(Integer autoChargeFlag) {
        this.autoChargeFlag = autoChargeFlag;
    }

    public Integer getRelateErrorTypeFlag() {
        return relateErrorTypeFlag;
    }

    public void setRelateErrorTypeFlag(Integer relateErrorTypeFlag) {
        this.relateErrorTypeFlag = relateErrorTypeFlag;
    }

    public Dict getOrderServiceTypeDict() {
        return orderServiceTypeDict;
    }

    public void setOrderServiceTypeDict(Dict orderServiceTypeDict) {
        this.orderServiceTypeDict = orderServiceTypeDict;
    }

    public Integer getTaxFeeFlag() {
        return taxFeeFlag;
    }

    public void setTaxFeeFlag(Integer taxFeeFlag) {
        this.taxFeeFlag = taxFeeFlag;
    }

    public Integer getInfoFeeFlag() {
        return infoFeeFlag;
    }

    public void setInfoFeeFlag(Integer infoFeeFlag) {
        this.infoFeeFlag = infoFeeFlag;
    }

    @Override
    public String toString() {
        return String.format("%d(%s,%s)", id, code, name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.code);
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
        final ServiceType other = (ServiceType) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
//        if (!Objects.equals(this.code, other.code)) {
//            return false;
//        }
        return true;
    }
}
