package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户数据访问接口
 * Created on 2017-04-12.
 */
@Mapper
public interface OrderHeadDao extends LongIDCrudDao<Order> {

    //region crud

    /**
     * 修改单头
     * @param map
     */
    long updateOrder(HashMap<String, Object> map);

    /**
     * 读取订单单头信息
     * @param orderId   订单ID
     * @param quarter   数据分片
     */
    Order getOrderById(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /**
     * 按订单id列表批量获得订单单头
     *
     * @param orderIds 订单id列表
     * @return

    List<Order> getOrderByIds(List<Long> orderIds);
     */

    /**
     * 读取订单单头中项目明细
     * 项目明细返回二进制数组，需转换
     * @param orderId   订单ID
     * @param quarter   数据分片
     */
    Order getOrderItemsById(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /**
     * 从主库读取订单单头
     * @param orderId   订单ID
     * @param quarter   数据分片
     */
    Order getOrderFromMasterById(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /**
     * 根据订单ID读取订单分片
     * @param orderId
     * @return
     */
    String getOrderQuarter(@Param("orderId") Long orderId);

    /**
     * 根据订单号获得订单id及分片
     * @param orderNo
     * @param quarter
     * @return
     */
    Map<String, Object> getOrderIdByNo(@Param("orderNo") String orderNo, @Param("quarter") String quarter);

    //endregion crud

    //region B2B

    /**
     * 按B2B工单id+分片+数据源读取已转换工单系统工单信息
     * 包含:id,order_no,quarter
     *
     * @param dataSource
     * @param workCardId
     * @param quarter
     * @return 工单系统订单号或null
     */
    HashMap<String, Object> getB2BOrderNo(@Param("dataSource") int dataSource, @Param("workCardId") String workCardId, @Param("quarter") String quarter);

    /**
     * 更新订单附加信息
     * @param orderId
     * @param quarter
     * @param additionalInfoPb  附加信息（二进制数组）
     */
    void updateOrderAdditionalInfo(@Param("id") Long orderId, @Param("quarter") String quarter, @Param("additionalInfoPb") byte[] additionalInfoPb);

    //endregion B2B
}
