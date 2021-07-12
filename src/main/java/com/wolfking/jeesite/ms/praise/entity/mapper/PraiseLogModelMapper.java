package com.wolfking.jeesite.ms.praise.entity.mapper;
import com.kkl.kklplus.entity.praise.PraiseLog;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import com.wolfking.jeesite.ms.praise.entity.PraiseLogModel;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mapstruct.*;

import java.util.List;

/**
 * 好评费实体转换
 */
@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class PraiseLogModelMapper {

    private static String DateFormat = "yyyy-MM-dd HH:mm:ss";


    /**
     * 数据转视图模型
     */
    public abstract PraiseLogModel toViewModel(PraiseLog model);

    @AfterMapping
    protected void after(@MappingTarget final PraiseLogModel viewModel, PraiseLog model){
       if(model.getCreateDt()!=null && model.getCreateDt()>0){
           viewModel.setStrCreateDate(DateFormatUtils.format(model.getCreateDt(), DateFormat));
       }
       if(model.getCreateById()!=null && model.getCreateById()>0){
           User createBy = UserUtils.get(model.getCreateById());
           if(createBy!=null){
               viewModel.setCreateName(createBy.getName());
           }
       }
    }

    /**
     * 转List
     */
    public abstract List<PraiseLogModel> toViewModels(List<PraiseLog> models);

}
