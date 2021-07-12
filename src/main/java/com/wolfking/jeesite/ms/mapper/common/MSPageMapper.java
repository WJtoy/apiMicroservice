package com.wolfking.jeesite.ms.mapper.common;

import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MSPageMapper<T> extends CustomMapper<MSPage<T>, Page<T>> {

    @Override
    public void mapAtoB(MSPage<T> a, Page<T> b, MappingContext context) {
        b.setPageNo(a.getPageNo());
        b.setPageSize(a.getPageSize());
        b.setCount(a.getRowCount());
        b.setOrderBy(a.getOrderBy());
        b.setList(a.getList());
    }

    @Override
    public void mapBtoA(Page<T> b, MSPage<T> a, MappingContext context) {
        a.setPageNo(b.getPageNo());
        a.setPageSize(b.getPageSize());
        a.setPageCount(b.getPageCount());
        a.setRowCount(b.getCount());
        a.setOrderBy(b.getOrderBy());
        a.setList(b.getList());
    }
}
