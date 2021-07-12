package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.config.Global;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.api.entity.sd.RestMaterialMaster;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.utils.ApiPropertiesUtils;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MaterialMaster <-> RestMaterialMaster
 */
@Component
public class RestMaterialMasterMapper extends CustomMapper<RestMaterialMaster, MaterialMaster>{

    @Override
    public void mapAtoB(RestMaterialMaster a, MaterialMaster b, MappingContext context) {

    }

    @Override
    public void mapBtoA(MaterialMaster b, RestMaterialMaster a, MappingContext context) {
        a.setId(b.getId().toString());
        a.setQuarter(b.getQuarter());
        a.setOrderId(b.getOrderId().toString());
        a.setOrderdetailId(b.getOrderDetailId()==null?"0":b.getOrderDetailId().toString());
        a.setApplytype(b.getApplyType().getLabel());
        a.setApplytypeValue(b.getApplyType().getValue());
        //a.setMaterialTypeValue(b.getMaterialType().getValue());
        //a.setMaterialType(b.getMaterialType().getLabel());
        a.setMaterialTypeValue("1");
        a.setMaterialType("配件申请");
        a.setStatus(b.getStatus().getValue());
        a.setStatusName(b.getStatus().getLabel());
        a.setExpresscompany(b.getExpressCompany().getLabel());
        a.setExpressno(b.getExpressNo());
        a.setRemarks(b.getRemarks());
        a.setTotalprice(b.getTotalPrice());
        //a.setCreateDate(DateUtils.formatDate(b.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
        a.setCreateDate(b.getCreateDate().getTime());
        a.setReturnFlag(b.getReturnFlag());

        //StringBuffer sbRemarks = new StringBuffer(250);
        //for (MaterialItem item : a.getItems()) {
        //    sbRemarks.append(",")
        //            .append(item.getMaterial().getName())
        //            .append(",数量:")
        //            .append(String.valueOf(item.getQty()))
        //            .append(",单价:")
        //            .append(String.format("%.2f", item.getPrice()))
        //            .append("元,总价:")
        //            .append(String.format("%.2f", item.getTotalPrice()))
        //            .append("元;");
        //}
        //a.setDetails(sbRemarks.length()>0?sbRemarks.substring(1):sbRemarks.toString());
        a.setDetails("");
        a.setItems(b.getItems());
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
