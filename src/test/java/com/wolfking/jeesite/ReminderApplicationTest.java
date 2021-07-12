package com.wolfking.jeesite;

import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.modules.sd.entity.Identity;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderAdditionalInfo;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sd.service.OrderAdditionalInfoService;
import com.wolfking.jeesite.modules.sd.service.OrderItemService;
import com.wolfking.jeesite.modules.sd.utils.OrderAdditionalInfoUtils;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.b2bcenter.sd.dao.B2BOrderDao;
import com.wolfking.jeesite.ms.cc.service.ReminderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther wj
 * @Date 2020/11/10 13:56
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("dev")
public class ReminderApplicationTest {

    @Autowired
    ReminderService reminderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private B2BOrderDao b2BOrderDao;

    @Autowired
    private OrderAdditionalInfoService orderAdditionalInfoService;

    @Test
    public void test(){
        User user = new User();
        user.setId(1L);
        reminderService.replyReminder(1328572222419238912L,1296683321711005696L,"20203","测试",user,15L);

    }

    @Test
    public void getOrderItems(){
        Order order = orderItemService.getOrderItems("20204", 1340498203774160897L);
        List<OrderItem> items = order.getItems();
        System.out.println("items: " + GsonUtils.getInstance().toGson(items));
    }

    @Test
    public void getOrderAdditionalInfo(){
        OrderAdditionalInfo orderAdditionalInfo = null;
        Order order = b2BOrderDao.getOrderAdditionalInfo(1314485318283563008L, "20204");
        if(order != null && order.getAdditionalInfoPb() != null && order.getAdditionalInfoPb().length > 0){
            orderAdditionalInfo = OrderAdditionalInfoUtils.pbBypesToAdditionalInfo(order.getAdditionalInfoPb());
        }else{
            System.out.println("orderAdditionalInfo: null");
        }
        if(orderAdditionalInfo != null){
            System.out.println("orderAdditionalInfo: " + GsonUtils.getInstance().toGson(orderAdditionalInfo));
        }else{
            System.out.println("orderAdditionalInfo: null");
        }
    }

    @Test
    public void getOrderAdditionalInfo2(){
        OrderAdditionalInfo orderAdditionalInfo = orderAdditionalInfoService.getOrderAdditionalInfo(1314485318283563008L, "20204");
        if(orderAdditionalInfo != null){
            System.out.println("orderAdditionalInfo: " + GsonUtils.getInstance().toGson(orderAdditionalInfo));
        }else{
            System.out.println("orderAdditionalInfo: null");
        }
    }



}
