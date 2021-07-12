package com.wolfking.jeesite.ms.mapper.sys;

import com.kkl.kklplus.entity.sys.SysAppNotice;
import com.wolfking.jeesite.modules.sys.entity.APPNotice;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class SysAppNoticeMapper extends CustomMapper<SysAppNotice, APPNotice> {
    @Override
    public void mapAtoB(SysAppNotice a, APPNotice b, MappingContext context) {
        b.setId(a.getId());
        b.setPlatform(a.getPlatform());
        b.setUserId(a.getUserId());
        b.setDeviceId(a.getDeviceId());
        b.setChannelId(a.getChannelId());
    }

    @Override
    public void mapBtoA(APPNotice b, SysAppNotice a, MappingContext context) {
        a.setId(b.getId());
        a.setPlatform(b.getPlatform());
        a.setUserId(b.getUserId());
        a.setDeviceId(b.getDeviceId());
        a.setChannelId(b.getChannelId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateDate(b.getUpdateDate());
    }
}
