package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.MaterialAttachment;
import com.wolfking.jeesite.modules.sd.entity.MaterialReturn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author Ryan Lu
 * @version 1.0.0
 * @description 返件单数据层
 * @date 2019/6/29
 */
@Mapper
public interface OrderMaterialReturnDao extends LongIDCrudDao<MaterialReturn> {


    /**
     * 按id返回返件申请记录(不包含图片)
     *
     * @param id           返件单id
     * @param masterId     配件单id
     * @param quarter      分片
     * @param withOrder    返回订单基本信息
     * @param withPending  返回跟踪信息
     * @param withReceivor 返回收件信息
     * @param withClose    返回关闭信息
     * @return
     */
    MaterialReturn getReturnFormById(@Param("id") Long id, @Param("masterId") Long masterId, @Param("quarter") String quarter, @Param("withOrder") Integer withOrder, @Param("withPending") Integer withPending, @Param("withReceivor") Integer withReceivor, @Param("withClose") Integer withClose);


    /**
     * 按返件单id查询图片列表
     *
     * @param returnId 返件单id
     * @return
     */
    List<MaterialAttachment> findAttachementsByReturnId(@Param("returnId") Long returnId, @Param("quarter") String quarter);


    /**
     * 修改单头
     */
    void updateMaterialReturn(HashMap<String, Object> params);


    /**
     * 添加配件附件（和配件单头关联）
     *
     * @param attachment
     */
    void insertMaterialAttach(MaterialAttachment attachment);

    /**
     * 配件表（单头）- 附件 关联表
     *
     * @param returnId     返件单id
     * @param attachmentId 附件id(sd_material_attachment)
     */
    void insertMaterialMasterAttachMap(@Param("returnId") Long returnId, @Param("attachmentId") Long attachmentId, @Param("quarter") String quarter);


    /**
     * 读取订单返件状态信息，判断是否可客评
     *
     * @return
     */
    List<MaterialReturn> getMaterialReturnListForGrade(@Param("orderId") Long orderId, @Param("quarter") String quarter);


}
