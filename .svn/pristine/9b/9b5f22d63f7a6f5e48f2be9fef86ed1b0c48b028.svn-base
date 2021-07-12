package com.wolfking.jeesite.modules.md.entity;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.utils.ProductSimplexAdapter;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品完成图片模型
 * @autor Ryan Lu
 * @date 2018/8/17 下午10:04
 */
@Data
public class ProductCompletePic extends LongIDDataEntity<ProductCompletePic> {
    public static String DICTTYPE = "CompletePicType";
    // Fields
    //产品
    @JsonAdapter(ProductSimplexAdapter.class)
    private Product product;
    //图片类型设定(json)
    private String jsonInfo;

    @GsonIgnore
    private Customer customer;

    private Integer barcodeMustFlag;

    //辅助属性
    //图片类型设定(list,json转换而来)
    //用于前端及缓存
    private List<ProductCompletePicItem> items;

    public ProductCompletePic() {}

    public ProductCompletePic(Long id){
        this.id=id;
    }

    public ProductCompletePic(Long id,Long productId){
        this.id=id;
        this.product = new Product(productId);
    }

    public ProductCompletePic(Long id, Long productId,Long customerId){
        this.id=id;
        this.product = new Product(productId);
        this.customer = new Customer(customerId);
    }

    @Override
    public boolean equals(Object obj) {
        boolean eqResult = super.equals(obj);
        if(!eqResult){
            return eqResult;
        }
        ProductCompletePic that = (ProductCompletePic) obj;
        if(null == this.getProduct() || null == this.getProduct().getId() || 0l == this.getProduct().getId().longValue()
                || null == that.getProduct() || null == that.getProduct().getId() || 0l == that.getProduct().getId().longValue()){
           return false;
        }
        if(that.getProduct().getId().longValue() != this.getProduct().getId().longValue()){
            return false;
        }
        if(this.getJsonInfo().equalsIgnoreCase(that.getJsonInfo())){
            return true;
        }else{
            return false;
        }
    }

    //json to list
    public void parseItemsFromJson(){
        if(StringUtils.isBlank(this.jsonInfo)){
            this.items = Lists.newArrayList();
        }else{
            ProductCompletePicItem[] arrItems = GsonUtils.getInstance().fromJson(this.jsonInfo,ProductCompletePicItem[].class);
            if(arrItems == null || arrItems.length == 0){
                this.items = Lists.newArrayList();
            }else{
                this.items = new ArrayList<>(Arrays.asList(arrItems));
            }
        }
    }

    //解析json
    public void toJsonInfo(){
        if(this.items == null || this.items.isEmpty()){
            this.jsonInfo = "";
        }else{
            //排除code为空
            this.items=this.items.stream().filter(t->StringUtils.isNotBlank(t.getPictureCode())).collect(Collectors.toList());
            this.jsonInfo = GsonUtils.getInstance().toGson(this.items);
        }
    }

}
