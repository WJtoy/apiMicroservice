package com.wolfking.jeesite.ms.im.feign.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.sys.IMNoticeInfo;
import com.kkl.kklplus.entity.sys.IMNoticeUser;
import com.kkl.kklplus.entity.sys.vm.IMNoticeInfoSearchVM;
import com.wolfking.jeesite.ms.im.feign.IMFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Component
public class IMFeignFallbackFactory implements FallbackFactory<IMFeign> {

    @Override
    public IMFeign create(Throwable throwable) {
        return new IMFeign() {

            //region 公告

            //region 管理
            @Override
            public MSResponse<IMNoticeInfo> newNotice(@RequestBody IMNoticeInfo notice){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页查询公告
             */
            @Override
            public MSResponse<MSPage<IMNoticeInfo>> getNoticeList(@RequestBody IMNoticeInfoSearchVM notice){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据Id获取
             */
            @Override
            public MSResponse<IMNoticeInfo> getNoticeById(@PathVariable("id") Long id){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 撤销公告
             */
            @Override
            public MSResponse<Boolean> cancel(@PathVariable("id") long id){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            //endregion


            //region 个人

            /**
             * 个人阅读公告
             * @param id 公告对象记录id
             */
            @Override
            public MSResponse<IMNoticeUser> getUserNoticeById(@PathVariable("id") long id){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页查询个人公告
             * notice.userId 为个人帐号id
             */
            @Override
            public MSResponse<MSPage<IMNoticeUser>> getUserNoticeList(@RequestBody IMNoticeInfoSearchVM notice){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 标记单条为已读
             * @param userId
             * @param id 公告对象记录id
             */
            @Override
            public MSResponse<Integer> markUserNoticeReaded(@PathVariable("userId") long userId, @PathVariable("id") long id){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 标记多条为已读
             * @param params HashMap类型
             * ids:公告对象记录id，多个id用逗号分隔
             * userId: 操作人
             */
            @Override
            public MSResponse<Integer> markMultiUserNoticeReaded(@RequestBody Map<String,Object> params){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

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
            @Override
            public MSResponse<Map> clients(@RequestParam(value="withList") String withList){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            //endregion

        };
    }
}
