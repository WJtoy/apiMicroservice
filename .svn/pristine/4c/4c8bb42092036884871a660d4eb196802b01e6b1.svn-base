package com.wolfking.jeesite.ms.praise.entity.mapper;


import com.kkl.kklplus.entity.praise.Praise;
import com.kkl.kklplus.entity.praise.PraiseListModel;
import com.kkl.kklplus.entity.praise.PraiseStatusEnum;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.service.CustomerService;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import com.wolfking.jeesite.ms.praise.entity.ViewPraiseModel;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mapstruct.*;

import java.util.List;

/**
 * 好评费实体转换
 */
@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class PraiseModelMapper {

    private static String DateFormat = "yyyy-MM-dd HH:mm:ss";

    private static CustomerService customerService = SpringContextHolder.getBean(CustomerService.class);

    /**
     * 数据转视图模型
     */
    public abstract ViewPraiseModel toViewModel(PraiseListModel model);


    /**
     * Praise 转 ViewPraiseModel
     */
    public abstract ViewPraiseModel PraiseToViewModel(Praise model);


    @AfterMapping
    protected void after(@MappingTarget final ViewPraiseModel viewModel, PraiseListModel model){
       if(model.getStatus()!=null && model.getStatus()>0){
           PraiseStatusEnum praiseStatusEnum = PraiseStatusEnum.fromCode(model.getStatus());
           if(praiseStatusEnum!=null){
               viewModel.setStrStatus(praiseStatusEnum.msg);
           }
       }
       if(model.getCustomerId()!=null && model.getCustomerId()>0){
           Customer customer =  customerService.getFromCache(model.getCustomerId());
           if(customer!=null){
               viewModel.setCustomerName(customer.getName());
           }
       }
       if(model.getCreateDt()!=null && model.getCreateDt()>0){
           viewModel.setApplyTime(DateFormatUtils.format(model.getCreateDt(), DateFormat));
       }

       if(model.getCreateById()!=null && model.getCreateById()>0){
           User createBy = UserUtils.get(model.getCreateById());
           if(createBy!=null){
               viewModel.setApplyName(createBy.getName());
           }
       }

       if(model.getUpdateDt()!=null && model.getUpdateDt()>0){
           viewModel.setStrUpdateDate(DateFormatUtils.format(model.getUpdateDt(), DateFormat));
       }

       if(model.getUpdateById()!=null && model.getUpdateById()>0){
           User updateBy = UserUtils.get(model.getUpdateById());
           if(updateBy!=null){
               viewModel.setStrUpdateName(updateBy.getName());
           }
       }
       if(model.getPics()!=null && model.getPics().size()>0){
           viewModel.setPics(model.getPics());
       }
    }

    /**
     * 转List
     */
    public abstract List<ViewPraiseModel> toViewModels(List<PraiseListModel> models);

}
