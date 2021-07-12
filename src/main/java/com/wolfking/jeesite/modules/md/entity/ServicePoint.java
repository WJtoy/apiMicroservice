package com.wolfking.jeesite.modules.md.entity;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.IntegerRange;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.utils.ServicePointAdapter;
import com.wolfking.jeesite.modules.md.utils.ServicePointPrimaryAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 安维店信息实体类
 * Created on 2017-04-16.
 */
@JsonAdapter(ServicePointAdapter.class)
public class ServicePoint extends LongIDDataEntity<ServicePoint> {

    public static final int SERVICE_POINT_ORDER_LIST_TYPE_WAITING_APPOINTMENT = 10;//待预约订单列表
    public static final int SERVICE_POINT_ORDER_LIST_TYPE_PROCESSING = 20;//处理中订单列表
    public static final int SERVICE_POINT_ORDER_LIST_TYPE_APPOINTED = 30;//已预约订单列表
    public static final int SERVICE_POINT_ORDER_LIST_TYPE_WAITING_ACCESSORY = 40;//等配件订单列表
    public static final int SERVICE_POINT_ORDER_LIST_TYPE_OTHER_PENDING = 50;//其他停滞订单列表(不包括等配件、已预约)

    public static final int APP_INSURANCE_FLAG_NO_READ = 0;//APP未阅读保险条款
    public static final int APP_INSURANCE_FLAG_AGREE = 10;//APP已阅读且同意保险条款
    public static final int APP_INSURANCE_FLAG_DISAGREE = 20;//APP已阅读且不同意保险条款

    public static final int INSURANCE_FLAG_DISABLED = 0;//不开启保险计算
    public static final int INSURANCE_FLAG_ENABLED = 1;//开启保险计算

    public static final int AUTO_PLAN_FLAG_DISABLED = 0; //不能自动派单
    public static final int AUTO_PLAN_FLAG_ENABLED = 1;  //开启自动派单

    public static final int TIME_LINESS_FLAG_DISABLED = 0; //停用时效奖励
    public static final int TIME_LINESS_FLAG_ENABLED = 1; //启用时效奖励

    public static final int CUSTOMIZE_PRICE_FLAG_ENABLED  = 1; //启用自定义价格
    public static final int CUSTOMIZE_PRICE_FLAG_DISABLED = 0; //停用自定义价格

    public ServicePoint(Long id) {
        super(id);
    }

    public ServicePoint() {
        super();
    }

    private String servicePointNo = "";   //网点编号
    private String name = "";   //网点名称
    private String contactInfo1 = ""; //联系方式(手机)
    private String contactInfo2 = ""; //联系方式(电话)
    //    private String contactInfo3; //联系方式
    private Date contractDate; //签约日期
    private String developer = "";  //开发人员
    private Area area;
    private String address = "";  //详细地址=area.fullName + subAddress
    private String subAddress = ""; //具体的地址，不包含省市区
    private int grade = 0; //评价分数
    private Dict level = new Dict("0", "");  //等级
    private Integer signFlag = -1;  //是否签约
    private int unfinishedOrderCount =0; //未完成工单数量
    private int orderCount = 0; //完成订单数量
    private int planCount = 0;  //派单数
    private int breakCount = 0;  //违约单数
    private double longitude = 0.0;  //经度
    private double latitude = 0.0;    //纬度
    private String qq;          //qq
    private String attachment1;  //附件
    private String attachment2; //附件
    private String attachment3; //附件
    private String attachment4; //附件
    private int useDefaultPrice = 0; //使用默认价，0:自定义价, >0:数据字典维护价格
    private int shortMessageFlag = 0; //是否接收短信通知 1:接收
    private int autoCompleteOrder = 0; //是否自动完工,0:否
    private int resetPrice = 0; //是否重置价格
    private int subEngineerCount = 0;//网点子帐号数(用于APP中) 2017/11/27
    private int property;    //公司性质
    private int scale;       //规模
    private String description; //简介
    private ServicePointFinance finance = new ServicePointFinance();//安维商财务信息表
    @JsonAdapter(ServicePointPrimaryAdapter.class)
    private Engineer primary; //网点主账号
    @GsonIgnore
    private List<Area> areas = Lists.newArrayList();//区域
    @GsonIgnore
    private List<Product> products = Lists.newArrayList();//产品
    @GsonIgnore
    private List<Long> productCategoryIds = Lists.newArrayList();
    @GsonIgnore
    private List<ProductCategory> productCategories = Lists.newArrayList();
    @GsonIgnore
    private String serviceAreas = ""; //网点的服务区域列表字符串

