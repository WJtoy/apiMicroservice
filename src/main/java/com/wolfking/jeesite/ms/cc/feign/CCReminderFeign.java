package com.wolfking.jeesite.ms.cc.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.cc.Reminder;
import com.kkl.kklplus.entity.cc.ReminderItem;
import com.kkl.kklplus.entity.cc.ReminderLog;
import com.kkl.kklplus.entity.cc.vm.*;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.cc.feign.fallback.CCReminderFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 催单(客诉功能其一)
 * 调用微服务：provider-customer-compliant
 */
@FeignClient(name = "provider-customer-compliant", fallbackFactory = CCReminderFactory.class)
public interface CCReminderFeign {

    //region 催单

    //region 列表及读取

    /**
     * 待回复列表
     */
    @RequestMapping("/reminders/waitReplyList")
    MSResponse<MSPage<ReminderListModel>> waitReplyList(@RequestBody ReminderPageSearchModel serachModel);

    /**
     * 查询已回复列表
     */
    @PostMapping("/reminders/haveRepliedList")
    MSResponse<MSPage<ReminderListModel>> haveRepliedList(@RequestBody ReminderPageSearchModel reminderPageSearchModel);

    /**
     * 已处理列表(跟单/客户)
     */
    @RequestMapping("/reminders/processedList")
    MSResponse<MSPage<ReminderListModel>> processedList(@RequestBody ReminderPageSearchModel serachModel);

    /**
     * 查询工单完成的催单列表
     */
    @PostMapping("/reminders/finishList")
    MSResponse<MSPage<ReminderListModel>> finishList(@RequestBody ReminderPageSearchModel reminderPageSearchModel);


    /**
     * 分页查询所有催单列表
     */
    @RequestMapping("/reminders/search")
    MSResponse<MSPage<ReminderListModel>> getReminderPage(@RequestBody ReminderPageSearchModel serachModel);

    /**
     * 查询订单下催单列表
     */
    @RequestMapping("/reminders/getListByOrderId")
    MSResponse<MSPage<Reminder>> getListByOrderId(@RequestBody ReminderSearchModel serachModel);

    /**
     * 批量检查订单是否可以再次催单
     */
    @RequestMapping("/reminders/bulkRereminderCheck")
    MSResponse<Map<Long,Reminder>> bulkRereminderCheck(@RequestBody BulkRereminderCheckModel serachModel);


    /**
     * 检查订单是否可以再次催单
     */
    @RequestMapping("/reminders/reReminderCheck/{quarter}/{orderId}/{orderCreateAt}")
    MSResponse<RereminderCheckRespone> reReminderCheck(@PathVariable("quarter") String quarter, @PathVariable("orderId") Long orderId,@PathVariable("orderCreateAt") Long orderCreateAt);

    /**
     * 按订单id批量读取订单催单时效信息
     */
    @RequestMapping("/reminders/bulkGetReminderTimeLinessByOrders")
    MSResponse<Map<Long, ReminderTimeLinessModel>> bulkGetReminderTimeLinessByOrders(@RequestBody BulkRereminderCheckModel serachModel);

    /**
     * 根据Id+分片获取具体某个催单单据
     */
    @GetMapping("/reminders/{quarter}/{id}/{itemFlag}")
    MSResponse<Reminder> getReminderById(@PathVariable("quarter") String quarter, @PathVariable("id") Long id,@PathVariable("itemFlag")Integer itemFlag);



    /**
     * 根据订单Id+分片获取待回复催单单据
     */
    @GetMapping("/reminders/getWaitReply/{quarter}/{orderId}")
    MSResponse<Reminder> getWaitReplyReminder(@PathVariable("quarter") String quarter, @PathVariable("orderId") Long orderId);


    /**
     * 根据订单Id+分片获取最后一次催单单据
     */
    @GetMapping("/reminders/getLast/{quarter}/{orderId}")
    MSResponse<Reminder> getLast(@PathVariable("quarter") String quarter, @PathVariable("orderId") Long orderId);



    /**
     * 创建
     */
    @PostMapping("/reminders/new")
    MSResponse<Reminder> newReminder(@RequestBody Reminder reminder);

    /**
     * 回复处理(客服/网点回复)
     */
    @PutMapping("/reminders/reply")
    MSResponse<Integer> replyReminder(@RequestBody Reminder reminder);


    /**
     * 获取催单状态
     */
    @GetMapping("/reminders/getStatusById")
    MSResponse<Integer> getStatusById(@RequestParam("id") Long id,@RequestParam("quarter") String quarter);

    /**
     *
     * @param
     * @return
     */
    @GetMapping("/reminders/findIdsByReminderId")
    MSResponse<List<ReminderItem>> findIdsByReminderId(@RequestParam("reminderId") Long reminderId ,@RequestParam("quarter") String quarter);


    @GetMapping("/reminders/getReminderItemById")
    MSResponse<ReminderItem> getReminderItemById(@RequestParam("id") Long id, @RequestParam("quarter") String quarter);

    /**
     * 确认处理
     */
    @PutMapping("/reminders/confirm")
    MSResponse<Integer> confirmProcessed(@RequestBody Reminder reminder);

    /**
     * 驳回催单(再次催单)
     */
    @PutMapping("/reminders/reject")
    MSResponse<Integer> rejectProcessed(@RequestBody Reminder reminder);

    /**
     * 完成
     * 取消订单，退单并审核，客评
     */
    @PutMapping("/reminders/completeByOrder")
    MSResponse<Integer> completeByOrder(@RequestBody Reminder reminder);

    /**
     * 工单关闭更新相关信息(工单状态,工单关闭时间,工单关闭时效) [预留]
     */
    @PutMapping("/reminders/updateOrderCloseInfo")
    MSResponse<Integer> updateOrderCloseInfo(@RequestBody Reminder reminder);

    //endregion 操作

    //endregion 催单

    //region 日志

    /**
     * 新增日志
     */
    @PostMapping("/reminders/logs/new")
    MSResponse<Long> newLog(@RequestBody ReminderLog log);

    /**
     * 读取具体催单日志列表
     */
    @GetMapping("/reminders/logs/getReminderLogs")
    MSResponse<MSPage<ReminderLog>> getReminderLogs(@RequestParam("quarter") String quarter , @RequestParam("reminderId") String reminderId);

    //endregion
}
