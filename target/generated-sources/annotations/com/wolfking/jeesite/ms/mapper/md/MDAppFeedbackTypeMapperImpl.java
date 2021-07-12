package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDAppFeedbackType;
import com.kkl.kklplus.entity.md.dto.MDAppFeedbackTypeDto;
import com.wolfking.jeesite.ms.providermd.entity.AppFeedbackTypeVModel;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-16T18:08:06+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class MDAppFeedbackTypeMapperImpl implements MDAppFeedbackTypeMapper {

    @Override
    public AppFeedbackTypeVModel toAppFeedbackTypeVModel(MDAppFeedbackTypeDto appFeedbackTypeDto) {
        if ( appFeedbackTypeDto == null ) {
            return null;
        }

        AppFeedbackTypeVModel appFeedbackTypeVModel = new AppFeedbackTypeVModel();

        appFeedbackTypeVModel.setId( appFeedbackTypeDto.getId() );
        appFeedbackTypeVModel.setRemarks( appFeedbackTypeDto.getRemarks() );
        appFeedbackTypeVModel.setCreateBy( appFeedbackTypeDto.getCreateBy() );
        appFeedbackTypeVModel.setCreateById( appFeedbackTypeDto.getCreateById() );
        appFeedbackTypeVModel.setCreateDate( appFeedbackTypeDto.getCreateDate() );
        appFeedbackTypeVModel.setUpdateBy( appFeedbackTypeDto.getUpdateBy() );
        appFeedbackTypeVModel.setUpdateById( appFeedbackTypeDto.getUpdateById() );
        appFeedbackTypeVModel.setUpdateDate( appFeedbackTypeDto.getUpdateDate() );
        appFeedbackTypeVModel.setDelFlag( appFeedbackTypeDto.getDelFlag() );
        appFeedbackTypeVModel.setPage( appFeedbackTypeDto.getPage() );
        appFeedbackTypeVModel.setParentId( appFeedbackTypeDto.getParentId() );
        appFeedbackTypeVModel.setParentName( appFeedbackTypeDto.getParentName() );
        appFeedbackTypeVModel.setFeedbackType( appFeedbackTypeDto.getFeedbackType() );
        appFeedbackTypeVModel.setHasChildren( appFeedbackTypeDto.getHasChildren() );
        appFeedbackTypeVModel.setValue( appFeedbackTypeDto.getValue() );
        appFeedbackTypeVModel.setName( appFeedbackTypeDto.getName() );
        appFeedbackTypeVModel.setLabel( appFeedbackTypeDto.getLabel() );
        appFeedbackTypeVModel.setIsEffect( appFeedbackTypeDto.getIsEffect() );
        appFeedbackTypeVModel.setIsAbnormaly( appFeedbackTypeDto.getIsAbnormaly() );
        appFeedbackTypeVModel.setSortBy( appFeedbackTypeDto.getSortBy() );
        appFeedbackTypeVModel.setAbnormalyOverTimes( appFeedbackTypeDto.getAbnormalyOverTimes() );
        appFeedbackTypeVModel.setActionType( appFeedbackTypeDto.getActionType() );
        appFeedbackTypeVModel.setUserType( appFeedbackTypeDto.getUserType() );
        appFeedbackTypeVModel.setSumType( appFeedbackTypeDto.getSumType() );
        appFeedbackTypeVModel.setCreateAt( appFeedbackTypeDto.getCreateAt() );
        appFeedbackTypeVModel.setUpdateAt( appFeedbackTypeDto.getUpdateAt() );

        return appFeedbackTypeVModel;
    }

    @Override
    public AppFeedbackTypeVModel FeedbackTypeToVModel(MDAppFeedbackType appFeedbackType) {
        if ( appFeedbackType == null ) {
            return null;
        }

        AppFeedbackTypeVModel appFeedbackTypeVModel = new AppFeedbackTypeVModel();

        appFeedbackTypeVModel.setId( appFeedbackType.getId() );
        appFeedbackTypeVModel.setRemarks( appFeedbackType.getRemarks() );
        appFeedbackTypeVModel.setCreateBy( appFeedbackType.getCreateBy() );
        appFeedbackTypeVModel.setCreateById( appFeedbackType.getCreateById() );
        appFeedbackTypeVModel.setCreateDate( appFeedbackType.getCreateDate() );
        appFeedbackTypeVModel.setUpdateBy( appFeedbackType.getUpdateBy() );
        appFeedbackTypeVModel.setUpdateById( appFeedbackType.getUpdateById() );
        appFeedbackTypeVModel.setUpdateDate( appFeedbackType.getUpdateDate() );
        appFeedbackTypeVModel.setDelFlag( appFeedbackType.getDelFlag() );
        appFeedbackTypeVModel.setPage( appFeedbackType.getPage() );
        appFeedbackTypeVModel.setParentId( appFeedbackType.getParentId() );
        appFeedbackTypeVModel.setParentName( appFeedbackType.getParentName() );
        appFeedbackTypeVModel.setFeedbackType( appFeedbackType.getFeedbackType() );
        appFeedbackTypeVModel.setHasChildren( appFeedbackType.getHasChildren() );
        appFeedbackTypeVModel.setValue( appFeedbackType.getValue() );
        appFeedbackTypeVModel.setName( appFeedbackType.getName() );
        appFeedbackTypeVModel.setLabel( appFeedbackType.getLabel() );
        appFeedbackTypeVModel.setIsEffect( appFeedbackType.getIsEffect() );
        appFeedbackTypeVModel.setIsAbnormaly( appFeedbackType.getIsAbnormaly() );
        appFeedbackTypeVModel.setSortBy( appFeedbackType.getSortBy() );
        appFeedbackTypeVModel.setAbnormalyOverTimes( appFeedbackType.getAbnormalyOverTimes() );
        appFeedbackTypeVModel.setActionType( appFeedbackType.getActionType() );
        appFeedbackTypeVModel.setUserType( appFeedbackType.getUserType() );
        appFeedbackTypeVModel.setSumType( appFeedbackType.getSumType() );
        appFeedbackTypeVModel.setCreateAt( appFeedbackType.getCreateAt() );
        appFeedbackTypeVModel.setUpdateAt( appFeedbackType.getUpdateAt() );

        return appFeedbackTypeVModel;
    }
}