    //结算方式
    private Dict paymentType;
    //开户行
    private Dict bank;
    //账号
    private String bankNo = "";
    //开户人
    private String bankOwner = "";
    //付款失败描述
    private Dict bankIssue;
    //开票标记，0:不需要开票，1:需要开票
    private Integer invoiceFlag = 0;
    //是否扣点
    private Integer discountFlag = 0;

    //辅助字段，用于查询条件
    @GsonIgnore
    private Integer productId = 0;
    @GsonIgnore
    private Integer productCategory = 0;
    @GsonIgnore
    private String productIds;
    @GsonIgnore
    private String areaIds;
    @GsonIgnore
    private String orderBy;
    @GsonIgnore
    private IntegerRange levelRange = null;
    @GsonIgnore
    private IntegerRange statusRange = null;
    //是否是第一次查询
    private Integer firstSearch = 1;
    @GsonIgnore
    private Area subArea;   // 乡/街道
    @GsonIgnore
    private Long productCategoryId = 0L;//服务品类

    private String planRemark;//派单的时候客服的备注
    private String planRemarks;//派单的时候客服的备注 历史记录JSON
    private Integer forTmall = 0; //是否是淘宝服务网点，B2B
    private Integer insuranceFlag = INSURANCE_FLAG_DISABLED;//是否开启保险计算Flag;0 不开启 1 开启
    private Integer appInsuranceFlag = APP_INSURANCE_FLAG_NO_READ;//APP是否同意保险条款：0 - APP未阅读保险条款、 10 - APP已阅读且同意保险条款、 20 - APP已阅读且不同意保险条款
    private Integer timeLinessFlag = 0;//网点时效开关//2018-06-25  时效开关默认为开 //2018-7-31 时效开关默认为关闭
    private Integer autoPlanFlag = 0; //自动派单开关,0-人工派单,1:自动派单 //2019-4-9
    private Integer customizePriceFlag =0; //0-使用参考价格,1-使用自定义价格
    private Integer praiseFeeFlag = 0; // 好评费开关 //2020-3-28
    private Integer customerTimeLinessFlag = 1; //客户时效开关；0-停用，1-开启  //2020-5-22

    private Dict status = new Dict("0", ""); //网点状态

    @GsonIgnore
    private List<ServicePointStation> servicePointStations;  //服务点列表 //2019-4-25
    private Integer capacity =0;    //网点容量   // 2019-4-25

    private String bankOwnerIdNo = ""; //开户预留身份证号
    private String bankOwnerPhone = ""; //开户预留手机号
    private Integer needAuthFlag = 0;           //是否需要实名认证标识  1需要，0不需要
    private Integer completeAuthFlag =0;       //是否完成实名认证标识  1完成，0-未完成
    private Integer paymentChannel;         //结算途径
    private Integer autoPaymentFlag =0;        //自动结算标识,1自动结算,0手动结算
    private Integer planContactFlag;           //派单联系人，0-负责人，1-师傅

    /**
     * 网点分级
     * */
    private Integer degree;
    private Integer appFlag =0 ;   //网点手机接单  //add on 2020-6-11

    //private String noBlackList;//时候显示黑名单（等级为6:黑名单 7:取消合作 8:停用）

