package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDServicePointStation;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServicePointStation;
import com.wolfking.jeesite.modules.sys.entity.Area;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MDServicePointStationMapper extends CustomMapper<MDServicePointStation, ServicePointStation> {
    @Override
    public void mapAtoB(MDServicePointStation a, ServicePointStation b, MappingContext context) {
        b.setId(a.getId());
        b.setName(a.getName());
        b.setAddress(a.getAddress());
        b.setLongtitude(a.getLongtitude());
        b.setLatitude(a.getLatitude());
        b.setRadius(a.getRadius());
        b.setAutoPlanFlag(a.getAutoPlanFlag());
        b.setServicePoint(Optional.ofNullable(a.getServicePointId()).map(r->new ServicePoint(r)).orElse(null));
        if(a.getSubAreaId()!=null && a.getSubAreaId()>0){
            Area area = new Area(a.getSubAreaId());
            if(a.getAreaId() !=null && a.getAreaId()>0){
               Area parent = new Area(a.getAreaId());
                area.setParent(parent);
            }
            b.setArea(area);
        }
        b.setDelFlag(a.getDelFlag());
        b.setRemarks(a.getRemarks());
        b.setIsNewRecord(a.getIsNewRecord());
    }

    @Override
    public void mapBtoA(ServicePointStation b, MDServicePointStation a, MappingContext context) {
        a.setId(b.getId());
        a.setName(b.getName());
        a.setAddress(b.getAddress());
        a.setLongtitude(b.getLongtitude());
        a.setLatitude(b.getLatitude());
        a.setRadius(b.getRadius());
        a.setAutoPlanFlag(b.getAutoPlanFlag());
        a.setServicePointId(Optional.ofNullable(b.getServicePoint()).map(ServicePoint::getId).orElse(null));
        //a.setAreaId(Optional.ofNullable(b.getArea()).map(Area::getId).orElse(null));
        if(b.getArea() !=null && b.getArea().getId() !=null && b.getArea().getId()>0){
            a.setSubAreaId(b.getArea().getId());
            Area parent = b.getArea().getParent();
            if(parent !=null && parent.getId() !=null && parent.getId()>0){
                a.setAreaId(parent.getId());
            }
        }
        a.setRemarks(b.getRemarks());
        a.setDelFlag(b.getDelFlag());
        a.setNewRecord(b.getIsNewRecord());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
        a.setNewRecord(b.getIsNewRecord());
    }
}
