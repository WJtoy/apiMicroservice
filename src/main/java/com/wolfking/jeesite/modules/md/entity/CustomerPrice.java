package com.wolfking.jeesite.modules.md.entity;

import com.google.common.collect.Ordering;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.utils.CustomerPriceAdapter;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 客户实体类
 * Created on 2017-04-12.
 */
@JsonAdapter(CustomerPriceAdapter.class)
public class CustomerPrice extends LongIDDataEntity<CustomerPrice>
{
    private static final long serialVersionUID = 1L;

    public CustomerPrice(){super();}

    public CustomerPrice(Long id){
        super(id);
    }

    public final static Ordering<CustomerPrice> CustomerPriceOrdering = Ordering.from(new Comparator<CustomerPrice>() {
        @Override
        public int compare(CustomerPrice o1, CustomerPrice o2) {
            if(o1.getServiceType().getId().longValue() == o2.getServiceType().getId().longValue()){
                return 0;
            }
            return o1.getServiceType().getId().longValue() > o2.getServiceType().getId().longValue() ? 1 : -1;
        }
    });

    private Customer customer;
    private Product product;
    private ServiceType serviceType;
    private double price = 0;
    private double discountPrice = 0;
    private double blockedPrice = 0;
    private Dict unit = new Dict("RMB","人民币");  //价格单位

    //参考价格，辅助用
    private Dict priceType; //参考价格类型
    private double referPrice = 0;
    private double referDiscountPrice = 0;
    private Integer flag = 0;//价格查询列表用标志位,0-已维护价格 1-未维护价格  2-无参考价格，不需维护
    private List<HashMap<String, Object>> productPriceList;
    //产品分类，辅助用
    private ProductCategory productCategory;
    //是否是第一次查询
    private Integer firstSearch = 1;   //add on 2019-7-26
    // 产品id列表,辅助用
    private List<Long> productIds;     //add on 2019-8-20

    @NotNull(message = "客户不能为空")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @NotNull(message = "产品不能为空")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @NotNull(message = "服务类型不能为空")
    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    @DecimalMin(value = "0.0",message = "标准价不能小于0")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @DecimalMin(value = "0.0",message = "折扣价不能小于0")
    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    @DecimalMin(value = "0.0",message = "冻结奸恶不能小于0")
    public double getBlockedPrice() {
        return blockedPrice;
    }

    public void setBlockedPrice(double blockedPrice) {
        this.blockedPrice = blockedPrice;
    }

    @NotNull(message = "币别单位不能为空")
    public Dict getUnit() {

        return unit;
    }

    public void setUnit(Dict unit) {
        this.unit = unit;
    }

    //以下复写方法，用于做list的交集/并集/差集处理
    @Override
    public String toString() {
        return String.format("%d", id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.serviceType);
        hash = 79 * hash + Objects.hashCode(this.product);
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
        final CustomerPrice other = (CustomerPrice) obj;
//        if (!Objects.equals(this.product, other.product)) {
//            return false;
//        }
        if (!Objects.equals(this.serviceType, other.serviceType)) {
            return false;
        }
        return true;
    }

    public Dict getPriceType() {
        return priceType;
    }

    public void setPriceType(Dict priceType) {
        this.priceType = priceType;
    }

    public double getReferPrice() {
        return referPrice;
    }

    public void setReferPrice(double referPrice) {
        this.referPrice = referPrice;
    }

    public double getReferDiscountPrice() {
        return referDiscountPrice;
    }

    public void setReferDiscountPrice(double referDiscountPrice) {
        this.referDiscountPrice = referDiscountPrice;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public List<HashMap<String, Object>> getProductPriceList() {
        return productPriceList;
    }

    public void setProductPriceList(List<HashMap<String, Object>> productPriceList) {
        this.productPriceList = productPriceList;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getFirstSearch() {
        return firstSearch;
    }

    public void setFirstSearch(Integer firstSearch) {
        this.firstSearch = firstSearch;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
