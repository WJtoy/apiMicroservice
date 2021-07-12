package com.wolfking.jeesite.common.persistence;

/**
 * Created by F1053169 on 2016/5/27.
 */

import java.util.List;

/**
 * DAO支持类实现
 * @author ThinkGem
 * @version 2014-05-16
 * @param <T>
 */
//zhoucy: 新增
public interface LongIDTreeDao<T extends LongIDTreeEntity<T>> extends LongIDCrudDao<T> {

    /**
     * 找到所有子节点
     * @param entity
     * @return
     */
    public List<T> findByParentIdsLike(T entity);

    /**
     * 找到自己及所有子节点
     * @param entity
     * @return
     */
    public List<T> findSelfAndChidrens(T entity);

    /**
     * 更新所有父节点字段
     * @param entity
     * @return
     */
    public int updateParentIds(T entity);


}
