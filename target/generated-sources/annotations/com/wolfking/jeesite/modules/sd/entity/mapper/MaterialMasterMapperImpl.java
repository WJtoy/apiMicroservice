package com.wolfking.jeesite.modules.sd.entity.mapper;

import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.sd.entity.MaterialItem;
import com.wolfking.jeesite.modules.sd.entity.MaterialMaster;
import com.wolfking.jeesite.modules.sd.entity.MaterialProduct;
import com.wolfking.jeesite.modules.sd.entity.MaterialReturn;
import com.wolfking.jeesite.modules.sd.entity.MaterialReturnItem;
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
public class MaterialMasterMapperImpl extends MaterialMasterMapper {

    @Override
    public MaterialMaster clone(MaterialMaster model) {
        if ( model == null ) {
            return null;
        }

        MaterialMaster materialMaster = new MaterialMaster();

        if ( model.getId() != null ) {
            materialMaster.setId( model.getId() );
        }
        if ( model.getRemarks() != null ) {
            materialMaster.setRemarks( model.getRemarks() );
        }
        if ( model.getCreateBy() != null ) {
            materialMaster.setCreateBy( model.getCreateBy() );
        }
        if ( model.getCreateDate() != null ) {
            materialMaster.setCreateDate( model.getCreateDate() );
        }
        if ( model.getCurrentUser() != null ) {
            materialMaster.setCurrentUser( model.getCurrentUser() );
        }
        if ( model.getPage() != null ) {
            materialMaster.setPage( model.getPage() );
        }
        Map<String, String> map = model.getSqlMap();
        if ( map != null ) {
            materialMaster.setSqlMap( new HashMap<String, String>( map ) );
        }
        else {
            materialMaster.setSqlMap( null );
        }
        materialMaster.setIsNewRecord( model.getIsNewRecord() );
        if ( model.getUpdateBy() != null ) {
            materialMaster.setUpdateBy( model.getUpdateBy() );
        }
        if ( model.getUpdateDate() != null ) {
            materialMaster.setUpdateDate( model.getUpdateDate() );
        }
        if ( model.getDelFlag() != null ) {
            materialMaster.setDelFlag( model.getDelFlag() );
        }
        if ( model.getDataSource() != null ) {
            materialMaster.setDataSource( model.getDataSource() );
        }
        if ( model.getOrderId() != null ) {
            materialMaster.setOrderId( model.getOrderId() );
        }
        if ( model.getOrderDetailId() != null ) {
            materialMaster.setOrderDetailId( model.getOrderDetailId() );
        }
        if ( model.getProductId() != null ) {
            materialMaster.setProductId( model.getProductId() );
        }
        if ( model.getApplyType() != null ) {
            materialMaster.setApplyType( model.getApplyType() );
        }
        materialMaster.setApplyTime( model.getApplyTime() );
        if ( model.getStatus() != null ) {
            materialMaster.setStatus( model.getStatus() );
        }
        if ( model.getApplyId() != null ) {
            materialMaster.setApplyId( model.getApplyId() );
        }
        if ( model.getExpressCompany() != null ) {
            materialMaster.setExpressCompany( model.getExpressCompany() );
        }
        if ( model.getExpressNo() != null ) {
            materialMaster.setExpressNo( model.getExpressNo() );
        }
        if ( model.getReturnFlag() != null ) {
            materialMaster.setReturnFlag( model.getReturnFlag() );
        }
        if ( model.getQuarter() != null ) {
            materialMaster.setQuarter( model.getQuarter() );
        }
        List<MaterialProduct> list = model.getProductInfos();
        if ( list != null ) {
            materialMaster.setProductInfos( new ArrayList<MaterialProduct>( list ) );
        }
        else {
            materialMaster.setProductInfos( null );
        }
        Map<String, List<MaterialItem>> map1 = model.getProductGroup();
        if ( map1 != null ) {
            materialMaster.setProductGroup( new HashMap<String, List<MaterialItem>>( map1 ) );
        }
        else {
            materialMaster.setProductGroup( null );
        }
        Map<String, Product> map2 = model.getProducts();
        if ( map2 != null ) {
            materialMaster.setProducts( new HashMap<String, Product>( map2 ) );
        }
        else {
            materialMaster.setProducts( null );
        }
        if ( model.getProduct() != null ) {
            materialMaster.setProduct( model.getProduct() );
        }
        if ( model.getOrderNo() != null ) {
            materialMaster.setOrderNo( model.getOrderNo() );
        }
        if ( model.getCustomer() != null ) {
            materialMaster.setCustomer( model.getCustomer() );
        }
        if ( model.getUserName() != null ) {
            materialMaster.setUserName( model.getUserName() );
        }
        if ( model.getUserPhone() != null ) {
            materialMaster.setUserPhone( model.getUserPhone() );
        }
        if ( model.getArea() != null ) {
            materialMaster.setArea( model.getArea() );
        }
        if ( model.getSubArea() != null ) {
            materialMaster.setSubArea( model.getSubArea() );
        }
        if ( model.getUserAddress() != null ) {
            materialMaster.setUserAddress( model.getUserAddress() );
        }
        if ( model.getCloseBy() != null ) {
            materialMaster.setCloseBy( model.getCloseBy() );
        }
        if ( model.getCloseDate() != null ) {
            materialMaster.setCloseDate( model.getCloseDate() );
        }
        if ( model.getCloseRemark() != null ) {
            materialMaster.setCloseRemark( model.getCloseRemark() );
        }
        if ( model.getCloseType() != null ) {
            materialMaster.setCloseType( model.getCloseType() );
        }
        if ( model.getMasterNo() != null ) {
            materialMaster.setMasterNo( model.getMasterNo() );
        }
        if ( model.getThrdNo() != null ) {
            materialMaster.setThrdNo( model.getThrdNo() );
        }
        if ( model.getProductIds() != null ) {
            materialMaster.setProductIds( model.getProductIds() );
        }
        if ( model.getProductNames() != null ) {
            materialMaster.setProductNames( model.getProductNames() );
        }
        if ( model.getOrderCreator() != null ) {
            materialMaster.setOrderCreator( model.getOrderCreator() );
        }
        if ( model.getSignAt() != null ) {
            materialMaster.setSignAt( model.getSignAt() );
        }
        if ( model.getCanRush() != null ) {
            materialMaster.setCanRush( model.getCanRush() );
        }
        if ( model.getProvinceId() != null ) {
            materialMaster.setProvinceId( model.getProvinceId() );
        }
        if ( model.getCityId() != null ) {
            materialMaster.setCityId( model.getCityId() );
        }
        if ( model.getProductCategoryId() != null ) {
            materialMaster.setProductCategoryId( model.getProductCategoryId() );
        }
        if ( model.getReceiver() != null ) {
            materialMaster.setReceiver( model.getReceiver() );
        }
        if ( model.getReceiverPhone() != null ) {
            materialMaster.setReceiverPhone( model.getReceiverPhone() );
        }
        if ( model.getReceiverProvinceId() != null ) {
            materialMaster.setReceiverProvinceId( model.getReceiverProvinceId() );
        }
        if ( model.getReceiverCityId() != null ) {
            materialMaster.setReceiverCityId( model.getReceiverCityId() );
        }
        if ( model.getReceiverAreaId() != null ) {
            materialMaster.setReceiverAreaId( model.getReceiverAreaId() );
        }
        if ( model.getReceiverAddress() != null ) {
            materialMaster.setReceiverAddress( model.getReceiverAddress() );
        }
        if ( model.getB2bOrderId() != null ) {
            materialMaster.setB2bOrderId( model.getB2bOrderId() );
        }
        if ( model.getReceiverType() != null ) {
            materialMaster.setReceiverType( model.getReceiverType() );
        }
        if ( model.getDescription() != null ) {
            materialMaster.setDescription( model.getDescription() );
        }
        if ( model.getKefuType() != null ) {
            materialMaster.setKefuType( model.getKefuType() );
        }

        return materialMaster;
    }

