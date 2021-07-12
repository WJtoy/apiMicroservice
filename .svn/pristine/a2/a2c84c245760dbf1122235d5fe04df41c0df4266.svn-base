package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDCustomer;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MSCustomerFeignFallbackFactory implements FallbackFactory<MSCustomerFeign> {

    @Override
    public MSCustomerFeign create(Throwable throwable) {
        return new MSCustomerFeign() {
            @Override
            public MSResponse<List<MDCustomer>> findAll() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<NameValuePair<Long, String>>> findBatchListByIds(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据ID从缓存中获取客户信息(输出字段与Web中从缓存获取客户信息相同)
             *
             * @param id
             * @return id, Name, SalesId, Master, Phone, ContractDate
             * MinUploadNumber,MaxUploadNumber,ReturnAddress,
             * PaymentType,Remarks,DefaultBrand,EffectFlag
             * ShortMessageFlag,TimeLinessFlag, UrgentFlag,tVipFlag
             */
            @Override
            public MSResponse<MDCustomer> getFromCache(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 获取所有客户列表
             * id
             * code
             * name
             * contractDate
             * salesMan
             * paymentType
             * @return
             */
            @Override
            public MSResponse<List<MDCustomer>> findAllSpecifiedColumn() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDCustomer>> findCustomerList(MDCustomer customer) {
                log.warn("findCustomerList异常:{}", throwable);
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDCustomer>> findAllWithIdAndName() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDCustomer> get(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDCustomer> getByIdToCustomerSpecifiedColumn(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Long> getCustomerIdByCode(String code) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDCustomer>> findBatchByIds(String ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据id列表获取customer信息列表
             *
             * @param ids
             * @return id
             * code
             * name
             */
            @Override
            public MSResponse<List<MDCustomer>> findByBatchIds(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据id列表获取customer的id与name 两个字段的列表  2020-3-17
             *
             * @param ids
             * @return
             */
            @Override
            public MSResponse<List<MDCustomer>> findIdAndNameListByIds(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 查找所有的非贵宾客户  2020-3-17
             *
             * @return
             */
            @Override
            public MSResponse<List<MDCustomer>> findNoVIPList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据id列表获取客户列表信息
             *
             * @param ids
             * @return id, code, name, paymenttype, salesid, contractDate
             */
            @Override
            public MSResponse<List<MDCustomer>> findCustomersWithIds(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据ID获取客户信息
             *
             * @param id
             * @return id
             * code
             * name
             * salesId
             * remarks
             */
            @Override
            public MSResponse<MDCustomer> getByIdToCustomer(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDCustomer>> findListBySalesId(Integer salesId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据跟单员id获取客户列表
             *
             * @param merchandiserId
             * @return id, name
             */
            @Override
            public MSResponse<List<MDCustomer>> findListByMerchandiserId(Long merchandiserId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDCustomer>> findAllByIdAndPaymentType(MDCustomer customer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<Long>> findCustomerIdByPaymentType(Integer paymentType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 获取vip客户列表
             *
             * @return
             */
            @Override
            public MSResponse<List<MDCustomer>> findListByVipCustomer() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> saveCustomer(MDCustomer customer) {
                log.warn("saveCustomer异常:{}", throwable);
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateCustomer(MDCustomer customer) {
                log.warn("updateCustomer异常:{}", throwable);
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateSales(MDCustomer customer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 更新客户跟单员信息
             *
             * @param customer
             * @return
             */
            @Override
            public MSResponse<Integer> updateMerchandiserId(MDCustomer customer) {
                log.warn("updateMerchandiserId异常:{}", throwable);
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> removeCustomer(MDCustomer mdCustomer) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
