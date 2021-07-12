package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDServicePointLog;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServicePointLog;
import com.wolfking.jeesite.modules.sys.entity.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MDServicePointLogMapper extends CustomMapper<MDServicePointLog, ServicePointLog> {
    @Override
    public void mapAtoB(MDServicePointLog a, ServicePointLog b, MappingContext context) {
        b.setId(a.getId());
        b.setServicePoint(new ServicePoint(Optional.ofNullable(a.getServicePointId()).orElse(0L)));
        b.setType(a.getType());
        b.setTitle(a.getTitle());
        b.setContent(a.getContent());
        b.setOperator(a.getOperator());
        b.setCreateBy(new User(Optional.ofNullable(a.getCreateById()).orElse(0L)));
        b.setCreateDate(a.getCreateDate());
    }

    @Override
    public void mapBtoA(ServicePointLog b, MDServicePointLog a, MappingContext context) {
        a.setId(b.getId());
        a.setServicePointId(Optional.ofNullable(b.getServicePoint()).map(ServicePoint::getId).orElse(0L));
        a.setType(b.getType());
        a.setTitle(b.getTitle());
        a.setContent(b.getContent());
        a.setOperator(b.getOperator());
        a.setCreateById(Optional.ofNullable(b.getCreateBy()).map(User::getId).orElse(0L));
        a.setCreateDate(b.getCreateDate());
    }
}
