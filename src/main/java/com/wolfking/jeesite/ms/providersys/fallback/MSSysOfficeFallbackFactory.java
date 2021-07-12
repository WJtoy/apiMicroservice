package com.wolfking.jeesite.ms.providersys.fallback;


import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.sys.SysOffice;
import com.wolfking.jeesite.ms.providersys.feign.MSSysOfficeFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class MSSysOfficeFallbackFactory implements FallbackFactory<MSSysOfficeFeign> {

    @Override
    public MSSysOfficeFeign create(Throwable throwable) {
        if(throwable != null) {
            log.error("MSSysOfficeFeign FallbackFactory:{}", throwable.getMessage());
        }

        return new MSSysOfficeFeign() {
            /**
             * 根据id获取id，name，code属性
             *
             * @param id
             * @return id, name, code
             */
            @Override
            public MSResponse<SysOffice> getSpecColumnById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据部门id列表获取部门的(id,name,code)
             *
             * @param ids
             * @return
             */
            @Override
            public MSResponse<List<SysOffice>> findSpecColumnListByIds(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
