package com.wolfking.jeesite.ms.providermd.service;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDMaterialCategory;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.md.entity.MaterialCategory;
import com.wolfking.jeesite.ms.providermd.feign.MSMaterialCategoryFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MSMaterialCategoryService {

    @Autowired
    private MSMaterialCategoryFeign msMaterialCategoryFeign;

    /**
     * 根据id获取配件类别信息
     * @param id
     * @return
     */
    public MaterialCategory getById(Long id) {
        return MDUtils.getById(id, MaterialCategory.class, msMaterialCategoryFeign::getById);
    }
}
