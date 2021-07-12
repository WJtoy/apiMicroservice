package com.wolfking.jeesite.modules.md.utils;

import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-16T18:08:05+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class ProductCompletePicItemMapperImpl extends ProductCompletePicItemMapper {

    @Override
    public ProductCompletePicItem toPicItem(Dict entity) {
        if ( entity == null ) {
            return null;
        }

        ProductCompletePicItem productCompletePicItem = new ProductCompletePicItem();

        productCompletePicItem.setPictureCode( entity.getValue() );
        productCompletePicItem.setSort( entity.getSort() );
        productCompletePicItem.setTitle( entity.getLabel() );
        productCompletePicItem.setRemarks( entity.getDescription() );

        return productCompletePicItem;
    }

    @Override
    public List<ProductCompletePicItem> listToPicItem(List<Dict> dicts) {
        if ( dicts == null ) {
            return null;
        }

        List<ProductCompletePicItem> list = new ArrayList<ProductCompletePicItem>( dicts.size() );
        for ( Dict dict : dicts ) {
            list.add( toPicItem( dict ) );
        }

        return list;
    }
}
