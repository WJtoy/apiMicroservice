package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDServiceType;
import com.wolfking.jeesite.ms.providermd.feign.MSServiceTypeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MSServiceTypeFeignFallbackFactory implements FallbackFactory<MSServiceTypeFeign> {
    @Override
    public MSServiceTypeFeign create(Throwable throwable) {
        return new MSServiceTypeFeign() {

            @Override
            public MSResponse<MDServiceType> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据ID获取是否扣点和平台信息费开关标识-->财务
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<MDServiceType> getTaxAndInfoFlagByIdForFI(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据ID列表获取是否扣点和平台信息费开关标识列表-->财务
             *
             * @param ids
             * @return
             */
            @Override
            public MSResponse<List<MDServiceType>> findTaxAndInfoFlagListByIdsForFI(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDServiceType>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDServiceType>> findList(MDServiceType mdServiceType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDServiceType mdServiceType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDServiceType mdServiceType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDServiceType mdServiceType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDServiceType> getFromCache(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Map<Long, String>> findAllIdsAndNames() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Map<Long, String>> findIdsAndCodes() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDServiceType>> findAllListWithCondition(List<String> fieldList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 获取工单类型为维修的服务类型列表 //2019-11-26
             *
             * @return
             */
            @Override
            public MSResponse<List<MDServiceType>> findListByMaintenance() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
