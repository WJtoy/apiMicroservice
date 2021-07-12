package com.wolfking.jeesite.modules.sd.entity.viewModel;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.IntegerRange;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.ServicePointFinance;
import com.wolfking.jeesite.modules.sd.utils.ServicePointCrushAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 安维店突击列表信息
 * Created on 2017-04-16.
 */
@JsonAdapter(ServicePointCrushAdapter.class)
public class ServicePointCrush extends LongIDDataEntity<ServicePointCrush> {

    public ServicePointCrush(Long id){
        super(id);
    }

    public ServicePointCrush(){
        super();
    }

    private Integer seqNo;//序号

    private String servicePointNo = "";   //网点编号
    private String name= "";   //网点名称
    private String contactInfo1= ""; //联系方式(手机)
    @GsonIgnore
    private Date contractDate; //签约日期
    @GsonIgnore
    private String developer= "";  //开发人员
    private Area area;
    private String address= "";  //详细地址=area.fullName + subAddress
    @GsonIgnore
    private String subAddress=""; //具体的地址，不包含省市区
    @GsonIgnore
    private int grade = 0; //评价分数
    @GsonIgnore
    private Integer signFlag = -1;  //是否签约
    @GsonIgnore
    private int  orderCount =0; //完成订单数量
    @GsonIgnore
    private int  planCount=0;  //派单数
    @GsonIgnore
    private int  breakCount=0;  //违约单数
    @GsonIgnore
    private double longitude=0.0;  //经度
    @GsonIgnore
    private double latitude=0.0;    //纬度
    @GsonIgnore
    private String qq;          //qq

    @GsonIgnore
    private int  shortMessageFlag = 1; //是否接收短信通知 1:接收
    @GsonIgnore
    private int autoCompleteOrder = 0; //是否自动完工,0:否
    @GsonIgnore
    private int subEngineerCount = 0;//网点子帐号数(用于APP中) 2017/11/27
    @GsonIgnore
    private int property;    //公司性质
    @GsonIgnore
    private int scale;       //规模
    @GsonIgnore
    private String description; //简介
    @GsonIgnore
    private Engineer primary; //网点主账号
    @GsonIgnore
    private ServicePointFinance finance = new ServicePointFinance();//安维商财务信息表

    @GsonIgnore
    private List<Area> areas = Lists.newArrayList();//区域
    @GsonIgnore
    private String serviceAreas = ""; //网点的服务区域列表字符串


    private String paymentType;

    private String appFlag;

    private String master;

    @GsonIgnore
    private String areaIds;
    @GsonIgnore
    private String orderBy;
    @GsonIgnore
    private IntegerRange levelRange = null;

    private String planRemarks;//派单说明

    private String crushRemarks;//突击说明

    @Length(min = 1,max = 14,message = "网点号不能为空，且长度不能超过20")
    public String getServicePointNo() {
        return servicePointNo;
    }

    public void setServicePointNo(String servicePointNo) {
        this.servicePointNo = servicePointNo;
    }

    @Length(min = 1,max = 50,message = "网点名称不能为空，且长度不能超过50")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 1,max = 20,message = "联系方式1不能为空，且长度不能超过20")
    public String getContactInfo1() {
        return contactInfo1;
    }

    public void setContactInfo1(String contactInfo1) {
        this.contactInfo1 = contactInfo1;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    @Length(max = 20,message = "开发人员长度不能超过20")
    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    @Length(min = 0,max = 100,message = "详细地址不能为空，且长度不能超过100")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Min(value = 0,message = "评价分数不能小于0")
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Min(value = 0,message = "完成订单数不能小于0")
    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    @Min(value = 0,message = "派单数不能小于0")
    public int getPlanCount() {
        return planCount;
    }

    public void setPlanCount(int planCount) {
        this.planCount = planCount;
    }

    @Min(value = 0,message = "违约单数不能小于0")
    public int getBreakCount() {
        return breakCount;
    }

    public void setBreakCount(int breakCount) {
        this.breakCount = breakCount;
    }


    @Length(max = 15,message = "QQ号长度不能超过15")
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
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

    @NotNull(message = "主帐号不能为空")
    public Engineer getPrimary() {
        return primary;
    }

    public void setPrimary(Engineer primary) {
        this.primary = primary;
    }


    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }


    public String getAreaIds() {
        if(this.areas==null || this.areas.size()==0) {
            return areaIds;
        }else{
            return areas.stream()
                    .map(t->t.getName())
                    .collect(Collectors.joining(","));
        }
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }


    public String getServiceAreas() {
        return serviceAreas;
    }

    public void setServiceAreas(String serviceAreas) {
        this.serviceAreas = serviceAreas;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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
        final ServicePointCrush other = (ServicePointCrush) obj;
        if(!Objects.equals(this.id,other.id)){
            return false;
        }
        return true;
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


    public String getPlanRemarks() {
        return planRemarks;
    }

    public void setPlanRemarks(String planRemarks) {
        this.planRemarks = planRemarks;
    }

    public String getCrushRemarks() {
        return crushRemarks;
    }

    public void setCrushRemarks(String crushRemarks) {
        this.crushRemarks = crushRemarks;
    }

    public ServicePointFinance getFinance() {
        return finance;
    }

    public void setFinance(ServicePointFinance finance) {
        this.finance = finance;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getAppFlag() {
        return appFlag;
    }

    public void setAppFlag(String appFlag) {
        this.appFlag = appFlag;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }
}
