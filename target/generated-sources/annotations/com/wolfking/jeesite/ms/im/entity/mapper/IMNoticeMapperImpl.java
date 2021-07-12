package com.wolfking.jeesite.ms.im.entity.mapper;

import com.kkl.kklplus.entity.sys.IMNoticeInfo;
import com.wolfking.jeesite.ms.im.entity.IMNoticeModel;
import com.wolfking.jeesite.ms.im.entity.IMNoticeNewModel;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-16T18:08:05+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class IMNoticeMapperImpl extends IMNoticeMapper {

    @Override
    public IMNoticeInfo viewModelToBean(IMNoticeNewModel model) {
        if ( model == null ) {
            return null;
        }

        IMNoticeInfo iMNoticeInfo = new IMNoticeInfo();

        if ( model.getId() != null ) {
            iMNoticeInfo.setId( model.getId() );
        }
        if ( model.getRemarks() != null ) {
            iMNoticeInfo.setRemarks( model.getRemarks() );
        }
        if ( model.getCreateBy() != null ) {
            iMNoticeInfo.setCreateBy( model.getCreateBy() );
        }
        if ( model.getCreateById() != null ) {
            iMNoticeInfo.setCreateById( model.getCreateById() );
        }
        if ( model.getCreateDate() != null ) {
            iMNoticeInfo.setCreateDate( model.getCreateDate() );
        }
        if ( model.getUpdateBy() != null ) {
            iMNoticeInfo.setUpdateBy( model.getUpdateBy() );
        }
        if ( model.getUpdateById() != null ) {
            iMNoticeInfo.setUpdateById( model.getUpdateById() );
        }
        if ( model.getUpdateDate() != null ) {
            iMNoticeInfo.setUpdateDate( model.getUpdateDate() );
        }
        if ( model.getDelFlag() != null ) {
            iMNoticeInfo.setDelFlag( model.getDelFlag() );
        }
        if ( model.getPage() != null ) {
            iMNoticeInfo.setPage( model.getPage() );
        }
        if ( model.getTitle() != null ) {
            iMNoticeInfo.setTitle( model.getTitle() );
        }
        if ( model.getSubTitle() != null ) {
            iMNoticeInfo.setSubTitle( model.getSubTitle() );
        }
        if ( model.getContent() != null ) {
            iMNoticeInfo.setContent( model.getContent() );
        }
        iMNoticeInfo.setCreateAt( model.getCreateAt() );
        if ( model.getIsCanceled() != null ) {
            iMNoticeInfo.setIsCanceled( model.getIsCanceled() );
        }
        iMNoticeInfo.setCancelAt( model.getCancelAt() );
        iMNoticeInfo.setNoticeType( model.getNoticeType() );

        return iMNoticeInfo;
    }

    @Override
    public IMNoticeModel beanToViewModel(IMNoticeInfo bean) {
        if ( bean == null ) {
            return null;
        }

        IMNoticeModel iMNoticeModel = new IMNoticeModel();

        if ( bean.getId() != null ) {
            iMNoticeModel.setId( bean.getId() );
        }
        if ( bean.getRemarks() != null ) {
            iMNoticeModel.setRemarks( bean.getRemarks() );
        }
        if ( bean.getCreateBy() != null ) {
            iMNoticeModel.setCreateBy( bean.getCreateBy() );
        }
        if ( bean.getCreateById() != null ) {
            iMNoticeModel.setCreateById( bean.getCreateById() );
        }
        if ( bean.getCreateDate() != null ) {
            iMNoticeModel.setCreateDate( bean.getCreateDate() );
        }
        if ( bean.getUpdateBy() != null ) {
            iMNoticeModel.setUpdateBy( bean.getUpdateBy() );
        }
        if ( bean.getUpdateById() != null ) {
            iMNoticeModel.setUpdateById( bean.getUpdateById() );
        }
        if ( bean.getUpdateDate() != null ) {
            iMNoticeModel.setUpdateDate( bean.getUpdateDate() );
        }
        if ( bean.getDelFlag() != null ) {
            iMNoticeModel.setDelFlag( bean.getDelFlag() );
        }
        if ( bean.getPage() != null ) {
            iMNoticeModel.setPage( bean.getPage() );
        }
        if ( bean.getTitle() != null ) {
            iMNoticeModel.setTitle( bean.getTitle() );
        }
        if ( bean.getSubTitle() != null ) {
            iMNoticeModel.setSubTitle( bean.getSubTitle() );
        }
        if ( bean.getContent() != null ) {
            iMNoticeModel.setContent( bean.getContent() );
        }
        iMNoticeModel.setUserTypes( bean.getUserTypes() );
        iMNoticeModel.setCreateAt( bean.getCreateAt() );
        if ( bean.getIsCanceled() != null ) {
            iMNoticeModel.setIsCanceled( bean.getIsCanceled() );
        }
        iMNoticeModel.setCancelAt( bean.getCancelAt() );
        iMNoticeModel.setNoticeType( bean.getNoticeType() );

        after( iMNoticeModel, bean );

        return iMNoticeModel;
    }
}
