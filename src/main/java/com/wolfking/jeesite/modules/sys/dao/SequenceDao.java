package com.wolfking.jeesite.modules.sys.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sys.entity.Sequence;
import org.apache.ibatis.annotations.Mapper;

/**
 * 序号的数据访问类
 * Created on 2017-04-18.
 */
@Mapper
public interface SequenceDao extends LongIDCrudDao<Sequence> {
    /**
     * 按序号编码获取序号信息
     * @param code
     * @return
     */
    Sequence getByCode(String code);

    /**
     * 更新前一顺序号
     * @param entity
     */
    void updatePreviousDigit(Sequence entity);

    /**
     * 通过序号名称获取自增iD值
     * @param sequence
     * @return
     */
    long NextIDValue(Sequence sequence);
}
