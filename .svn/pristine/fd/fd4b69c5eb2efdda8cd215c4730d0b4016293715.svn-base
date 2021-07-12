package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.*;
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
public interface OrderDao extends LongIDCrudDao<Order> {

    /*订单状态*/
    OrderStatus getOrderStatusById(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("fromMasterDb") Boolean fromMasterDb);

    /*订单费用*/
    OrderFee getOrderFeeById(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("fromMasterDb") Boolean fromMasterDb);

    /**
     * 读取订单当前网点id
     *
     * @param orderId
     * @param quarter
     * @return
     */
    Long getCurrentServicePointId(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /*订单扩展表(从主库读) 2017/11/19*/
    OrderCondition getOrderConditionFromMasterById(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /**
     * 从主库读个别特殊字段，主要给并发处理判断使用 2018/01/16
     * 状态，结帐标记，客评标记，配件标记
     */
    Map<String, Object> getOrderConditionSpecialFromMasterById(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    OrderCondition getOrderConditionImportantPropertiesFromMasterById(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    OrderCondition getOrderConditionImportantInfoById(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    void updateCondition(HashMap<String, Object> condition);

    //标记为自动对账中
    int signAutoChargeing(HashMap<String, Object> condition);

    void updateFee(HashMap<String, Object> map);

    int updateStatus(HashMap<String, Object> status);

    //更新orderStatus中催单信息
    int updateReminderInfo(HashMap<String, Object> status);

    // 更新orderCondition.reminderFlag
    int updateConditionReminderFlag(HashMap<String, Object> stauts);

    int updateComplainInfo(HashMap<String, Object> status);

    /**
     * 派单时，新增派单记录
     */
    void insertOrderPlan(OrderPlan model);

    /**
     * 读取网点具体安维师傅的派单记录
     */
    OrderPlan getOrderPlan(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("servicePointId") Long servicePointId, @Param("engineerId") Long engineerId);

    /**
     * 订单派单记录
     */
    List<OrderPlan> getOrderPlanList(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("isMaster") Integer isMaster);

    Integer getOrderPlanMaxTimes(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /**
     * 更新
     */
    Integer UpdateOrderPlan(HashMap<String, Object> map);

    /**
     * 更新安维上门服务标记
     */
    Integer updateServiceFlagOfOrderPlan(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("servicePointId") Long servicePointId, @Param("engineerId") Long engineerId, @Param("serviceFlag") Integer serviceFlag, @Param("updateBy") Long updateBy, @Param("updateDate") Date updateDate);

    /**
     * 新增日志
     */
    void insertProcessLog(OrderProcessLog log);

    /**
     * 返回APP需要的日志列表
     *
     * @param orderId
     * @param quarter
     * @return
     */
    List<OrderProcessLog> getAppOrderLogs(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /**
     * 订单实际服务明细
     *
     * @param orderId
     * @return
     */
    List<OrderDetail> getOrderDetails(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("fromMasterDb") Boolean fromMasterDb);

    void insertDetail(OrderDetail detail);

    /**
     * 更改上门服务费用
     */
    void updateDetail(HashMap<String, Object> params);

    /**
     * 修改上门服务
     *
     * @param params
     */
    void editDetail(HashMap<String, Object> params);

    /**
     * 获得网点保险单
     * 2021/03/04 改为读主库，防止主从不一致造成保险费未扣费
     */
    OrderInsurance getOrderInsuranceByServicePoint(@Param("quarter") String quarter, @Param("orderId") Long orderId, @Param("servicePointId") Long servicePointId);

    /**
     * 新增保险单
     */
    void insertOrderInsurance(OrderInsurance insurance);

    /**
     * 更改
     */
    void updateOrderInsurance(OrderInsurance insurance);

    /**
     * 获得具体网点的费用
     *
     * @param quarter
     * @param orderId
     * @param servicePointId
     * @return
     */
    OrderServicePointFee getOrderServicePointFee(@Param("quarter") String quarter, @Param("orderId") Long orderId, @Param("servicePointId") Long servicePointId);

    /**
     * 获得订单下所有网点费用汇总
     *
     * @param quarter
     * @param orderId
     * @return
     */
    List<OrderServicePointFee> getOrderServicePointFees(@Param("quarter") String quarter, @Param("orderId") Long orderId, @Param("fromMasterDb") Boolean fromMasterDb);

    /**
     * 新增
     */
    void insertOrderServicePointFee(OrderServicePointFee servicePointFee);

    /**
     * 修改网点费用
     *
     * @param maps
     */
    void updateOrderServicePointFeeByMaps(HashMap<String, Object> maps);

    /**
     * 订单保险费合计(返回Null或负数)
     */
    Double getTotalOrderInsurance(@Param("orderId") Long orderId, @Param("quarter") String quarter);


    OrderFee getPresetFeeWhenPlanFromMasterDB(@Param("orderId") Long orderId, @Param("quarter") String quarter);
}
