package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductType;
import com.kkl.kklplus.entity.md.dto.MDProductTypeDto;
import com.kkl.kklplus.entity.md.dto.TreeDTO;
import com.wolfking.jeesite.ms.providermd.feign.MSProductTypeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Component
public class MSProductTypeFeignFallbackFactory implements FallbackFactory<MSProductTypeFeign> {
    @Override
    public MSProductTypeFeign create(Throwable throwable) {
        return new MSProductTypeFeign() {

            @Override
            public MSResponse<MDProductTypeDto> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDProductTypeDto>> findList(MDProductTypeDto productTypeDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Long> getIdByName(String name) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDProductTypeDto mdProductTypeDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDProductTypeDto mdProductTypeDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDProductTypeDto mdProductTypeDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDProductType>> findListByCategoryId(Long productCategoryId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<TreeDTO>>  findTypeAndItemsByCategoryIds(List<Long> productCategoryIds){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

        };
    }
}