    @Length(min = 1, max = 14, message = "网点号不能为空，且长度不能超过20")
    public String getServicePointNo() {
        return servicePointNo;
    }

    public void setServicePointNo(String servicePointNo) {
        this.servicePointNo = servicePointNo;
    }

    @Length(min = 1, max = 50, message = "网点名称不能为空，且长度不能超过50")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 1, max = 20, message = "联系方式1不能为空，且长度不能超过20")
    public String getContactInfo1() {
        return contactInfo1;
    }

    public void setContactInfo1(String contactInfo1) {
        this.contactInfo1 = contactInfo1;
    }

    @Length(max = 20, message = "联系方式2长度不能超过20")
    public String getContactInfo2() {
        return contactInfo2;
    }

    public void setContactInfo2(String contactInfo2) {
        this.contactInfo2 = contactInfo2;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    @Length(max = 20, message = "开发人员长度不能超过20")
    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    @Length(min = 0, max = 100, message = "详细地址长度不能超过100")
    @NotEmpty(message = "详细地址不能为空")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Min(value = 0, message = "评价分数不能小于0")
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @NotNull(message = "等级不能为空")
    public Dict getLevel() {
        return level;
    }

    public void setLevel(Dict level) {
        this.level = level;
    }

    public Integer getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    @Min(value = 0, message = "完成订单数不能小于0")
    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    @Min(value = 0, message = "派单数不能小于0")
    public int getPlanCount() {
        return planCount;
    }

    public void setPlanCount(int planCount) {
        this.planCount = planCount;
    }

    @Min(value = 0, message = "违约单数不能小于0")
    public int getBreakCount() {
        return breakCount;
    }

    public void setBreakCount(int breakCount) {
        this.breakCount = breakCount;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Length(max = 15, message = "QQ号长度不能超过15")
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    @Length(max = 100, message = "合同附件路径长度不能超过100")
    public String getAttachment1() {
        return attachment1;
    }

    public void setAttachment1(String attachment1) {
        this.attachment1 = attachment1;
    }

    @Length(max = 100, message = "个人身份证附件路径长度不能超过100")
    public String getAttachment2() {
        return attachment2;
    }

    public void setAttachment2(String attachment2) {
        this.attachment2 = attachment2;
    }

    @Length(max = 100, message = "其他证件1附件路径长度不能超过100")
    public String getAttachment3() {
        return attachment3;
    }

    public void setAttachment3(String attachment3) {
        this.attachment3 = attachment3;
    }

    @Length(max = 100, message = "其他证件2附件路径长度不能超过100")
    public String getAttachment4() {
        return attachment4;
    }

    public void setAttachment4(String attachment4) {
        this.attachment4 = attachment4;
    }

    //    @Range(min = 0,max = 1,message = "使用默认价不能超出0-1范围")
    public int getUseDefaultPrice() {
        return useDefaultPrice;
    }

    public void setUseDefaultPrice(int useDefaultPrice) {
        this.useDefaultPrice = useDefaultPrice;
    }

    //    @NotEmpty(message = "详细地址不能为空")
    @Length(max = 100, message = "详细地址长度不能超过100")
    public String getSubAddress() {
        return subAddress;
    }

    public void setSubAddress(String subAddress) {
        this.subAddress = subAddress;
    }

    public int getProperty() {
        return property;
    }

    public void setProperty(int property) {
        this.property = property;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServicePointFinance getFinance() {
        return finance;
    }

    public void setFinance(ServicePointFinance finance) {
        this.finance = finance;
    }

    @NotNull(message = "主帐号不能为空")
    public Engineer getPrimary() {
        return primary;
    }

    public void setPrimary(Engineer primary) {
        this.primary = primary;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public String getProductIds() {
        if (this.products == null || this.products.size() == 0) {
            return productIds;
        } else {
            return products.stream()
                    .map(t -> t.getName())
                    .collect(Collectors.joining(","));
        }
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public List<Long> getProductCategoryIds() {
        return productCategoryIds;
    }

    public void setProductCategoryIds(List<Long> productCategoryIds) {
        this.productCategoryIds = productCategoryIds;
    }

    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public String getAreaIds() {
        if (this.areas == null || this.areas.size() == 0) {
            return areaIds;
        } else {
            return areas.stream()
                    .map(t -> t.getName())
                    .collect(Collectors.joining(","));
        }
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getServiceAreas() {
        return serviceAreas;
    }

    public void setServiceAreas(String serviceAreas) {
        this.serviceAreas = serviceAreas;
    }

    public String getProductCategoryNames() {
        String result = "";
        if (this.productCategories != null && !this.productCategories.isEmpty()) {
            List<String> productCategoryNames = this.productCategories.stream().map(ProductCategory::getName).collect(Collectors.toList());
            result = StringUtils.join(productCategoryNames, ",");
        }
        return result;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPraiseFeeFlag() {
        return praiseFeeFlag;
    }

    public void setPraiseFeeFlag(Integer praiseFeeFlag) {
        this.praiseFeeFlag = praiseFeeFlag;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", id, name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.servicePointNo);
        hash = 79 * hash + Objects.hashCode(this.name);
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
        final ServicePoint other = (ServicePoint) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        /*
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.servicePointNo, other.servicePointNo)) {
            return false;
        }*/
        return true;
    }

    public IntegerRange getLevelRange() {
        return levelRange;
    }

    public void setLevelRange(IntegerRange levelRange) {
        this.levelRange = levelRange;
    }

    public IntegerRange getStatusRange() {
        return statusRange;
    }

    public void setStatusRange(IntegerRange statusRange) {
        this.statusRange = statusRange;
    }

    public int getShortMessageFlag() {
        return shortMessageFlag;
    }

    public void setShortMessageFlag(int shortMessageFlag) {
        this.shortMessageFlag = shortMessageFlag;
    }

    public int getAutoCompleteOrder() {
        return autoCompleteOrder;
    }

    public void setAutoCompleteOrder(int autoCompleteOrder) {
        this.autoCompleteOrder = autoCompleteOrder;
    }

    public int getResetPrice() {
        return resetPrice;
    }

    public void setResetPrice(int resetPrice) {
        this.resetPrice = resetPrice;
    }

    public Integer getFirstSearch() {
        return firstSearch;
    }

    public void setFirstSearch(Integer firstSearch) {
        this.firstSearch = firstSearch;
    }

    public int getSubEngineerCount() {
        return subEngineerCount;
    }

    public void setSubEngineerCount(int subEngineerCount) {
        this.subEngineerCount = subEngineerCount;
    }

    public String getPlanRemark() {
        return planRemark;
    }

    public void setPlanRemark(String planRemark) {
        this.planRemark = planRemark;
    }

    public String getPlanRemarks() {
        return planRemarks;
    }

    public void setPlanRemarks(String planRemarks) {
        this.planRemarks = planRemarks;
    }

    public Integer getInsuranceFlag() {
        return insuranceFlag;
    }

    public void setInsuranceFlag(Integer insuranceFlag) {
        this.insuranceFlag = insuranceFlag;
    }

    public Integer getAppInsuranceFlag() {
        return appInsuranceFlag;
    }

    public void setAppInsuranceFlag(Integer appInsuranceFlag) {
        this.appInsuranceFlag = appInsuranceFlag;
    }

    public Integer getTimeLinessFlag() {
        return timeLinessFlag;
    }

    public void setTimeLinessFlag(Integer timeLinessFlag) {
        this.timeLinessFlag = timeLinessFlag;
    }

    public Integer getForTmall() {
        return forTmall;
    }

    public void setForTmall(Integer forTmall) {
        this.forTmall = forTmall;
    }

    public Dict getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Dict paymentType) {
        this.paymentType = paymentType;
    }

    public Dict getBank() {
        return bank;
    }

    public void setBank(Dict bank) {
        this.bank = bank;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankOwner() {
        return bankOwner;
    }

    public void setBankOwner(String bankOwner) {
        this.bankOwner = bankOwner;
    }

    public Integer getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(Integer invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    public Dict getBankIssue() {
        return bankIssue;
    }

    public void setBankIssue(Dict bankIssue) {
        this.bankIssue = bankIssue;
    }

    public Integer getDiscountFlag() {
        return discountFlag;
    }

    public void setDiscountFlag(Integer discountFlag) {
        this.discountFlag = discountFlag;
    }

    public Dict getStatus() {
        return status;
    }

    public void setStatus(Dict status) {
        this.status = status;
    }

    public int getStatusValue() {
        return status != null ? StringUtils.toInteger(status.getValue()) : 0;
    }

    public Integer getAutoPlanFlag() {
        return autoPlanFlag;
    }

    public void setAutoPlanFlag(Integer autoPlanFlag) {
        this.autoPlanFlag = autoPlanFlag;
    }

    public List<ServicePointStation> getServicePointStations() {
        return servicePointStations;
    }

    public void setServicePointStations(List<ServicePointStation> servicePointStations) {
        this.servicePointStations = servicePointStations;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Area getSubArea() {
        return subArea;
    }

    public void setSubArea(Area subArea) {
        this.subArea = subArea;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Integer getCustomizePriceFlag() {
        return customizePriceFlag;
    }

    public void setCustomizePriceFlag(Integer customizePriceFlag) {
        this.customizePriceFlag = customizePriceFlag;
    }

    public Integer getCustomerTimeLinessFlag() {
        return customerTimeLinessFlag;
    }

    public void setCustomerTimeLinessFlag(Integer customerTimeLinessFlag) {
        this.customerTimeLinessFlag = customerTimeLinessFlag;
    }

    /**
     * 网点是否允许接派单
     */
    public boolean canGrabOrPlanOrder() {
        boolean flag = false;
        int statusValue = getStatusValue();
        if (statusValue == ServicePointStatus.NORMAL.getValue()) {
            flag = true;
        }
        return flag;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Integer getAppFlag() {
        return appFlag;
    }

    public void setAppFlag(Integer appFlag) {
        this.appFlag = appFlag;
    }

    public String getBankOwnerIdNo() {
        return bankOwnerIdNo;
    }

    public void setBankOwnerIdNo(String bankOwnerIdNo) {
        this.bankOwnerIdNo = bankOwnerIdNo;
    }

    public String getBankOwnerPhone() {
        return bankOwnerPhone;
    }

    public void setBankOwnerPhone(String bankOwnerPhone) {
        this.bankOwnerPhone = bankOwnerPhone;
    }

    public Integer getPlanContactFlag() {
        return planContactFlag;
    }

    public void setPlanContactFlag(Integer planContactFlag) {
        this.planContactFlag = planContactFlag;
    }

    public Integer getNeedAuthFlag() {
        return needAuthFlag;
    }

    public void setNeedAuthFlag(Integer needAuthFlag) {
        this.needAuthFlag = needAuthFlag;
    }

    public Integer getCompleteAuthFlag() {
        return completeAuthFlag;
    }

    public void setCompleteAuthFlag(Integer completeAuthFlag) {
        this.completeAuthFlag = completeAuthFlag;
    }

    public Integer getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(Integer paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public Integer getAutoPaymentFlag() {
        return autoPaymentFlag;
    }

    public void setAutoPaymentFlag(Integer autoPaymentFlag) {
        this.autoPaymentFlag = autoPaymentFlag;
    }

    public int getUnfinishedOrderCount() {
        return unfinishedOrderCount;
    }

    public void setUnfinishedOrderCount(int unfinishedOrderCount) {
        this.unfinishedOrderCount = unfinishedOrderCount;
    }
}
