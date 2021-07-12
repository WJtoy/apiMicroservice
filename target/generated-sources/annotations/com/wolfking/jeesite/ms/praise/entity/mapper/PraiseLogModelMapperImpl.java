package com.wolfking.jeesite.ms.praise.entity.mapper;

import com.kkl.kklplus.entity.praise.PraiseLog;
import com.wolfking.jeesite.ms.praise.entity.PraiseLogModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-16T18:08:06+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class PraiseLogModelMapperImpl extends PraiseLogModelMapper {

    @Override
    public PraiseLogModel toViewModel(PraiseLog model) {
        if ( model == null ) {
            return null;
        }

        PraiseLogModel praiseLogModel = new PraiseLogModel();

        if ( model.getId() != null ) {
            praiseLogModel.setId( model.getId() );
        }
        if ( model.getRemarks() != null ) {
            praiseLogModel.setRemarks( model.getRemarks() );
        }
        if ( model.getCreateBy() != null ) {
            praiseLogModel.setCreateBy( model.getCreateBy() );
        }
        if ( model.getCreateById() != null ) {
            praiseLogModel.setCreateById( model.getCreateById() );
        }
        if ( model.getCreateDate() != null ) {
            praiseLogModel.setCreateDate( model.getCreateDate() );
        }
        if ( model.getUpdateBy() != null ) {
            praiseLogModel.setUpdateBy( model.getUpdateBy() );
        }
        if ( model.getUpdateById() != null ) {
            praiseLogModel.setUpdateById( model.getUpdateById() );
        }
        if ( model.getUpdateDate() != null ) {
            praiseLogModel.setUpdateDate( model.getUpdateDate() );
        }
        if ( model.getDelFlag() != null ) {
            praiseLogModel.setDelFlag( model.getDelFlag() );
        }
        if ( model.getPage() != null ) {
            praiseLogModel.setPage( model.getPage() );
        }
        if ( model.getCreateDt() != null ) {
            praiseLogModel.setCreateDt( model.getCreateDt() );
        }
        if ( model.getUpdateDt() != null ) {
            praiseLogModel.setUpdateDt( model.getUpdateDt() );
        }
        if ( model.getQuarter() != null ) {
            praiseLogModel.setQuarter( model.getQuarter() );
        }
        if ( model.getPraiseId() != null ) {
            praiseLogModel.setPraiseId( model.getPraiseId() );
        }
        if ( model.getStatus() != null ) {
            praiseLogModel.setStatus( model.getStatus() );
        }
        if ( model.getActionType() != null ) {
            praiseLogModel.setActionType( model.getActionType() );
        }
        if ( model.getCreatorType() != null ) {
            praiseLogModel.setCreatorType( model.getCreatorType() );
        }
        if ( model.getTimeLiness() != null ) {
            praiseLogModel.setTimeLiness( model.getTimeLiness() );
        }
        praiseLogModel.setVisibilityFlag( model.getVisibilityFlag() );
        if ( model.getContent() != null ) {
            praiseLogModel.setContent( model.getContent() );
        }

        after( praiseLogModel, model );

        return praiseLogModel;
    }

    @Override
    public List<PraiseLogModel> toViewModels(List<PraiseLog> models) {
        if ( models == null ) {
            return null;
        }

        List<PraiseLogModel> list = new ArrayList<PraiseLogModel>( models.size() );
        for ( PraiseLog praiseLog : models ) {
            list.add( toViewModel( praiseLog ) );
        }

        return list;
    }
}
