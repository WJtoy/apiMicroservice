package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDCustomerMaterial;
import com.kkl.kklplus.entity.md.MDProductMaterial;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerMaterialFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
public class MSCustomerMaterialFeignFallbackFactory implements FallbackFactory<MSCustomerMaterialFeign> {
    @Override
    public MSCustomerMaterialFeign create(Throwable throwable) {
        return new MSCustomerMaterialFeign() {


            @Override
            public MSResponse<MDCustomerMaterial> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDCustomerMaterial>> findList(MDCustomerMaterial mdCustomerMaterial) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDCustomerMaterial>> findListByProductAndCustomerIdAndMaterial(MDCustomerMaterial mdCustomerMaterial) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> batchInsert(List<MDCustomerMaterial> mdMaterialList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDCustomerMaterial mdCustomerMaterial) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDCustomerMaterial> getCustomerMaterialByCustomerAndProductAndMaterial(Long customerId, Long productId, Long material) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDCustomerMaterial>> findListByCustomerAndProduct(Long customerId, Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据客户id和产品id删除客户配件
             *
             * @param customerId 客户id
             * @param productId  产品id
             * @return
             */
            @Override
            public MSResponse<Integer> deleteByCustomerAndProduct(Long customerId, Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据客户ID、产品ID、配件IDS获取配件信息
             * @param customerId,productId
             * @param NameValuePairList
             * @return
             */
            @Override
            public MSResponse<List<MDCustomerMaterial>> findListByCustomerIdAndMaterialIdsFromCache(Long customerId, @RequestBody List<NameValuePair<Long, Long>> NameValuePairList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 从客户获取配件并更新到系统中去
             * @param customerMaterials
             * @return
             */
            @Override
            public MSResponse<String> updateCustomerMaterials(List<MDCustomerMaterial> customerMaterials) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据客户id和产品id，客户型号从客户配件中获取产品id及配件id列表
             * @param customerId
             * @param productId
             * @return
             */
            @Override
            public MSResponse<List<MDProductMaterial>> findProductMaterialByCustomerAndProduct(Long customerId, Long productId, String customerModelId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据客户id和产品id、客户型号列表从客户配件中获取产品id及配件id列表
             * @param customerId
             * @param nameValuePairs long-产品id，String- customerModel
             * @return
             */
            @Override
            public MSResponse<List<MDProductMaterial>> findProductMaterialByCustomerAndProductIds(Long customerId, List<NameValuePair<Long,String>> nameValuePairs) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据客户id，产品id，配件id，客户型号(可为空)列表获取客户配件信息
             *
             * @param customerMaterials
             * @return
             */
            @Override
            public MSResponse<List<MDCustomerMaterial>> findListByCustomerMaterial(List<MDCustomerMaterial> customerMaterials) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
