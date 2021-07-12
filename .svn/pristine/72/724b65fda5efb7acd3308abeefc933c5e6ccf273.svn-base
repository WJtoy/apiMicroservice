/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sys.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sys.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色DAO接口
 *
 * @author ThinkGem
 * @version 2013-12-05
 */
@Mapper
public interface RoleDao extends LongIDCrudDao<Role> {
    List<Role> findAllListNew();   // sysOffice微服务化  2020-12-12
    /**
     * 按用户ID获得角色列表
     */
    List<Role> getUserRoles(@Param("userId") Long userId);
}
