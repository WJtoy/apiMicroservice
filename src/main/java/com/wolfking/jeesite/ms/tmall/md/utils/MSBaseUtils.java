package com.wolfking.jeesite.ms.tmall.md.utils;

import com.kkl.kklplus.entity.common.MSBase;

public class MSBaseUtils {

    /**
     * 为实体设置创建者、创建时间、更新者、更新时间
     * @param baseEntity
     */
    public static void preInsert(MSBase baseEntity) {
        baseEntity.preInsert();
//        User user = UserUtils.getUser();
//        if (user.getId() != null) {
//            baseEntity.setCreateById(user.getId());
//            baseEntity.setUpdateById(user.getId());
//        }
//        else {
//            baseEntity.setCreateById(0L);
//            baseEntity.setUpdateById(0L);
//        }
        if (baseEntity.getCreateById() == null) {
            baseEntity.setCreateById(0L);
        }
        if (baseEntity.getUpdateById() == null) {
            baseEntity.setUpdateById(0L);
        }
    }

}
