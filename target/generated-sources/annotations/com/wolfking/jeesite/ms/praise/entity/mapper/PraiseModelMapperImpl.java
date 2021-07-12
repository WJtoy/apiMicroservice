package com.wolfking.jeesite.ms.praise.entity.mapper;

import com.kkl.kklplus.entity.praise.Praise;
import com.kkl.kklplus.entity.praise.PraiseListModel;
import com.kkl.kklplus.entity.praise.PraisePicItem;
import com.wolfking.jeesite.ms.praise.entity.ViewPraiseModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-16T18:08:06+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class PraiseModelMapperImpl extends PraiseModelMapper {

    @Override
    public ViewPraiseModel toViewModel(PraiseListModel model) {
        if ( model == null ) {
            return null;
        }

        ViewPraiseModel viewPraiseModel = new ViewPraiseModel();

        viewPraiseModel.setId( model.getId() );
        if ( model.getRemarks() != null ) {
            viewPraiseModel.setRemarks( model.getRemarks() );
        }
        if ( model.getCreateById() != null ) {
            viewPraiseModel.setCreateById( model.getCreateById() );
        }
        if ( model.getUpdateById() != null ) {
            viewPraiseModel.setUpdateById( model.getUpdateById() );
        }
        if ( model.getCreateDt() != null ) {
            viewPraiseModel.setCreateDt( model.getCreateDt() );
        }
        if ( model.getUpdateDt() != null ) {
            viewPraiseModel.setUpdateDt( model.getUpdateDt() );
        }
        if ( model.getQuarter() != null ) {
            viewPraiseModel.setQuarter( model.getQuarter() );
        }
        if ( model.getProductNames() != null ) {
            viewPraiseModel.setProductNames( model.getProductNames() );
        }
        if ( model.getPraiseNo() != null ) {
            viewPraiseModel.setPraiseNo( model.getPraiseNo() );
        }
        if ( model.getStatus() != null ) {
            viewPraiseModel.setStatus( model.getStatus() );
        }
        if ( model.getOrderId() != null ) {
            viewPraiseModel.setOrderId( model.getOrderId() );
        }
        if ( model.getOrderNo() != null ) {
            viewPraiseModel.setOrderNo( model.getOrderNo() );
        }
        if ( model.getWorkcardId() != null ) {
            viewPraiseModel.setWorkcardId( model.getWorkcardId() );
        }
        if ( model.getParentBizOrderId() != null ) {
            viewPraiseModel.setParentBizOrderId( model.getParentBizOrderId() );
        }
        if ( model.getCustomerId() != null ) {
            viewPraiseModel.setCustomerId( model.getCustomerId() );
        }
        if ( model.getUserName() != null ) {
            viewPraiseModel.setUserName( model.getUserName() );
        }
        if ( model.getUserPhone() != null ) {
            viewPraiseModel.setUserPhone( model.getUserPhone() );
        }
        if ( model.getUserAddress() != null ) {
            viewPraiseModel.setUserAddress( model.getUserAddress() );
        }
        if ( model.getCustomerPraiseFee() != null ) {
            viewPraiseModel.setCustomerPraiseFee( model.getCustomerPraiseFee() );
        }
        if ( model.getServicepointPraiseFee() != null ) {
            viewPraiseModel.setServicepointPraiseFee( model.getServicepointPraiseFee() );
        }
        if ( model.getApplyCustomerPraiseFee() != null ) {
            viewPraiseModel.setApplyCustomerPraiseFee( model.getApplyCustomerPraiseFee() );
        }
        if ( model.getApplyServicepointPraiseFee() != null ) {
            viewPraiseModel.setApplyServicepointPraiseFee( model.getApplyServicepointPraiseFee() );
        }
        if ( model.getApproveTimeLiness() != null ) {
            viewPraiseModel.setApproveTimeLiness( model.getApproveTimeLiness() );
        }
        if ( model.getTimeoutAt() != null ) {
            viewPraiseModel.setTimeoutAt( model.getTimeoutAt() );
        }
        if ( model.getRejectionCategory() != null ) {
            viewPraiseModel.setRejectionCategory( model.getRejectionCategory() );
        }
        if ( model.getPicsJson() != null ) {
            viewPraiseModel.setPicsJson( model.getPicsJson() );
        }
        List<String> list = model.getPics();
        if ( list != null ) {
            viewPraiseModel.setPics( new ArrayList<String>( list ) );
        }
        else {
            viewPraiseModel.setPics( null );
        }
        if ( model.getPicItemsJson() != null ) {
            viewPraiseModel.setPicItemsJson( model.getPicItemsJson() );
        }
        List<PraisePicItem> list1 = model.getPicItems();
        if ( list1 != null ) {
            viewPraiseModel.setPicItems( new ArrayList<PraisePicItem>( list1 ) );
        }
        else {
            viewPraiseModel.setPicItems( null );
        }

        after( viewPraiseModel, model );

        return viewPraiseModel;
    }

    @Override
    public ViewPraiseModel PraiseToViewModel(Praise model) {
        if ( model == null ) {
            return null;
        }

        ViewPraiseModel viewPraiseModel = new ViewPraiseModel();

        if ( model.getId() != null ) {
            viewPraiseModel.setId( model.getId() );
        }
        if ( model.getRemarks() != null ) {
            viewPraiseModel.setRemarks( model.getRemarks() );
        }
        if ( model.getCreateBy() != null ) {
            viewPraiseModel.setCreateBy( model.getCreateBy() );
        }
        if ( model.getCreateById() != null ) {
            viewPraiseModel.setCreateById( model.getCreateById() );
        }
        if ( model.getCreateDate() != null ) {
            viewPraiseModel.setCreateDate( model.getCreateDate() );
        }
        if ( model.getUpdateBy() != null ) {
            viewPraiseModel.setUpdateBy( model.getUpdateBy() );
        }
        if ( model.getUpdateById() != null ) {
            viewPraiseModel.setUpdateById( model.getUpdateById() );
        }
        if ( model.getUpdateDate() != null ) {
            viewPraiseModel.setUpdateDate( model.getUpdateDate() );
        }
        if ( model.getDelFlag() != null ) {
            viewPraiseModel.setDelFlag( model.getDelFlag() );
        }
        if ( model.getPage() != null ) {
            viewPraiseModel.setPage( model.getPage() );
        }
        if ( model.getCreateDt() != null ) {
            viewPraiseModel.setCreateDt( model.getCreateDt() );
        }
        if ( model.getUpdateDt() != null ) {
            viewPraiseModel.setUpdateDt( model.getUpdateDt() );
        }
        if ( model.getQuarter() != null ) {
            viewPraiseModel.setQuarter( model.getQuarter() );
        }
        if ( model.getDataSource() != null ) {
            viewPraiseModel.setDataSource( model.getDataSource() );
        }
        if ( model.getProductCategoryId() != null ) {
            viewPraiseModel.setProductCategoryId( model.getProductCategoryId() );
        }
        if ( model.getProductNames() != null ) {
            viewPraiseModel.setProductNames( model.getProductNames() );
        }
        if ( model.getPraiseNo() != null ) {
            viewPraiseModel.setPraiseNo( model.getPraiseNo() );
        }
        if ( model.getStatus() != null ) {
            viewPraiseModel.setStatus( model.getStatus() );
        }
        if ( model.getAuditFlag() != null ) {
            viewPraiseModel.setAuditFlag( model.getAuditFlag() );
        }
        if ( model.getOrderId() != null ) {
            viewPraiseModel.setOrderId( model.getOrderId() );
        }
        if ( model.getOrderNo() != null ) {
            viewPraiseModel.setOrderNo( model.getOrderNo() );
        }
        if ( model.getWorkcardId() != null ) {
            viewPraiseModel.setWorkcardId( model.getWorkcardId() );
        }
        if ( model.getParentBizOrderId() != null ) {
            viewPraiseModel.setParentBizOrderId( model.getParentBizOrderId() );
        }
        if ( model.getOrderCloseAt() != null ) {
            viewPraiseModel.setOrderCloseAt( model.getOrderCloseAt() );
        }
        if ( model.getProvinceId() != null ) {
            viewPraiseModel.setProvinceId( model.getProvinceId() );
        }
        if ( model.getCityId() != null ) {
            viewPraiseModel.setCityId( model.getCityId() );
        }
        if ( model.getAreaId() != null ) {
            viewPraiseModel.setAreaId( model.getAreaId() );
        }
        if ( model.getSubAreaId() != null ) {
            viewPraiseModel.setSubAreaId( model.getSubAreaId() );
        }
        if ( model.getCustomerId() != null ) {
            viewPraiseModel.setCustomerId( model.getCustomerId() );
        }
        if ( model.getCustomerPaymentType() != null ) {
            viewPraiseModel.setCustomerPaymentType( model.getCustomerPaymentType() );
        }
        if ( model.getKefuId() != null ) {
            viewPraiseModel.setKefuId( model.getKefuId() );
        }
        if ( model.getServicepointId() != null ) {
            viewPraiseModel.setServicepointId( model.getServicepointId() );
        }
        if ( model.getEngineerId() != null ) {
            viewPraiseModel.setEngineerId( model.getEngineerId() );
        }
        if ( model.getUserName() != null ) {
            viewPraiseModel.setUserName( model.getUserName() );
        }
        if ( model.getUserPhone() != null ) {
            viewPraiseModel.setUserPhone( model.getUserPhone() );
        }
        if ( model.getUserAddress() != null ) {
            viewPraiseModel.setUserAddress( model.getUserAddress() );
        }
        if ( model.getCustomerPraiseFee() != null ) {
            viewPraiseModel.setCustomerPraiseFee( model.getCustomerPraiseFee() );
        }
        if ( model.getServicepointPraiseFee() != null ) {
            viewPraiseModel.setServicepointPraiseFee( model.getServicepointPraiseFee() );
        }
        if ( model.getApplyCustomerPraiseFee() != null ) {
            viewPraiseModel.setApplyCustomerPraiseFee( model.getApplyCustomerPraiseFee() );
        }
        if ( model.getApplyServicepointPraiseFee() != null ) {
            viewPraiseModel.setApplyServicepointPraiseFee( model.getApplyServicepointPraiseFee() );
        }
        if ( model.getApproveTimeLiness() != null ) {
            viewPraiseModel.setApproveTimeLiness( model.getApproveTimeLiness() );
        }
        if ( model.getTimeoutAt() != null ) {
            viewPraiseModel.setTimeoutAt( model.getTimeoutAt() );
        }
        if ( model.getRejectionCategory() != null ) {
            viewPraiseModel.setRejectionCategory( model.getRejectionCategory() );
        }
        if ( model.getPraiseLog() != null ) {
            viewPraiseModel.setPraiseLog( model.getPraiseLog() );
        }
        if ( model.getPicsJson() != null ) {
            viewPraiseModel.setPicsJson( model.getPicsJson() );
        }
        List<String> list = model.getPics();
        if ( list != null ) {
            viewPraiseModel.setPics( new ArrayList<String>( list ) );
        }
        else {
            viewPraiseModel.setPics( null );
        }
        if ( model.getCreatorType() != null ) {
            viewPraiseModel.setCreatorType( model.getCreatorType() );
        }
        if ( model.getPicItemsJson() != null ) {
            viewPraiseModel.setPicItemsJson( model.getPicItemsJson() );
        }
        List<PraisePicItem> list1 = model.getPicItems();
        if ( list1 != null ) {
            viewPraiseModel.setPicItems( new ArrayList<PraisePicItem>( list1 ) );
        }
        else {
            viewPraiseModel.setPicItems( null );
        }
        if ( model.getCanRush() != null ) {
            viewPraiseModel.setCanRush( model.getCanRush() );
        }
        if ( model.getKefuType() != null ) {
            viewPraiseModel.setKefuType( model.getKefuType() );
        }

        return viewPraiseModel;
    }

    @Override
    public List<ViewPraiseModel> toViewModels(List<PraiseListModel> models) {
        if ( models == null ) {
            return null;
        }

        List<ViewPraiseModel> list = new ArrayList<ViewPraiseModel>( models.size() );
        for ( PraiseListModel praiseListModel : models ) {
            list.add( toViewModel( praiseListModel ) );
        }

        return list;
    }
}
