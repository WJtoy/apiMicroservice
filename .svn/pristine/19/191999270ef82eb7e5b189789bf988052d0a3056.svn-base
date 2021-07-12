package com.wolfking.jeesite.modules.md.entity;


import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.md.utils.ProductCompletedPicItemAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 产品完成图片模型
 * @author Ryan Lu
 * @version 2018-08-17
 *
 * @date 2019-06-25
 * @author Ryan
 * 新增属性：上传时间，并设定该类的gson序列化/反序列化实现类
 */
@JsonAdapter(ProductCompletedPicItemAdapter.class)
@Data
@NoArgsConstructor
public class ProductCompletePicItem implements Serializable {

	//图片类型 (对应数据字典：CompletePicType)
	@Length(min = 1,max = 30,message="图片类型长度不能超过30个字符")
	private String pictureCode = "";
	//标题
	private String title = "";
	//地址
	private String url = "";
	//排序
	private Integer sort = 0;
	//否必须上传
	private Integer mustFlag = 0;

	//已选择
	private Integer checked = 0;
    //说明
    private String remarks = "";

    //上传时间
	private Date uploadDate;

	// Constructors
	public ProductCompletePicItem(String pictureCode,String title,Integer sort,Integer mustFlag) {
		this.pictureCode = pictureCode;
		this.title = title;
		this.sort = sort;
		this.mustFlag = mustFlag;
	}

    @Override
    public boolean equals(Object obj) {
	    if(null == obj){
	        return false;
        }
        ProductCompletePicItem that = (ProductCompletePicItem) obj;
        if(this.pictureCode.equalsIgnoreCase(that.getPictureCode())){
            return true;
        }else {
            return false;
        }
    }

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + Objects.hashCode(this.pictureCode);
		hash = 79 * hash + Objects.hashCode(this.title);
		return hash;
	}

}