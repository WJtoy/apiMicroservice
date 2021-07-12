package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.utils.ProductCategoryAdapter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 客户实体类
 * Created on 2017-04-12.
 */
@JsonAdapter(ProductCategoryAdapter.class)
public class ProductCategory extends LongIDDataEntity<ProductCategory>
{
    private static final long serialVersionUID = 1L;
    private String code = "";            //编码
    private String name = "";            //名称
    private String groupCategoryName = "";
    private Integer  sort;
    private Integer vipFlag;
    private Integer groupCategory;
    private Integer appCompleteFlag = 0;
    private Integer autoGradeFlag = 1;

    public ProductCategory(){
    }

    public ProductCategory(Long id){
        this.id = id;
    }

    @NotNull(message="编码不能为空")
    @Length(min = 2,max = 10,message = "编码长度应为(2~10)位")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NotNull(message="名称不能为空")
    @Length(min = 2,max = 20,message = "名称长度应为(2~20)位")
    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(Integer vipFlag) {
        this.vipFlag = vipFlag;
    }

    public String getGroupCategoryName() {
        return groupCategoryName;
    }

    public void setGroupCategoryName(String groupCategoryName) {
        this.groupCategoryName = groupCategoryName;
    }

    public Integer getGroupCategory() {
        return groupCategory;
    }

    public void setGroupCategory(Integer groupCategory) {
        this.groupCategory = groupCategory;
    }

    public Integer getAppCompleteFlag() {
        return appCompleteFlag;
    }

    public void setAppCompleteFlag(Integer appCompleteFlag) {
        this.appCompleteFlag = appCompleteFlag;
    }

    public Integer getAutoGradeFlag() {
        return autoGradeFlag;
    }

    public void setAutoGradeFlag(Integer autoGradeFlag) {
        this.autoGradeFlag = autoGradeFlag;
    }
}
