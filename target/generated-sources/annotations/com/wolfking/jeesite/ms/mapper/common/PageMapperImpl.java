package com.wolfking.jeesite.ms.mapper.common;

import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-16T18:08:05+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class PageMapperImpl implements PageMapper {

    @Override
    public Page toPage(MSPage entity) {
        if ( entity == null ) {
            return null;
        }

        Page page = new Page();

        page.setCount( entity.getRowCount() );
        page.setPageNo( entity.getPageNo() );
        page.setPageSize( entity.getPageSize() );
        List list = entity.getList();
        if ( list != null ) {
            page.setList( new ArrayList( list ) );
        }
        else {
            page.setList( null );
        }
        page.setOrderBy( entity.getOrderBy() );

        return page;
    }

    @Override
    public MSPage toMSPage(Page entity) {
        if ( entity == null ) {
            return null;
        }

        MSPage mSPage = new MSPage();

        mSPage.setRowCount( entity.getCount() );
        mSPage.setPageNo( entity.getPageNo() );
        mSPage.setPageSize( entity.getPageSize() );
        mSPage.setPageCount( entity.getPageCount() );
        List list = entity.getList();
        if ( list != null ) {
            mSPage.setList( new ArrayList( list ) );
        }
        else {
            mSPage.setList( null );
        }
        mSPage.setOrderBy( entity.getOrderBy() );

        return mSPage;
    }
}
