package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.config.Global;
import com.wolfking.jeesite.modules.api.entity.sd.RestMaterialMaster;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.utils.ApiPropertiesUtils;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MaterialReturn <-> RestMaterialMaster
 */
@Component
public class RestMaterialReturnMapper extends CustomMapper<RestMaterialMaster, MaterialReturn>{

    @Override
    public void mapAtoB(RestMaterialMaster a, MaterialReturn b, MappingContext context) {

    }

    @Override
    public void mapBtoA(MaterialReturn b, RestMaterialMaster a, MappingContext context) {
        a.setId(b.getId().toString());
        a.setQuarter(b.getQuarter());
        a.setOrderId(b.getOrderId().toString());
        a.setApplytype(b.getApplyType().getLabel());
        a.setApplytypeValue(b.getApplyType().getValue());
        a.setMaterialTypeValue("1");
        a.setMaterialType("配件申请");
        a.setStatus(b.getStatus().getValue());
        a.setStatusName(b.getStatus().getLabel());
        a.setExpresscompany(b.getExpressCompany().getLabel());
        a.setExpressno(b.getExpressNo());
        a.setRemarks(b.getReceivorAddress());//返件地址
        a.setTotalprice(b.getTotalPrice());
        a.setCreateDate(b.getCreateDate().getTime());
        a.setDetails("");
        MaterialItem item;
        List<MaterialItem> items = Lists.newArrayList();
        for(MaterialReturnItem itm:b.getItems()){
            item = new MaterialItem();
            item.setMaterial(itm.getMaterial());
            item.setQty(itm.getQty());
            item.setPrice(itm.getPrice());
            item.setTotalPrice(itm.getTotalPrice());
            items.add(item);
        }
        a.setItems(items);
        //完成照片
        List<MaterialAttachment> photos = Lists.newArrayList();
        if(b.getAttachments() != null && b.getAttachments().size()>0){
//            String host = Global.getConfig("userfiles.host")+"/";
            String host = ApiPropertiesUtils.getUserFiles().getHost() +"/";
            b.getAttachments().forEach(t->{
                t.setFilePath(host+t.getFilePath());
                //t.setQuarter(b.getQuarter());
                photos.add(t);
            });
        }
        a.setPhotos(photos);
    }
}
