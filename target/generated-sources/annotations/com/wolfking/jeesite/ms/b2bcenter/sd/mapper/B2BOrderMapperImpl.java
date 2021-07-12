package com.wolfking.jeesite.ms.b2bcenter.sd.mapper;

import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder.B2BOrderItem;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderVModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-16T18:08:06+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class B2BOrderMapperImpl extends B2BOrderMapper {

    @Override
    public B2BOrderVModel toB2BOrderVModel(B2BOrder order) {
        if ( order == null ) {
            return null;
        }

        B2BOrderVModel b2BOrderVModel = new B2BOrderVModel();

        b2BOrderVModel.setParentBizOrderId( order.getParentBizOrderId() );
        b2BOrderVModel.setId( order.getId() );
        b2BOrderVModel.setRemarks( order.getRemarks() );
        b2BOrderVModel.setCreateBy( order.getCreateBy() );
        b2BOrderVModel.setCreateById( order.getCreateById() );
        b2BOrderVModel.setCreateDate( order.getCreateDate() );
        b2BOrderVModel.setUpdateBy( order.getUpdateBy() );
        b2BOrderVModel.setUpdateById( order.getUpdateById() );
        b2BOrderVModel.setUpdateDate( order.getUpdateDate() );
        b2BOrderVModel.setDelFlag( order.getDelFlag() );
        b2BOrderVModel.setPage( order.getPage() );
        b2BOrderVModel.setCreateDt( order.getCreateDt() );
        b2BOrderVModel.setUpdateDt( order.getUpdateDt() );
        b2BOrderVModel.setProcessFlag( order.getProcessFlag() );
        b2BOrderVModel.setProcessTime( order.getProcessTime() );
        b2BOrderVModel.setProcessComment( order.getProcessComment() );
        b2BOrderVModel.setQuarter( order.getQuarter() );
        b2BOrderVModel.setDataSource( order.getDataSource() );
        b2BOrderVModel.setUniqueId( order.getUniqueId() );
        b2BOrderVModel.setB2bOrderId( order.getB2bOrderId() );
        b2BOrderVModel.setKklOrderNo( order.getKklOrderNo() );
        b2BOrderVModel.setOrderNo( order.getOrderNo() );
        b2BOrderVModel.setShopId( order.getShopId() );
        b2BOrderVModel.setShopName( order.getShopName() );
        b2BOrderVModel.setUserName( order.getUserName() );
        b2BOrderVModel.setUserMobile( order.getUserMobile() );
        b2BOrderVModel.setUserPhone( order.getUserPhone() );
        b2BOrderVModel.setUserAddress( order.getUserAddress() );
        b2BOrderVModel.setUserProvince( order.getUserProvince() );
        b2BOrderVModel.setUserCity( order.getUserCity() );
        b2BOrderVModel.setUserCounty( order.getUserCounty() );
        b2BOrderVModel.setUserStreet( order.getUserStreet() );
        b2BOrderVModel.setReceiveDate( order.getReceiveDate() );
        b2BOrderVModel.setEstimatedReceiveDate( order.getEstimatedReceiveDate() );
        b2BOrderVModel.setBrand( order.getBrand() );
        b2BOrderVModel.setServiceType( order.getServiceType() );
        b2BOrderVModel.setWarrantyType( order.getWarrantyType() );
        b2BOrderVModel.setStatus( order.getStatus() );
        b2BOrderVModel.setIssueBy( order.getIssueBy() );
        List<B2BOrderItem> list = order.getItems();
        if ( list != null ) {
            b2BOrderVModel.setItems( new ArrayList<B2BOrderItem>( list ) );
        }
        else {
            b2BOrderVModel.setItems( null );
        }
        b2BOrderVModel.setDescription( order.getDescription() );
        b2BOrderVModel.setAbnormalOrderFlag( order.getAbnormalOrderFlag() );
        b2BOrderVModel.setBuyDate( order.getBuyDate() );
        b2BOrderVModel.setExpectServiceTime( order.getExpectServiceTime() );
        b2BOrderVModel.setSaleChannel( order.getSaleChannel() );
        b2BOrderVModel.setSiteCode( order.getSiteCode() );
        b2BOrderVModel.setSiteName( order.getSiteName() );
        b2BOrderVModel.setEngineerName( order.getEngineerName() );
        b2BOrderVModel.setEngineerMobile( order.getEngineerMobile() );
        b2BOrderVModel.setOldKklOrderNo( order.getOldKklOrderNo() );

        return b2BOrderVModel;
    }
}
