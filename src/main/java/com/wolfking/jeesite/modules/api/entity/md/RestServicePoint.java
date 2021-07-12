package com.wolfking.jeesite.modules.api.entity.md;

import com.kkl.kklplus.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 网点基础类
 */
public class RestServicePoint {
    private String no;
    private String name;
    private String address;
    private String phone1;
    private String phone2;
    private int orderCount;
    private int planCount;
    private int breakCount;
    private double longitude;
    private double latitude;
    private String contractImage;
    private String idCardImage;
    private String otherImage1;
    private String otherImage2;
    private String primaryName;
    private RestDict type;
    private Integer subEngineerCount;
    private RestServicePointFinance finance;
    private List<RestArea> areaList;
    private String areaNames;
    private List<RestProduct> productList;
    private String productNames;
    private List<RestEngineer> engineerList;

    /**
     * 网点收货地址（2020-3-24新增属性）
     */
    @Getter
    @Setter
    private RestServicePointConsigneeAddress consigneeAddress = new RestServicePointConsigneeAddress();

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getPlanCount() {
        return planCount;
    }

    public void setPlanCount(int planCount) {
        this.planCount = planCount;
    }

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

    public String getContractImage() {
        return contractImage;
    }

    public void setContractImage(String contractImage) {
        this.contractImage = contractImage;
    }

    public String getIdCardImage() {
        return idCardImage;
    }

    public void setIdCardImage(String idCardImage) {
        this.idCardImage = idCardImage;
    }

    public String getOtherImage1() {
        return otherImage1;
    }

    public void setOtherImage1(String otherImage1) {
        this.otherImage1 = otherImage1;
    }

    public String getOtherImage2() {
        return otherImage2;
    }

    public void setOtherImage2(String otherImage2) {
        this.otherImage2 = otherImage2;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public RestDict getType() {
        return type;
    }

    public void setType(RestDict type) {
        this.type = type;
    }

    public Integer getSubEngineerCount() {
        return subEngineerCount;
    }

    public void setSubEngineerCount(Integer subEngineerCount) {
        this.subEngineerCount = subEngineerCount;
    }

    public RestServicePointFinance getFinance() {
        return finance;
    }

    public void setFinance(RestServicePointFinance finance) {
        this.finance = finance;
    }

    public List<RestArea> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<RestArea> areaList) {
        this.areaList = areaList;
        List<String> nameList = areaList.stream().map(t->t.getName()).collect(Collectors.toList());
        this.areaNames =  StringUtils.join(nameList,"、");
    }

    public String getAreaNames() {
        return areaNames;
    }

    public void setAreaNames(String areaNames) {
        this.areaNames = areaNames;
    }

    public List<RestProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<RestProduct> productList) {
        this.productList = productList;
        List<String> nameList = productList.stream().map(t->t.getName()).collect(Collectors.toList());
        this.productNames =  StringUtils.join(nameList,"、");
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public List<RestEngineer> getEngineerList() {
        return engineerList;
    }

    public void setEngineerList(List<RestEngineer> engineerList) {
        this.engineerList = engineerList;
    }
}
