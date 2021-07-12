package com.wolfking.jeesite.ms.im.feign;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.sys.IMNoticeInfo;
import com.kkl.kklplus.entity.sys.IMNoticeUser;
import com.kkl.kklplus.entity.sys.SysSystemNotice;
import com.kkl.kklplus.entity.sys.vm.IMNoticeInfoSearchVM;
import com.wolfking.jeesite.ms.im.feign.fallback.IMFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 站内即时通知
 * 调用微服务：providerim
 */
@FeignClient(name = "provider-im", fallbackFactory = IMFeignFallbackFactory.class)
public interface IMFeign {

    //region 公告

    //region 管理
    @PostMapping("/notice/manage/new")
    MSResponse<IMNoticeInfo> newNotice(@RequestBody IMNoticeInfo notice);

    /**
     * 分页查询公告
     */
    @PostMapping("/notice/manage/list")
    MSResponse<MSPage<IMNoticeInfo>> getNoticeList(@RequestBody IMNoticeInfoSearchVM notice);

    /**
     * 根据Id获取
     */
    @GetMapping("/notice/manage/{id}")
    MSResponse<IMNoticeInfo> getNoticeById(@PathVariable("id") Long id);

    /**
     * 撤销公告
     */
    @PostMapping("/notice/manage/cancel/{id}")
    MSResponse<Boolean> cancel(@PathVariable("id") long id);

    //endregion


    //region 个人

    /**
     * 个人阅读公告
     * @param id 公告对象记录id
     */
    @GetMapping("/notice/user/get/{id}")
    MSResponse<IMNoticeUser> getUserNoticeById(@PathVariable("id") long id);

    /**
     * 分页查询个人公告
     * notice.userId 为个人帐号id
     */
    @PostMapping("/notice/user/list")
    MSResponse<MSPage<IMNoticeUser>> getUserNoticeList(@RequestBody IMNoticeInfoSearchVM notice);

    /**
     * 标记单条为已读
     * @param userId
     * @param id 公告对象记录id
     */
    @PostMapping("/notice/user/markReaded/{userId}/{id}")
    MSResponse<Integer> markUserNoticeReaded(@PathVariable("userId") long userId, @PathVariable("id") long id);

    /**
     * 标记多条为已读
     * @param params HashMap类型
     * ids:公告对象记录id，多个id用逗号分隔
     * userId: 操作人
     */
    @PostMapping("/notice/user/markMultiReaded")
    MSResponse<Integer> markMultiUserNoticeReaded(@RequestBody Map<String,Object> params);

    //endregion

    //endregion

    //region 个人消息
    //person

    //endregion

    //region client

    /**
     * 在线客户端列表
     * @param withList 返回客户端列表
     */
    @GetMapping("/notice/clients/onlineUsers")
    MSResponse<Map> clients(@RequestParam(value="withList")String withList);

    //endregion
}
