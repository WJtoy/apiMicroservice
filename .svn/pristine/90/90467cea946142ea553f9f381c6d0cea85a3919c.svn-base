package com.wolfking.jeesite.ms.mapper.sys;

import com.kkl.kklplus.entity.sys.SysUser;
import com.wolfking.jeesite.modules.sys.entity.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

/**
 * @author Zhoucy
 * @date 2018/9/25 10:21
 **/
public class MSUserMapper extends CustomMapper<SysUser, User> {

    @Override
    public void mapAtoB(SysUser a, User b, MappingContext context) {
        b.setId(a.getId());
        b.setName(a.getName());
        b.setUserType(a.getUserType());
        b.setEmail(a.getEmail());
        b.setPhone(a.getPhone());
        b.setMobile(a.getMobile());
        b.setQq(a.getQq());
        b.setSubFlag(a.getSubFlag());
        b.setDelFlag(a.getDelFlag());
    }

    @Override
    public void mapBtoA(User b, SysUser a, MappingContext context) {
        a.setId(b.getId());
        a.setName(b.getName());
        a.setUserType(b.getUserType());
        a.setEmail(b.getEmail());
        a.setPhone(b.getPhone());
        a.setMobile(b.getMobile());
        a.setQq(b.getQq());
        a.setSubFlag(b.getSubFlag());
        a.setDelFlag(b.getDelFlag());
    }
}
