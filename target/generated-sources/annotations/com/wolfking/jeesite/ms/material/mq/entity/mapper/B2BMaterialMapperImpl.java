package com.wolfking.jeesite.ms.material.mq.entity.mapper;

import com.kkl.kklplus.entity.common.material.B2BMaterial;
import com.wolfking.jeesite.modules.sd.entity.MaterialMaster;
import com.wolfking.jeesite.modules.sys.entity.User;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-16T18:08:05+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class B2BMaterialMapperImpl extends B2BMaterialMapper {

    @Override
    public B2BMaterial toB2BMaterialForm(MaterialMaster model) {
        if ( model == null ) {
            return null;
        }

        B2BMaterial b2BMaterial = new B2BMaterial();

        if ( model.getMasterNo() != null ) {
            b2BMaterial.setKklMasterNo( model.getMasterNo() );
        }
        if ( model.getRemarks() != null ) {
            b2BMaterial.setDescription( model.getRemarks() );
        }
        if ( model.getOrderId() != null ) {
            b2BMaterial.setKklOrderId( model.getOrderId() );
        }
        if ( model.getReturnFlag() != null ) {
            b2BMaterial.setReturnFlag( model.getReturnFlag() );
        }
        if ( model.getOrderNo() != null ) {
            b2BMaterial.setKklOrderNo( model.getOrderNo() );
        }
        if ( model.getUserPhone() != null ) {
            b2BMaterial.setUserMobile( model.getUserPhone() );
        }
        if ( model.getThrdNo() != null ) {
            b2BMaterial.setOrderNo( model.getThrdNo() );
        }
        if ( model.getId() != null ) {
            b2BMaterial.setKklMasterId( model.getId() );
        }
        if ( model.getUserName() != null ) {
            b2BMaterial.setUserName( model.getUserName() );
        }
        Long id = modelCreateById( model );
        if ( id != null ) {
            b2BMaterial.setUpdateById( id );
        }
        if ( model.getUserAddress() != null ) {
            b2BMaterial.setUserAddress( model.getUserAddress() );
        }
        b2BMaterial.setMasterCount( (long) model.getApplyTime() );
        Long id1 = modelCreateById( model );
        if ( id1 != null ) {
            b2BMaterial.setCreateById( id1 );
        }
        if ( model.getQuarter() != null ) {
            b2BMaterial.setQuarter( model.getQuarter() );
        }
        if ( model.getId() != null ) {
            b2BMaterial.setId( model.getId() );
        }
        if ( model.getRemarks() != null ) {
            b2BMaterial.setRemarks( model.getRemarks() );
        }
        if ( model.getB2bOrderId() != null ) {
            b2BMaterial.setB2bOrderId( model.getB2bOrderId() );
        }

        b2BMaterial.setCreateDt( model.getCreateDate().getTime() );
        b2BMaterial.setStatus( Integer.parseInt( "1" ) );

        after( b2BMaterial, model );

        return b2BMaterial;
    }

    private Long modelCreateById(MaterialMaster materialMaster) {
        if ( materialMaster == null ) {
            return null;
        }
        User createBy = materialMaster.getCreateBy();
        if ( createBy == null ) {
            return null;
        }
        Long id = createBy.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
