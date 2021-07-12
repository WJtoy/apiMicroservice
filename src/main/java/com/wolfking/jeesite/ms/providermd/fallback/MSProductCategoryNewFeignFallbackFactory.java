package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDProductCategory;
import com.wolfking.jeesite.ms.providermd.feign.MSProductCategoryNewFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSProductCategoryNewFeignFallbackFactory implements FallbackFactory<MSProductCategoryNewFeign> {
    @Override
    public MSProductCategoryNewFeign create(Throwable throwable) {
        return new MSProductCategoryNewFeign() {
            /**
             * 获取全部产品类别-->基础资料
             *
             * @return id, name
             */
            @Override
            public MSResponse<List<NameValuePair<Long, String>>> findAllListForMD() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 获取全部产品类别-->报表
             *
             * @return id, name
             */
            @Override
            public MSResponse<List<NameValuePair<Long, String>>> findAllListForRPT() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 获取全部产品类别-->工单
             *
             * @return id, name
             */
            @Override
            public MSResponse<List<NameValuePair<Long, String>>> findAllListForSD() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 批量获取获取产品类别-->基础资料
             *
             * @param ids
             * @return id, name
             */
            @Override
            public MSResponse<List<NameValuePair<Long, String>>> findListByIdsForMD(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页&条件(code,name)获取产品类别-->基础资料
             *
             * @param mdProductCategory
             * @return
             */
            @Override
            public MSResponse<MSPage<MDProductCategory>> findListForMD(MDProductCategory mdProductCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 获取全部产品品类id
             *
             * @return
             */
            @Override
            public MSResponse<List<Long>> findIdListForMD() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据ID获取产品类别-->基础资料
             *
             * @param id
             * @return id，code,name,del_flag,remarks
             */
            @Override
            public MSResponse<MDProductCategory> getByIdForMD(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据ID从缓存读取-->基础资料
             *
             * @param id
             * @return id, name
             */
            @Override
            public MSResponse<String> getFromCacheForMD(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据ID从缓存读取->工单
             *
             * @param id
             * @return name
             */
            @Override
            public MSResponse<String> getFromCacheForSD(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据产品Code获取ID-->基础资料
             *
             * @param code
             * @return id
             */
            @Override
            public MSResponse<Long> getIdByCodeForMD(String code) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据产品Code获取ID-->基础资料
             *
             * @param name
             * @return id
             */
            @Override
            public MSResponse<Long> getIdByNameForMD(String name) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 添加产品类别
             *
             * @param mdProductCategory
             * @return
             */
            @Override
            public MSResponse<Integer> insert(MDProductCategory mdProductCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 更新产品类别
             *
             * @param mdProductCategory
             * @return
             */
            @Override
            public MSResponse<Integer> update(MDProductCategory mdProductCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 删除一个产品类别
             *
             * @param mdProductCategory
             * @return
             */
            @Override
            public MSResponse<Integer> delete(MDProductCategory mdProductCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
