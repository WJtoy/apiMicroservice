package com.wolfking.jeesite.modules.md.dao;

import com.wolfking.jeesite.common.persistence.BaseDao;
import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServicePointFinance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 客户数据访问接口
 * Created on 2017-04-12.
 */
@Mapper
public interface ServicePointDao extends BaseDao {

    /**
     * 获取服务网点帐务信息
     *
     * @param id
     * @return
     */
    ServicePointFinance getFinanceNew(@Param("id") Long id);


    /**
     * 从主库中获取获取服务网点帐务信息
     *
     * @param id
     * @return
     */
    ServicePointFinance getFinanceFromMaster(@Param("id") Long id);


    /**
     * 获取服务网点帐务信息 -- api获取网点余额信息
     *
     * @param id
     * @return
     */
    ServicePointFinance getFinanceForRestBalance(@Param("id") Long id);

    /**
     * 获取网点所有金额
     *
     * @param id
     * @return
     */
    ServicePointFinance getAmounts(@Param("id") Long id);

    /**
     * 更新网点银行账号信息
     * 参数：bank.value、branch、bankNo、bankOwner
     */
    void updateServicePointFIBankAccountInfo(ServicePointFinance servicePointFinance);
}
