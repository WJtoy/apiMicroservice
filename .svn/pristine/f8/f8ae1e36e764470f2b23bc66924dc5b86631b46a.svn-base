package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.base.Splitter;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDRegionPermission;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.service.AreaService;
import com.wolfking.jeesite.ms.providermd.feign.MSRegionPermissionFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Ryan Lu
 * @date: 2021/4/27 下午3:02
 * @Description: 区域设置
 */
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
@Slf4j
public class MSRegionPermissionNewService {

    @Autowired
    private MSRegionPermissionFeign regionPermissionFeign;

    @Autowired
    private AreaService areaService;

    /**
     * 根据区,街道判断是否为远程区域
     *
     * @param productCategory
     * @param areaId
     * @param subAreaId
     * @return
     *      1.code =0
     *       data=1:远程区域;data=0:非远程区域
     *      2.code >0,错误
     */
    public MSResponse<Integer> getRemoteAreaStatusFromCacheForSD(long productCategory, long areaId, long subAreaId) {
        Integer remoteAreaStatus = 0;
        if (subAreaId <= 3) {
            return new MSResponse<>(remoteAreaStatus);
        }
        MDRegionPermission regionPermission = new MDRegionPermission();
        regionPermission.setProductCategoryId(productCategory);
        regionPermission.setAreaId(areaId);
        regionPermission.setSubAreaId(subAreaId);
        Area area = areaService.getFromCache(areaId);
        if (area != null) {
            List<String> ids = Splitter.onPattern(",")
                    .omitEmptyStrings()
                    .trimResults()
                    .splitToList(area.getParentIds());
            if (ids.size() >= 2) {
                regionPermission.setCityId(Long.valueOf(ids.get(ids.size() - 1)));
            }
        }
        MSResponse<Integer> msResponse = regionPermissionFeign.getRemoteAreaStatusFromCacheForSD(regionPermission);
        if (!MSResponse.isSuccessCode(msResponse)) {
            log.error("调用微服务获取远程区域失败.失败原因:{},品类categoryId:{},街道subAreaId:{}", msResponse.getMsg(), regionPermission.getProductCategoryId(), regionPermission.getSubAreaId());
        }
        return msResponse;
    }
}
