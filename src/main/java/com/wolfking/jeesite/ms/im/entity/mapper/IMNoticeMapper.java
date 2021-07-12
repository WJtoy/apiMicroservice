package com.wolfking.jeesite.ms.im.entity.mapper;


import com.kkl.kklplus.entity.sys.IMNoticeInfo;
import com.wolfking.jeesite.common.utils.BitUtils;
import com.wolfking.jeesite.ms.im.entity.IMNoticeModel;
import com.wolfking.jeesite.ms.im.entity.IMNoticeNewModel;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @autor Ryan Lu
 * @date 2019/5/10 2:56 PM
 * 公告与消息队列数据模型转换
 */
@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class IMNoticeMapper {

    public static final Map<Integer,String> userTypesMap = Arrays.stream(IMNoticeInfo.UserType.values())
            .collect(Collectors.toMap(IMNoticeInfo.UserType::getCode, item -> item.getName()));

    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "userTypes",ignore = true)
    })
    public abstract IMNoticeInfo viewModelToBean(IMNoticeNewModel model);

    @Mappings({
            @Mapping(target = "userTypeLabels",ignore = true),
    })
    public abstract IMNoticeModel beanToViewModel(IMNoticeInfo bean);

    @AfterMapping
    protected void after(@MappingTarget final IMNoticeModel model, IMNoticeInfo bean) {
        if(bean != null){
            List<Integer> userTypes = BitUtils.getPositions(Long.valueOf(bean.getUserTypes()).intValue(),Integer.class);
            int size = userTypes.size();
            if(size>0) {
                StringBuilder sb = new StringBuilder(100);
                for (int i = 0; i < size; i++) {
                    if (userTypesMap.containsKey(userTypes.get(i))) {
                        if(sb.length()>0){
                            sb.append(",");
                        }
                        sb.append(userTypesMap.get(userTypes.get(i)));
                    }
                }
                if (sb.length() > 0) {
                    model.setUserTypeLabels(sb.toString());
                }
            }
        }
    }
}
