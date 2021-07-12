package com.wolfking.jeesite.modules.sd.service;

import com.wolfking.jeesite.common.service.LongIDCrudService;
import com.wolfking.jeesite.modules.sd.dao.OrderDao;
import com.wolfking.jeesite.modules.sd.dao.OrderItemCompleteDao;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderCacheOpType;
import com.wolfking.jeesite.modules.sd.entity.OrderCacheParam;
import com.wolfking.jeesite.modules.sd.entity.OrderItemComplete;
import com.wolfking.jeesite.modules.sd.utils.OrderCacheUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 完成工单上传图片
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class OrderItemCompleteService extends LongIDCrudService<OrderItemCompleteDao, OrderItemComplete> {

    @Resource
    private OrderItemCompleteDao orderItemCompleteDao;

    @Autowired
    private OrderService orderService;

    @Resource
    private OrderDao orderDao;

    /**
     * 上传的附件/
     *
     * @param orderItemComplete
     * @return
     */
    @Transactional()
    public void save(OrderItemComplete orderItemComplete) {
        if (orderItemComplete == null) {
            throw new RuntimeException("参数值未空。");
        }
        if (orderItemComplete.getOrderId() == null || orderItemComplete.getOrderId() <= 0) {
            throw new RuntimeException("未关联订单:无法保存附件。");
        }

        Order order = orderService.getOrderById(orderItemComplete.getOrderId(), "", OrderUtils.OrderDataLevel.CONDITION, true);

        if (order == null || order.getOrderCondition() == null) {
            throw new RuntimeException("读取关联订单信息失败");
        }
        if (orderItemComplete.getId() != null && orderItemComplete.getId() > 0) {
            orderItemCompleteDao.update(orderItemComplete);

        } else {
            orderItemCompleteDao.insert(orderItemComplete);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("quarter", order.getQuarter());
        map.put("orderId", orderItemComplete.getOrderId());
        map.put("finishPhotoQty", 1);//+1
        orderDao.updateCondition(map);

        //调用订单公共缓存
        OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
        builder.setOpType(OrderCacheOpType.UPDATE)
                .setOrderId(orderItemComplete.getOrderId())
                .incrFinishPhotoQty(1L);
        OrderCacheUtils.update(builder.build());

    }

    /**
     * 获取上传的附件数据/
     *
     * @param orderId
     * @param quarter
     * @return
     */
    public List<OrderItemComplete> getByOrderId(Long orderId, String quarter) {
        return orderItemCompleteDao.getByOrderId(orderId, quarter);
    }

    /**
     * 删除上传的附件(整条数据)/
     *
     * @param orderItemComplete
     * @return
     */
    public void delete(OrderItemComplete orderItemComplete) {
        orderItemCompleteDao.delete(orderItemComplete);
        HashMap<String, Object> map = new HashMap<>();
        map.put("quarter", orderItemComplete.getQuarter());
        map.put("orderId", orderItemComplete.getOrderId());
        map.put("finishPhotoQty", -1);//-1
        orderDao.updateCondition(map);
    }
}