    @Override
    public MaterialReturn toReturnForm(MaterialMaster model) {
        if ( model == null ) {
            return null;
        }

        MaterialReturn materialReturn = new MaterialReturn();

        if ( model.getId() != null ) {
            materialReturn.setMasterId( model.getId() );
        }
        if ( model.getId() != null ) {
            materialReturn.setId( model.getId() );
        }
        if ( model.getRemarks() != null ) {
            materialReturn.setRemarks( model.getRemarks() );
        }
        if ( model.getCurrentUser() != null ) {
            materialReturn.setCurrentUser( model.getCurrentUser() );
        }
        if ( model.getPage() != null ) {
            materialReturn.setPage( materialMasterPageToMaterialReturnPage( model.getPage() ) );
        }
        Map<String, String> map = model.getSqlMap();
        if ( map != null ) {
            materialReturn.setSqlMap( new HashMap<String, String>( map ) );
        }
        else {
            materialReturn.setSqlMap( null );
        }
        materialReturn.setIsNewRecord( model.getIsNewRecord() );
        if ( model.getQuarter() != null ) {
            materialReturn.setQuarter( model.getQuarter() );
        }
        if ( model.getStatus() != null ) {
            materialReturn.setStatus( model.getStatus() );
        }
        if ( model.getTotalPrice() != null ) {
            materialReturn.setTotalPrice( model.getTotalPrice() );
        }
        if ( model.getApplyType() != null ) {
            materialReturn.setApplyType( model.getApplyType() );
        }
        if ( model.getOrderId() != null ) {
            materialReturn.setOrderId( model.getOrderId() );
        }
        if ( model.getOrderNo() != null ) {
            materialReturn.setOrderNo( model.getOrderNo() );
        }
        if ( model.getProduct() != null ) {
            materialReturn.setProduct( model.getProduct() );
        }
        if ( model.getProductId() != null ) {
            materialReturn.setProductId( model.getProductId() );
        }
        if ( model.getCustomer() != null ) {
            materialReturn.setCustomer( model.getCustomer() );
        }
        if ( model.getUserName() != null ) {
            materialReturn.setUserName( model.getUserName() );
        }
        if ( model.getUserPhone() != null ) {
            materialReturn.setUserPhone( model.getUserPhone() );
        }
        if ( model.getArea() != null ) {
            materialReturn.setArea( model.getArea() );
        }
        if ( model.getSubArea() != null ) {
            materialReturn.setSubArea( model.getSubArea() );
        }
        if ( model.getUserAddress() != null ) {
            materialReturn.setUserAddress( model.getUserAddress() );
        }
        if ( model.getThrdNo() != null ) {
            materialReturn.setThrdNo( model.getThrdNo() );
        }
        if ( model.getProductIds() != null ) {
            materialReturn.setProductIds( model.getProductIds() );
        }
        if ( model.getProductNames() != null ) {
            materialReturn.setProductNames( model.getProductNames() );
        }
        if ( model.getOrderCreator() != null ) {
            materialReturn.setOrderCreator( model.getOrderCreator() );
        }

        return materialReturn;
    }

