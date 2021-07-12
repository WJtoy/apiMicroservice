package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDServicePoint;
import com.kkl.kklplus.entity.md.MDServicePointViewModel;
import com.kkl.kklplus.entity.md.dto.MDServicePointDto;
import com.kkl.kklplus.entity.md.dto.MDServicePointSearchDto;
import com.kkl.kklplus.entity.md.dto.MDServicePointTimeLinessSummaryDto;
import com.kkl.kklplus.entity.md.dto.MDServicePointUnionDto;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointFeign;
import com.wolfking.jeesite.ms.tmall.md.entity.ServicePointProvinceBatch;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MSServicePointFeignFallbackFactory implements FallbackFactory<MSServicePointFeign> {
    @Override
    public MSServicePointFeign create(Throwable throwable) {
        return new MSServicePointFeign() {
            @Override
            public MSResponse<MDServicePoint> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * @param id
             * @return id, servicepointNo, name, useDefaultPrice, primaryId, customizePriceFlag
             */
            @Override
            public MSResponse<MDServicePoint> getSimpleById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 从缓存中获取网点信息
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<MDServicePoint> getCacheById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 从缓存中获取网点信息(返回有限网点字段信息(id,servicepointno,name,primaryId))
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<MDServicePoint> getSimpleCacheById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 获取互助金购买标识
             * @param id
             * @return
             */
            @Override
            public MSResponse<NameValuePair<Integer,Integer>> getInsuranceFlagByIdForAPI(Long id){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
            /**
             * app关闭购买互助金
             * @param mdServicePoint
             * @return
             */
            @Override
            public MSResponse<Integer> updateInsuranceFlagForAPI(MDServicePoint mdServicePoint){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateRemark(Long servicePointId, String remarks) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Long> getServicePointIdByBankNo(String bankNo, Long exceptId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Long> getServicePointIdByContact(String contactInfo1, Long exceptId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Long> getServicePointNo(String servicePointNo, Long exceptId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Long> getServicePointCapacity(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 获取网点的结算标准/价格
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<Integer> getUseDefaultPrice(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /*@Override
            public MSResponse<List<MDServicePoint>> findBatchByIds(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            /*@Override
            public MSResponse<List<MDServicePointViewModel>> findBatchIdsAndNamesByIds(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDServicePointViewModel>> findBatchIdsAndNamesWithPointNoByIds(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            /**
             * 返回网点数据
             *
             * @param ids     fields
             *                delFlag
             * @param fields
             * @param delFlag
             * @return 网点id, Name, ServicePointNo
             */
            @Override
            public MSResponse<List<MDServicePointViewModel>> findBatchByIdsByCondition(List<Long> ids, List<String> fields, Integer delFlag) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过网点ID获取网点编号和自定义价格标志
             *
             * @param servicePointIds
             * @return
             */
            @Override
            public MSResponse<List<MDServicePoint>> findServicePointNoAndCustomizePriceFlagAndUseDefaultPriceListByIds(List<Long> servicePointIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 返回网点数据
             *
             * @param ids
             * @return 网点id, name, ServicePointNo, contractInfo1, contractInfo2, primaryId
             */
            /*@Override
            public MSResponse<List<MDServicePointViewModel>> findBatchIdsAndNamesWithPointNoAndInfosAndPrimaryId(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            /**
             * 返回网点数据
             *
             * @param
             * @return id, servicePointNo, name, contactInfo1, bank, bankOwner, bankNo, paymentType
             */
            /*@Override
            public MSResponse<List<MDServicePointViewModel>> findBatchIdsAndNamesWithPointNoAndInfosAndBankInfoesAndPayType(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            /*@Override
            public MSResponse<List<Long>> findServicePointIds(Integer forTmall) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            /*@Override
            public MSResponse<List<ServicePointProvinceBatch>> findAreaAndServicePointCountList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            /*@Override
            public MSResponse<MDServicePoint> getPlanRemarks(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<String> getPlanRemarksJson(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<String> getRemarksJson(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            @Override
            public MSResponse<MSPage<MDServicePoint>> findList(MDServicePoint mdServicePoint, List<Long> areaIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDServicePoint>> findListReport(MDServicePoint mdServicePoint, List<Long> areaIds, List<Long> engineerIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDServicePoint>> findListByIds(List<Long> servicePointIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 查询网点id列表
             *
             * @param mdServicePointSearchDto
             * @return
             */
            @Override
            public MSResponse<MSPage<Long>> findIdList(MDServicePointSearchDto mdServicePointSearchDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<Long>> findIdListWithPrice(MDServicePointSearchDto mdServicePointSearchDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 按区县/街道/品类 分页查询可派单列表
             *
             * @param mdServicePointSearchDto
             * @return
             */
            @Override
            public MSResponse<MSPage<Long>> findServicePointIdsForPlan(MDServicePointSearchDto mdServicePointSearchDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDServicePoint mdServicePoint) {
                try {
                    log.error("往微服务插入网点失败,失败原因:{}", throwable.getMessage());
                }catch (Exception e) {}
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 调用微服务保存网点和师傅及师傅地址   2020-05-20
             *
             * @param mdServicePointUnionDto
             * @return
             */
            @Override
            public MSResponse<NameValuePair<Long,Long>> insertServicePointAndEngineer(MDServicePointUnionDto mdServicePointUnionDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDServicePoint mdServicePoint) {
                try {
                    log.error("往微服务更新网点失败,失败原因:{}", throwable.getMessage());
                }catch (Exception e) {}
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updatePlanRemark(Long servicePointId, String planRemark) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateServicePointByMap(HashMap<String, Object> hashMap) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateServicePointForKeySetting(MDServicePoint mdServicePoint) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 更新网点是否使用自定义价格标志  2020-2-24
             *
             * @param mdServicePoint
             * @return
             */
            @Override
            public MSResponse<Integer> updateCustomizePriceFlag(MDServicePoint mdServicePoint) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> approve(List<Long> servicePointIds, Long updateById) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateBankIssue(Long servicePointId, String bankIssue) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> appReadInsuranceClause(Long id, Integer appInsuranceFlag, Long updateBy, Long updateDate) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateServicePointAddress(MDServicePoint servicePoint) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateServicePointBankAccountInfo(MDServicePoint mdServicePoint) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateAutoPlanFlag(MDServicePoint mdServicePoint) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateLevel(MDServicePoint mdServicePoint) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updatePrimaryAccount(MDServicePoint mdServicePoint) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> upgradeServicePoint(MDServicePoint mdServicePoint) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateProductCategoryServicePointMapping(Long servicePointId, List<Long> productCategoryIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDServicePoint mdServicePoint) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDServicePointDto>> findServicePointTimeliness(MDServicePointDto mdServicePointDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDServicePointDto> getServicePointTimeliness(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateTimeliness(MDServicePointDto mdServicePointDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDServicePointTimeLinessSummaryDto>> findTimeLinessFlagListByAreaIds(List<MDServicePointTimeLinessSummaryDto> servicePointTimeLinessSummaryDtoList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateTimelinessByArea(MDServicePointDto mdServicePointDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateCustomerTimelinessByArea(MDServicePointDto mdServicePointDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDServicePointDto>> findIdAndPointNoByAreaIds(List<Long> areaIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<NameValuePair<Long, Long>> insertServicePointUnionDto(MDServicePointUnionDto servicePointUnionDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<Long>> findServicePointIdsByAreaWithCategory(MDServicePointSearchDto servicePointSearchDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateStatus(MDServicePoint mdServicePoint) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateUnfinishedOrderCountByMapForSD(Map<String, Object> paramMap) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
