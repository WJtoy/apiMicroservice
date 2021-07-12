package com.wolfking.jeesite.ms.providersys.service;

import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.modules.sys.entity.Office;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import com.wolfking.jeesite.ms.providersys.feign.MSSysOfficeFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MSSysOfficeService {

    @Autowired
    private MSSysOfficeFeign msSysOfficeFeign;

    /**
     * 根据id获取id，name，code属性
     *
     * @param id
     * @return id, name, code
     */
    public Office getSpecColumnById(Long id) {
        return MDUtils.getObjNecessaryConvertType(Office.class, ()->msSysOfficeFeign.getSpecColumnById(id));
    }

    /**
     * 根据部门id列表获取部门的(id,name,code)
     *
     * @param ids
     * @return
     */
    public List<Office> findSpecColumnListByIds(List<Long> ids) {
        return MDUtils.findListNecessaryConvertType(Office.class, ()->msSysOfficeFeign.findSpecColumnListByIds(ids));
    }
}
