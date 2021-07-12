package com.wolfking.jeesite.ms.cc.service;

import com.google.common.collect.Maps;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderReminderMessage;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderReminderProcessMessage;
import com.kkl.kklplus.entity.cc.*;
import com.kkl.kklplus.entity.cc.vm.BulkRereminderCheckModel;
import com.kkl.kklplus.entity.cc.vm.ReminderTimeLinessModel;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.service.SequenceIdService;
import com.wolfking.jeesite.modules.md.service.ProductCategoryService;
import com.wolfking.jeesite.modules.mq.sender.B2BCenterOrderReminderCloseMQSender;
import com.wolfking.jeesite.modules.sd.service.OrderService;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.cc.entity.ReminderModel;
import com.wolfking.jeesite.ms.cc.feign.CCReminderFeign;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 催单服务层
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ReminderService {

    @Autowired
    private CCReminderFeign feign;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SequenceIdService sequenceIdService;

    @Autowired
    private B2BOrderReminderService b2BOrderReminderService;




    //region 催单

    /**
     * 根据订单Id+quarter读取最后一次催单信息
     */
    public Reminder getLastReminderByOrderId(Long orderId,String quarter){
        MSResponse<Reminder> mSResponse = feign.getLast(quarter,orderId);
        if(MSResponse.isSuccess(mSResponse)){
            return mSResponse.getData();
        }else{
            return null;
        }
    }



    /**
     * 按订单id批量读取订单催单时效信息
     * processTimeLiness:时效 ，创建或再次催单距离现在的时效
     * createDt: 创建日期时间戳
     */
    public Map<Long, ReminderTimeLinessModel> bulkGetReminderTimeLinessByOrders(BulkRereminderCheckModel searchModel){
        MSResponse<Map<Long,ReminderTimeLinessModel>> msResponse = feign.bulkGetReminderTimeLinessByOrders(searchModel);
        if(MSResponse.isSuccess(msResponse)){
            return msResponse.getData();
        }
        return null;
    }

    /**
     * 回复催单
     */
    @Transactional()
    public void replyReminder(Long id,Long orderId,String quarter,String remark,User user,Long servicePointId) {
        long time = System.currentTimeMillis();
        Reminder reminder = new Reminder();
        reminder.setId(id);
        reminder.setOrderId(orderId);
        reminder.setQuarter(quarter);
        reminder.setProcessAt(time);
        reminder.setProcessRemark(remark);
        reminder.setProcessBy(user.getName());
        reminder.setOperatorType(getReminderCreatorType(user).getCode());
        reminder.setStatus(ReminderStatus.Replied.getCode());
        reminder.setPreStatus(ReminderStatus.WaitReply.getCode());
        reminder.setUpdateById(user.getId());
        reminder.setUpdateDt(time);
        MSResponse<List<ReminderItem>> msResponseItemList = feign.findIdsByReminderId(id, quarter);
        List<ReminderItem> itemIds = Lists.newArrayList();
        if(MSResponse.isSuccess(msResponseItemList)){
            itemIds = msResponseItemList.getData();
        }
        MSResponse<Integer> msResponse = new MSResponse<>(MSErrorCode.FAILURE);
        for (ReminderItem item : itemIds) {
            ReminderLog reminderLog = ReminderLog.builder()
                    .quarter(quarter)
                    .status(reminder.getStatus())
                    .visibilityFlag(ReminderModel.VISIBILITY_FLAG_ALL)
                    .content(StringUtils.left("回复催单,回复内容：" + remark, 250))
                    .createName(user.getName())
                    .creatorType(reminder.getOperatorType())
                    .build();
            reminderLog.setCreateDt(reminder.getProcessAt());
            reminderLog.setId(sequenceIdService.nextId());//2020/05/24
            reminder.setReminderLog(reminderLog);
            reminder.setItemId(item.getId());
            msResponse = feign.replyReminder(reminder);
           if (MSResponse.isSuccessCode(msResponse)) {
               try {
                   if (isToJoyoungB2B(item)) {
                       MQB2BOrderReminderProcessMessage.B2BOrderReminderProcessMessage message = MQB2BOrderReminderProcessMessage.B2BOrderReminderProcessMessage.newBuilder()
                               .setDataSource(item.getDataSource())
                               .setKklReminderId(item.getId())
                               .setOperationType(20)
                               .setB2BReminderId(item.getB2bReminderId())
                               .setB2BReminderNo(item.getB2bReminderNo() == null ? "" : item.getB2bReminderNo())
                               .setContent(remark)
                               .setCreateDate(time)
                               .setOperatorId(user.getId())
                               .build();
                       b2BOrderReminderService.sendReminderProcess(message, item.getB2bReminderNo());
                   }
               } catch (Exception e) {
                   log.error("ReminderService.replyReminder:{}", item,e.getMessage());
                   //LogUtils.saveLog("回复催单发送给b2b失败", "reminderServer/replyReminder", "", e, null);
               }
           }
        }
        //MSResponse<Integer> msResponse = feign.replyReminder(reminder);

        if(!MSResponse.isSuccessCode(msResponse)){
            throw new RuntimeException(msResponse.getMsg());
        }else {
            HashMap<String, Object> map = Maps.newHashMapWithExpectedSize(6);
            map.put("orderId", orderId);
            map.put("quarter", quarter);
            map.put("reminderStatus", reminder.getStatus());
            map.put("reminderCreateBy", reminder.getUpdateById());
            map.put("reminderCreateAt", reminder.getUpdateDt());
            map.put("servicePointId",servicePointId);
            orderService.updateReminderInfo(map);
        }
    }

    public Integer getStatusById(Long id ,String quarter){

        Integer status =0;
        MSResponse<Integer> msResponse = feign.getStatusById(id,quarter);
        if (msResponse.getData() != null){
            status = msResponse.getData();
        }
        return status;
    }



    //endregion 催单


    //region 公共
    /**
     * 判断用户的类型
     * @param user  帐号信息
     * @return 用户类型(ReminderCreatorType枚举)
     */
    public ReminderCreatorType getReminderCreatorType(User user){
        if(user == null || user.getId() == null || user.getId()<=0){
            return ReminderCreatorType.None;
        }
        if(user.isSaleman()){
            return ReminderCreatorType.FollowUp;
        }
        if(user.isCustomer()){
            return ReminderCreatorType.Customer;
        }
        if(user.isEngineer()){
            return ReminderCreatorType.Engineer;
        }
        return ReminderCreatorType.Kefu;
    }


    private boolean isToJoyoungB2B(ReminderItem reminderItem){
        boolean result = false;
        if ((reminderItem.getDataSource()==B2BDataSourceEnum.JOYOUNG.id || reminderItem.getDataSource()==B2BDataSourceEnum.MQI.id)
                && reminderItem.getCreateType()==ReminderType.B2B.code
                && reminderItem.getId()> 0){
            return true;
        }
        return result;
    }


    //endregion
}
