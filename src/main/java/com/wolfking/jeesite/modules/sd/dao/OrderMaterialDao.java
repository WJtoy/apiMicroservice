package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.MaterialAttachment;
import com.wolfking.jeesite.modules.sd.entity.MaterialItem;
import com.wolfking.jeesite.modules.sd.entity.MaterialMaster;
import com.wolfking.jeesite.modules.sd.entity.MaterialProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ryan Lu
 * @version 1.0.0
 * @description 订单配件访问接口
 * @date 2019/5/31 11:24 AM
 */
@Mapper
public interface OrderMaterialDao extends LongIDCrudDao<MaterialMaster> {


    /**
     * 按订单返回配件申请单单头(不包含明细及图片)
     */
    List<MaterialMaster> findMaterialMasterHeadsByOrderId(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /**
     * 按订单返回配件申请记录(包含明细及图片)
     * 产品及配件需重缓存单独读取
     */
    List<MaterialMaster> findMaterialMastersByOrderId(@Param("orderId") Long orderId, @Param("quarter") String quarter);

//    /**
//     * 按订单返回配件申请记录(不包含图片) 预留
//     * 产品及配件需重缓存单独读取
//     */
//    List<MaterialMaster> findMaterialMastersNoAttachByOrderId(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /**
     * 读取订单配件状态信息，判断是否可客评
     */
    List<MaterialMaster> getMaterialFormsForGrade(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /**
     * 读取下个申请次序
     */
    int getNextApplyTime(@Param("orderId") Long orderId, @Param("quarter") String quarter);


    /**
     * 按申请单id返回配件申请单单头
     *
     * @param masterId 申请单id
     * @return
     */
    MaterialMaster getMaterialMasterHeadById(@Param("masterId") Long masterId, @Param("quarter") String quarter);


    /**
     * 添加配件单头
     *
     * @param materialMaster
     */
    void insertMaterialMaster(MaterialMaster materialMaster);

    /**
     * 添加配件产品信息
     */
    void insertMaterialProduct(MaterialProduct materialProduct);

    /**
     * 修改配件申请当头
     */
    int updateMaterialMaster(HashMap<String, Object> params);

    Integer getMaterialMasterCountByOrderId(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("status") Integer status);

    /**
     * 物流接口更新签收时间
     */
    void updateLogisticSignAt(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("expressNo") String expressNo, @Param("signAt") Date signAt, @Param("updateDate") Date updateDate);

    /**
     * 添加配件单身项
     *
     * @param materialItem
     */
    void insertMaterialItem(MaterialItem materialItem);

    /**
     * 添加配件附件（和配件单头关联）
     *
     * @param attachment
     */
    void insertMaterialAttach(MaterialAttachment attachment);

    /**
     * 配件表（单头）- 附件 关联表
     *
     * @param materialMasterId
     * @param attachmentId
     */
    void insertMaterialMasterAttachMap(@Param("materialMasterId") Long materialMasterId, @Param("attachmentId") Long attachmentId, @Param("quarter") String quarter);


}
