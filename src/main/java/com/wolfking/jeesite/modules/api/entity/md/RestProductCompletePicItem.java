package com.wolfking.jeesite.modules.api.entity.md;

import lombok.Data;

/**
 * @author Zhoucy
 * @date 2018/8/21 11:15
 **/
@Data
public class RestProductCompletePicItem {

    /**
     * 图片类型编码
     */
    private String pictureCode = "";

    /**
     * 排序
     */
    private Integer sort = 0;

    /**
     * 标题
     */
    private String title = "";

    /**
     * 说明
     */
    private String remarks = "";

    /**
     * 是否必须
     */
    private Integer mustFlag = 0;

    /**
     * 图片url
     */
    private String url = "";
}
