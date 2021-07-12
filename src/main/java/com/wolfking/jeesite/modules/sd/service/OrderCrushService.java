package com.wolfking.jeesite.modules.sd.service;

import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.sd.dao.OrderCrushDao;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 突击单Service
 *
 * @author RyanLu
 * @version 2020-04-22
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class OrderCrushService extends LongIDBaseService {

    @Resource
    private OrderCrushDao crushDao;

    /**
     * 按订单关闭突击单
     * @param orderId   订单id
     * @param quarter   分片
     * @param status    更新后状态
     * @param closeRemark   备注
     * @param closeBy   更新人
     * @param closeDate 更新日期
     */
    @Transactional
    public int closeOrderCurshByOrderId(long orderId, String quarter, int status, String closeRemark, User closeBy,Date closeDate){
        return crushDao.closeOrderCurshByOrderId(orderId,quarter,status,closeRemark,closeBy,closeDate);
    }

}
