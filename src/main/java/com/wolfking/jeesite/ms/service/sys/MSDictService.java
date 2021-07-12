package com.wolfking.jeesite.ms.service.sys;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.exception.BaseException;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.sys.SysDict;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.enums.OrderStatusType;
import com.wolfking.jeesite.ms.enums.PaymentType;
import com.wolfking.jeesite.ms.enums.YesNo;
import com.wolfking.jeesite.ms.feign.sys.MSDictFeign;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MSDictService {

    @Autowired
    private MSDictFeign sysDictFeign;

    @Autowired
    private MapperFacade mapper;


    /**
     * 根据id获取字典项
     * @param id
     * @return
     */
    public Dict get(Long id) {
        Dict dict;
        MSResponse<SysDict> responseEntity = sysDictFeign.get(id);
        if (MSResponse.isSuccess(responseEntity)) {
            dict = mapper.map(responseEntity.getData(), Dict.class);
        }
        else {
            dict = new Dict();
        }

//        MSResponse<MSPage<SysDict>> page = sysDictFeign.getList(new SysDict());
       return dict;
    }

    /**
     * 获取所有的字典类型
     * @return
     */
    public List<String> getTypeList(){
        List<String> typeList = Lists.newArrayList();
        MSResponse<List<String>> responseEntity = sysDictFeign.getTypeList();
        if (MSResponse.isSuccess(responseEntity)) {
            typeList = responseEntity.getData();
        }
        return typeList;
    }

    /**
     * 分页查询字典项
     * @param page
     * @param dict
     * @return
     */
    public Page<Dict> findPage(Page<Dict> page, Dict dict) {

        //转换参数类型
        SysDict sysDict = mapper.map(dict, SysDict.class);
        MSPage msPage = mapper.map(page, MSPage.class);
        sysDict.setPage(msPage);

        MSResponse<MSPage<SysDict>> responseEntity = sysDictFeign.getList(sysDict);
        if (MSResponse.isSuccess(responseEntity)) {
            MSPage<SysDict> returnPage = responseEntity.getData();

            //在内存中对记录进行分页
            page.setCount(returnPage.getRowCount());//必须先设置总记录条数
            page.setList(mapper.mapAsList(returnPage.getList(), Dict.class));
        }
        else {
            page.setCount(0);
            page.setList(Lists.newArrayList());
        }

        return page;
    }

    /**
     * 查询字典项
     * @param dict
     * @return
     */
    public List<Dict> findList(Dict dict) {
        //转换参数类型
        SysDict sysDict = mapper.map(dict, SysDict.class);
        MSResponse<MSPage<SysDict>> responseEntity = sysDictFeign.getList(sysDict);

        List<Dict> dictList = Lists.newArrayList();
        if (MSResponse.isSuccess(responseEntity)) {
            dictList = mapper.mapAsList(responseEntity.getData().getList(), Dict.class);
        }

        return dictList;
    }

    /**
     * 根据类型查询字典项
     * @param type
     * @return
     */
//    public List<Dict> findListByType(String type) {
//        List<Dict> dictList = Lists.newArrayList();
//        if (StringUtils.isNotBlank(type)) {
//            if (type.equalsIgnoreCase("PaymentType")) {
//                dictList = PaymentType.getAllPaymentTypes(); //将PaymentType由数据库取数据改为枚举
//            }
//            else if (type.equalsIgnoreCase("yes_no")) {
//                dictList = YesNo.getAllYesNo();
//            }
//            else if (type.equalsIgnoreCase("order_status")) {
//                dictList = OrderStatusType.getAllOrderStatusTypes();//将order_status由数据库取值改为枚举
//            }
//            else {
//                MSResponse<List<SysDict>> responseEntity = sysDictFeign.getListByType(type);
//                if (MSResponse.isSuccess(responseEntity)) {
//                    dictList = mapper.mapAsList(responseEntity.getData(), Dict.class);
//                }
//            }
//        }
//        return dictList;
//    }
    public List<Dict> findListByType(String type) {
        List<Dict> dictList = Lists.newArrayList();
        if (StringUtils.isNotBlank(type)) {
            if (SysDict.DICT_TYPE_PAYMENT_TYPE.equals(type)
                    || SysDict.DICT_TYPE_ORDER_STATUS.equals(type)
                    || SysDict.DICT_TYPE_YES_NO.equals(type)) {
                List<SysDict> list = SysDict.getDictListFromEnumObject(type);
                dictList = mapper.mapAsList(list, Dict.class);
            } else {
                    MSResponse<List<SysDict>> responseEntity = sysDictFeign.getListByType(type);
                if (MSResponse.isSuccess(responseEntity)) {
                    dictList = mapper.mapAsList(responseEntity.getData(), Dict.class);
                }
            }
        }
        return dictList;
    }

    /**
     * 删除字典项
     * @param dict
     */
    public void delete(Dict dict) {
        dict.preUpdate();
        SysDict sysDict = mapper.map(dict, SysDict.class);
        MSResponse<Integer> responseEntity = sysDictFeign.delete(sysDict);
        if (!MSResponse.isSuccess(responseEntity)) {
            throw new RuntimeException(responseEntity.getMsg());
        }
    }

    /**
     * 新建/编辑字典项
     * @param dict
     */
    public void save(Dict dict) {
        if (dict.getIsNewRecord()){
            dict.preInsert();
            MSResponse<SysDict> responseEntity = sysDictFeign.insert(mapper.map(dict, SysDict.class));
            if (MSResponse.isSuccess(responseEntity)) {
                dict.setId(responseEntity.getData().getId());
            }
            else {
                throw new RuntimeException(responseEntity.getMsg());
            }
        }else{
            dict.preUpdate();
            SysDict sysDict = mapper.map(dict, SysDict.class);
            MSResponse<Integer> responseEntity = sysDictFeign.update(sysDict);
            if (!MSResponse.isSuccess(responseEntity)) {
                throw new BaseException(responseEntity.getMsg());
            }
        }
    }

    /**
     * 缓存所有字典项
     */
    public void reloadAllToRedis() {
        MSResponse<Boolean> responseEntity = sysDictFeign.reloadAllToRedis();
        if (!MSResponse.isSuccess(responseEntity)) {
            throw new RuntimeException(responseEntity.getMsg());
        }
    }

    /**
     * 缓存某一类字典项
     */
    public void reloadToRedis(String type) {
        if (StringUtils.isNotBlank(type)) {
            MSResponse<Boolean> responseEntity = sysDictFeign.reloadToRedis(type);
            if (!MSResponse.isSuccess(responseEntity)) {
                throw new RuntimeException(responseEntity.getMsg());
            }
        }
    }


    /**
     * 获得字典
     *
     * @param value        字典值
     * @param type         字典类型
     * @return
     */
    public Dict getDictByValue(String value, String type) {
        Dict dict = null;
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
            List<Dict> dicts = findListByType(type);
            if (dicts.size() > 0) {
                dict = dicts.stream().filter(t->t.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
            }
        }
        return dict;
    }
}
