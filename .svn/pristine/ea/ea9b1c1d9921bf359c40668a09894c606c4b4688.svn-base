package com.wolfking.jeesite.modules.md.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.kkl.kklplus.entity.md.MDProductAttributes;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.beans.Transient;
import java.util.List;
import java.util.Objects;

/**
 * 产品实体类
 * Created on 2017-04-12.
 */
public class Product extends LongIDDataEntity<Product>
{
    //产品审核状态
    public static final Integer APPROVE_FLAG_NEW = 0;//待审核
    public static final Integer APPROVE_FLAG_APPROVED = 1;//已审核

    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private ProductCategory category;
    private String name = "";
    private String brand = "";
    private String  model = "";
    private int setFlag;
    private String productIds = "";
    private int  sort;
    //拼音简称,目前用于智能回访时发送开场白指令
    private String pinYin = "";


    @JsonIgnore
    private Integer approveFlag;
    @JsonIgnore
    private Long customerId;
    @JsonIgnore
    private List<Material> materialList = Lists.newArrayList();

    private List<MDProductAttributes> productTypeSpecList = Lists.newArrayList();

    @JsonIgnore
    private Long productTypeId = 0L;

    @JsonIgnore
    private Long productTypeItemId = 0L;

    @JsonIgnore
    private String specItemIds = "";

    @JsonIgnore
    private ServiceType serviceType;//上门服务选择产品时使用

    /**
     * 基础资料列表显示配件名称多个用逗号隔开
     * **/
    @JsonIgnore
    private String materialNames = ""; //

    /**
     * 基础资料列表显示产品分类信息
     * **/
    @JsonIgnore
    private String productTypeInfo = "";

    /**
     * 基础资料列表显示产品规格
     * **/
    @JsonIgnore
    private String productSpecInfo = "";

    public Product(){
        super();
    }

    public Product(Long id){
        this.id = id;
    }

    public Product(Long id,String name){
        super(id);
        this.name = name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public long getProductCategoryId() {
        long productCategoryId = 0;
        if (this.category != null && this.category.getId() != null) {
            productCategoryId = this.category.getId();
        }
        return productCategoryId;
    }

    @NotNull(message="产品名称不能为空")
    @Length(min = 2,max = 50,message = "名称长度应为(2~50)位")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {

        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getSetFlag() {

        return setFlag;
    }

    public void setSetFlag(int setFlag) {
        this.setFlag = setFlag;
    }

    public String getProductIds() {

        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public int getSort() {

        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Integer getApproveFlag() {
        return approveFlag;
    }

    public void setApproveFlag(Integer approveFlag) {
        this.approveFlag = approveFlag;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }

    //序列化，json忽略
    @Transient
    @JsonIgnore
    public String getMaterialIds()
    {
        List<String> nameIdList = Lists.newArrayList();
        for (Material p : materialList)
        {
            nameIdList.add(p.getId().toString());
        }
        return StringUtils.join(nameIdList, ",");
    }

    @Transient
    public void setMaterialIds(String materialIds)
    {
        materialList = Lists.newArrayList();
        if (materialIds != null)
        {
            String[] ids = StringUtils.split(materialIds, ",");
            for (String pId : ids)
            {
                Material p = new Material();
                p.setId(Long.parseLong(pId));
                materialList.add(p);
            }
        }
    }

    @Override
    public String toString() {
        return "Product(id=" + this.id + ", name=" + this.name + ")";
        //return "Product(id=" + this.id + ", name=" + this.name + ", brand=" + this.brand + ", model=" + this.model + ", setFlag=" + this.setFlag + ")";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        //hash = 79 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        else if (obj instanceof Product){
            Product other = (Product) obj;
            if (this.id == null) {
                return false;
            }
            if (this.id.equals(other.getId())){
                return true;
            }
        }
        return false;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getProductTypeItemId() {
        return productTypeItemId;
    }

    public void setProductTypeItemId(Long productTypeItemId) {
        this.productTypeItemId = productTypeItemId;
    }

    public String getSpecItemIds() {
        return specItemIds;
    }

    public void setSpecItemIds(String specItemIds) {
        this.specItemIds = specItemIds;
    }

    public List<MDProductAttributes> getProductTypeSpecList() {
        return productTypeSpecList;
    }

    public void setProductTypeSpecList(List<MDProductAttributes> productTypeSpecList) {
        this.productTypeSpecList = productTypeSpecList;
    }

    public String getMaterialNames() {
        return materialNames;
    }

    public void setMaterialNames(String materialNames) {
        this.materialNames = materialNames;
    }

    public String getProductTypeInfo() {
        return productTypeInfo;
    }

    public void setProductTypeInfo(String productTypeInfo) {
        this.productTypeInfo = productTypeInfo;
    }

    public String getProductSpecInfo() {
        return productSpecInfo;
    }

    public void setProductSpecInfo(String productSpecInfo) {
        this.productSpecInfo = productSpecInfo;
    }
}
