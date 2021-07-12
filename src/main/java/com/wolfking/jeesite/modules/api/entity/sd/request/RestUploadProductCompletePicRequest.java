package com.wolfking.jeesite.modules.api.entity.sd.request;

import lombok.Data;

/**
 * @author Zhoucy
 * @date 2018/8/22 13:56
 **/
@Data
public class RestUploadProductCompletePicRequest {

    private Long orderId;
    private String quarter;
    private Long uniqueId;
    private Long productId;
    private String pictureCode;

}
