package com.wolfking.jeesite.modules.md.entity.mapper;

import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.md.entity.CustomerMaterial;
import com.wolfking.jeesite.modules.md.entity.Material;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-16T18:08:05+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class MaterialMapperImpl extends MaterialMapper {

    @Override
    public Material customerToMaterial(CustomerMaterial model) {
        if ( model == null ) {
            return null;
        }

        Material material = new Material();

        if ( model.getIsReturn() != null ) {
            material.setIsReturn( model.getIsReturn() );
        }
        if ( model.getProduct() != null ) {
            material.setProduct( model.getProduct() );
        }
        if ( model.getPrice() != null ) {
            material.setPrice( model.getPrice() );
        }
        String name = modelMaterialName( model );
        if ( name != null ) {
            material.setName( name );
        }
        Long id = modelMaterialId( model );
        if ( id != null ) {
            material.setId( id );
        }
        if ( model.getCurrentUser() != null ) {
            material.setCurrentUser( model.getCurrentUser() );
        }
        if ( model.getPage() != null ) {
            material.setPage( customerMaterialPageToMaterialPage( model.getPage() ) );
        }
        Map<String, String> map = model.getSqlMap();
        if ( map != null ) {
            material.setSqlMap( new HashMap<String, String>( map ) );
        }
        else {
            material.setSqlMap( null );
        }
        material.setIsNewRecord( model.getIsNewRecord() );

        return material;
    }

    private String modelMaterialName(CustomerMaterial customerMaterial) {
        if ( customerMaterial == null ) {
            return null;
        }
        Material material = customerMaterial.getMaterial();
        if ( material == null ) {
            return null;
        }
        String name = material.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long modelMaterialId(CustomerMaterial customerMaterial) {
        if ( customerMaterial == null ) {
            return null;
        }
        Material material = customerMaterial.getMaterial();
        if ( material == null ) {
            return null;
        }
        Long id = material.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<Material> customerMaterialListToMaterialList(List<CustomerMaterial> list) {
        if ( list == null ) {
            return null;
        }

        List<Material> list1 = new ArrayList<Material>( list.size() );
        for ( CustomerMaterial customerMaterial : list ) {
            list1.add( customerToMaterial( customerMaterial ) );
        }

        return list1;
    }

    protected Page<Material> customerMaterialPageToMaterialPage(Page<CustomerMaterial> page) {
        if ( page == null ) {
            return null;
        }

        Page<Material> page1 = new Page<Material>();

        page1.setCount( page.getCount() );
        page1.setPageNo( page.getPageNo() );
        page1.setPageSize( page.getPageSize() );
        List<Material> list = customerMaterialListToMaterialList( page.getList() );
        if ( list != null ) {
            page1.setList( list );
        }
        else {
            page1.setList( null );
        }
        Map map = page.getMap();
        if ( map != null ) {
            page1.setMap( new HashMap( map ) );
        }
        else {
            page1.setMap( null );
        }
        if ( page.getOrderBy() != null ) {
            page1.setOrderBy( page.getOrderBy() );
        }
        if ( page.getFuncName() != null ) {
            page1.setFuncName( page.getFuncName() );
        }
        if ( page.getFuncParam() != null ) {
            page1.setFuncParam( page.getFuncParam() );
        }

        return page1;
    }
}
