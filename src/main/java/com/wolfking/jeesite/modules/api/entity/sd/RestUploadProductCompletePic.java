package com.wolfking.jeesite.modules.api.entity.sd;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.api.entity.sd.adapter.RestOrderStatusLogAdapter;
import com.wolfking.jeesite.modules.api.entity.sd.adapter.RestUploadProductCompletePicAdapter;
import lombok.Data;

/**
 * 上传完工图片的响应实体
 */
@JsonAdapter(RestUploadProductCompletePicAdapter.class)
public class RestUploadProductCompletePic {
    private Long uniqueId;
    private String pictureCode;
    private String url;

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getPictureCode() {
        return pictureCode;
    }

    public void setPictureCode(String pictureCode) {
        this.pictureCode = pictureCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