    @Override
    public MaterialReturnItem toReturnItem(MaterialItem model) {
        if ( model == null ) {
            return null;
        }

        MaterialReturnItem materialReturnItem = new MaterialReturnItem();

        if ( model.getRemarks() != null ) {
            materialReturnItem.setRemarks( model.getRemarks() );
        }
        if ( model.getCurrentUser() != null ) {
            materialReturnItem.setCurrentUser( model.getCurrentUser() );
        }
        if ( model.getPage() != null ) {
            materialReturnItem.setPage( materialItemPageToMaterialReturnItemPage( model.getPage() ) );
        }
        if ( model.getQuarter() != null ) {
            materialReturnItem.setQuarter( model.getQuarter() );
        }
        if ( model.getMaterial() != null ) {
            materialReturnItem.setMaterial( model.getMaterial() );
        }
        materialReturnItem.setQty( model.getQty() );
        if ( model.getPrice() != null ) {
            materialReturnItem.setPrice( model.getPrice() );
        }
        if ( model.getTotalPrice() != null ) {
            materialReturnItem.setTotalPrice( model.getTotalPrice() );
        }

        return materialReturnItem;
    }

    protected List<MaterialReturn> materialMasterListToMaterialReturnList(List<MaterialMaster> list) {
        if ( list == null ) {
            return null;
        }

        List<MaterialReturn> list1 = new ArrayList<MaterialReturn>( list.size() );
        for ( MaterialMaster materialMaster : list ) {
            list1.add( toReturnForm( materialMaster ) );
        }

        return list1;
    }

    protected Page<MaterialReturn> materialMasterPageToMaterialReturnPage(Page<MaterialMaster> page) {
        if ( page == null ) {
            return null;
        }

        Page<MaterialReturn> page1 = new Page<MaterialReturn>();

        page1.setCount( page.getCount() );
        page1.setPageNo( page.getPageNo() );
        page1.setPageSize( page.getPageSize() );
        List<MaterialReturn> list = materialMasterListToMaterialReturnList( page.getList() );
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

    protected List<MaterialReturnItem> materialItemListToMaterialReturnItemList(List<MaterialItem> list) {
        if ( list == null ) {
            return null;
        }

        List<MaterialReturnItem> list1 = new ArrayList<MaterialReturnItem>( list.size() );
        for ( MaterialItem materialItem : list ) {
            list1.add( toReturnItem( materialItem ) );
        }

        return list1;
    }

    protected Page<MaterialReturnItem> materialItemPageToMaterialReturnItemPage(Page<MaterialItem> page) {
        if ( page == null ) {
            return null;
        }

        Page<MaterialReturnItem> page1 = new Page<MaterialReturnItem>();

        page1.setCount( page.getCount() );
        page1.setPageNo( page.getPageNo() );
        page1.setPageSize( page.getPageSize() );
        List<MaterialReturnItem> list = materialItemListToMaterialReturnItemList( page.getList() );
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
