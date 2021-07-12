package com.wolfking.jeesite.ms.providermd.utils;

import com.google.common.collect.Maps;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSBase;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDServicePointViewModel;
import com.wolfking.jeesite.common.persistence.LongIDBaseEntity;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.ms.providermd.service.MSEngineerService;
import com.wolfking.jeesite.ms.providermd.service.MSServicePointService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
public class MDUtils {
    private static MapperFacade mapper = SpringContextHolder.getBean(MapperFacade.class);

    /**
     * 获取不需要转换的对象数据 add on 2020-3-27 16:30
     * @param supplier
     * @param <R>
     * @return
     */
    public static <R> R getObjUnnecessaryConvertType(Supplier<MSResponse<R>> supplier) {
        R r = null;
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();
        MSResponse<R> msResponse = supplier.get();
        if (MSResponse.isSuccess(msResponse)) {
            r = msResponse.getData();
            log.warn("微服务方法:{};获取的数据:{}", strMethodName, GsonUtils.toGsonString(r));
        } else {
            log.warn("微服务方法:{};获取的数据为空", strMethodName);
        }
        return r;
    }

    /**
     * 获取需要转换的单个对象 add on 2020-3-27 16:30
     * @param returnType
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T extends MSBase, S extends LongIDBaseEntity> S getObjNecessaryConvertType(Class<S> returnType, Supplier<MSResponse<T>> supplier) {
        S s = null;
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();
        MSResponse<T> msResponse = supplier.get();
        if (MSResponse.isSuccess(msResponse)) {
            s = mapper.map(msResponse.getData(), returnType);
            log.warn("微服务方法:{};获取的数据:{}", strMethodName, GsonUtils.toGsonString(s));
        } else {
            log.warn("微服务方法:{};获取的数据为空", strMethodName);
        }
        return s;
    }


    /**
     * 获取单笔记录数据
     * @param id
     * @param returnType
     * @param fun
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T extends MSBase, S extends LongIDBaseEntity> S getById(Long id, Class<S> returnType, Function<Long,MSResponse<T>> fun) {
        S s = null;
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();
        MSResponse<T> msResponse = fun.apply(id);
        if (MSResponse.isSuccess(msResponse)) {
            s = mapper.map(msResponse.getData(), returnType);
            log.warn("微服务方法:{};获取的数据:{}", strMethodName, GsonUtils.toGsonString(s));
        } else {
            log.warn("微服务方法:{};获取的数据为空", strMethodName);
        }
        return s;
    }

    /**
     * 分页获取list列表数据
     * @param page
     * @param s
     * @param returnType
     * @param needTransformType
     * @param fun
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T extends MSBase, S extends LongIDBaseEntity> Page<S> findListForPage(Page<S> page, S s, Class<S> returnType, Class<T> needTransformType, Function<T,MSResponse<MSPage<T>>> fun) {
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();
        T t = mapper.map(s, needTransformType);

        Page<S> returnPage = new Page<>();
        returnPage.setPageSize(page.getPageSize());
        returnPage.setPageNo(page.getPageNo());

        t.setPage(new MSPage<>(page.getPageNo(), page.getPageSize()));
        MSResponse<MSPage<T>> returnResponse = fun.apply(t);
        if (MSResponse.isSuccess(returnResponse)) {
            MSPage<T>  msPage = returnResponse.getData();
            returnPage.setList(mapper.mapAsList(msPage.getList(), returnType));
            returnPage.setCount(msPage.getRowCount());
            log.warn("微服务方法:{};获取的分页数据:{}", strMethodName, GsonUtils.toGsonString(msPage.getList()));
        } else {
            returnPage.setCount(0);
            returnPage.setList(new ArrayList<>());
            log.warn("微服务方法:{}; 获取的分页数据为空", strMethodName);
        }
        return returnPage;
    }

    /**
     * 分页获取list列表数据  //add on 2019-11-25
     * @param page
     * @param t
     * @param fun
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T extends MSBase, S extends MSBase> Page<S> findMDEntityListForPage(Page<S> page, T t, Function<T,MSResponse<MSPage<S>>> fun) {
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();

        Page<S> returnPage = new Page<>();
        returnPage.setPageSize(page.getPageSize());
        returnPage.setPageNo(page.getPageNo());

        t.setPage(new MSPage<>(page.getPageNo(), page.getPageSize()));
        MSResponse<MSPage<S>> returnResponse = fun.apply(t);
        if (MSResponse.isSuccess(returnResponse)) {
            MSPage<S>  msPage = returnResponse.getData();
            returnPage.setCount(msPage.getRowCount());
            returnPage.setList(msPage.getList());
            log.warn("微服务方法:{};获取的分页数据:{}", strMethodName, GsonUtils.toGsonString(msPage.getList()));
        } else {
            returnPage.setCount(0);
            returnPage.setList(new ArrayList<>());
            log.warn("微服务方法:{}; 获取的分页数据为空", strMethodName);
        }
        return returnPage;
    }

    /**
     * 获取带参数的列表
     * @param s 入口参数(如Customer)
     * @param returnType 返回的类型 (如Customer.class)
     * @param needTransformType 需要转换的类型(如MDCutomer.class)
     * @param fun
     * @param <T>
     * @param <S>
     * @return
     */
    @Deprecated  //用方法： findListNecessaryConvertType代替
    public static <T extends MSBase, S extends LongIDBaseEntity> List<S> findList(S s, Class<S> returnType, Class<T> needTransformType, Function<T,MSResponse<List<T>>> fun) {
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();

        List<S> sList = null;
        T t = mapper.map(s, needTransformType);
        MSResponse<List<T>> msResponse = fun.apply(t);
        if (MSResponse.isSuccess(msResponse)) {
            sList = mapper.mapAsList(msResponse.getData(), returnType);
            log.warn("微服务方法:{};获取的数据:{}", strMethodName, GsonUtils.toGsonString(msResponse.getData()));
        } else {
            log.warn("微服务方法:{}; 获取的数据为空", strMethodName);
        }
        return sList;
    }

    /**
     * 根据基本数据类型的封装类(如Integer，Long)或非继承于MSBase,LongIDBaseEntity的数据类型为参数获取的数据列表
     * @param p
     * @param returnType
     * @param fun
     * @param <T>
     * @param <S>
     * @param <P>
     * @return
     */
    public static <T extends MSBase, S extends LongIDBaseEntity, P> List<S> findListByCustomCondition(P p, Class<S> returnType, Function<P,MSResponse<List<T>>> fun) {
        // TODO: 后续这个方法考虑删除  // 2019-11-16
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();

        List<S> sList = null;
        MSResponse<List<T>> msResponse = fun.apply(p);
        if (MSResponse.isSuccess(msResponse)) {
            sList = mapper.mapAsList(msResponse.getData(), returnType);
            log.warn("微服务方法:{};获取的数据:{}", strMethodName, GsonUtils.toGsonString(msResponse.getData()));
        } else {
            log.warn("微服务方法:{};获取的数据为空", strMethodName);
        }
        return sList;
    }

    /**
     * 自定义查询不需要转换的列表   // 2019-12-16
     * TODO: 用来取代方法findListByCustomCondition(不需要类型转换）
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> List<T> findListUnnecessaryConvertType(Supplier<MSResponse<List<T>>> supplier) {
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();
        List<T> sList = null;
        MSResponse<List<T>> msResponse = supplier.get();
        if (MSResponse.isSuccess(msResponse)) {
            sList = msResponse.getData();
        }
        return sList;
    }


    /**
     * 获取无参数的列表方法
     * TODO: 用来取代方法findListByCustomCondition(需要类型转换）
     * @param returnType
     * @param fun
     * @param <T>
     * @param <S>
     * @return
     */
    @Deprecated   // TODO：后面使用方法 findListNecessaryConvertType 代替  2020-4-2
    public static <T extends MSBase, S extends LongIDBaseEntity> List<S> findAllList(Class<S> returnType, Supplier<MSResponse<List<T>>> fun) {
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();
        List<S> sList = null;
        MSResponse<List<T>> msResponse = fun.get();
        if (MSResponse.isSuccess(msResponse)) {
            sList = mapper.mapAsList(msResponse.getData(), returnType);
            log.warn("微服务方法:{};获取的数据:{}", strMethodName, GsonUtils.toGsonString(msResponse.getData()));
        } else {
            log.warn("微服务方法:{};获取的数据为空", strMethodName);
        }
        return sList;
    }

    /**
     * 获取无参数的列表方法
     * TODO: 用来取代方法findListByCustomCondition(需要类型转换）,还可以用来替换findAllList方法  //add 2019-12-16
     * @param returnType
     * @param fun
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T extends MSBase, S extends LongIDBaseEntity> List<S> findListNecessaryConvertType(Class<S> returnType, Supplier<MSResponse<List<T>>> fun) {
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();
        List<S> sList = null;
        MSResponse<List<T>> msResponse = fun.get();
        if (MSResponse.isSuccess(msResponse)) {
            sList = mapper.mapAsList(msResponse.getData(), returnType);
            log.warn("微服务方法:{};获取的数据:{}", strMethodName, GsonUtils.toGsonString(msResponse.getData()));
        } else {
            log.warn("微服务方法:{};获取的数据为空", strMethodName);
        }
        return sList;
    }

    /**
     * 保存/更新
     * @param s
     * @param isNew
     * @param fun
     * @param <T>
     * @return
     */
    public static <T extends MSBase, S extends LongIDBaseEntity> MSErrorCode genericSave(S s, Class<T> type, boolean isNew, Function<T,MSResponse<Integer>> fun) {
        return genericSaveShouldReturnId(s,type,isNew,fun,false);
    }

    /**
     * 保存/更新
     * @param s
     * @param isNew
     * @param fun
     * @param <T>
     * @return
     */
    public static <T extends MSBase, S extends LongIDBaseEntity> MSErrorCode genericSaveShouldReturnId(S s, Class<T> type, boolean isNew, Function<T,MSResponse<Integer>> fun, boolean shouldReturnId) {
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();
        T t = mapper.map(s, type);
//        User user = UserUtils.getUser();
        if (isNew) {
//            if (user.getId() != null) {
//                t.setCreateById(user.getId());
//                t.setUpdateById(user.getId());
//            }
            t.preInsert();
        } else {
//            if (user.getId() != null) {
//                t.setUpdateById(user.getId());
//            }
            t.preUpdate();
        }

        long lStart = System.currentTimeMillis();
        MSResponse<Integer> msResponse = fun.apply(t);
        if (shouldReturnId == true && isNew == true) {
            // 当插入时从微服务中获取生成ID
            if (MSResponse.isSuccess(msResponse)) {
                s.setId(msResponse.getData().longValue());
            }
        }
        long lEnd = System.currentTimeMillis();
        log.warn("微服务方法{},方法类型{};{},耗时:{}毫秒;返回Code:{},msg:{}", strMethodName, isNew?"insert":"update", GsonUtils.toGsonString(t), lEnd-lStart, msResponse.getCode(), msResponse.getMsg());

        return new MSErrorCode(msResponse.getCode(), msResponse.getMsg());
    }

    public static <P> MSErrorCode genericCustomConditionSave(P p, Function<P, MSResponse<Integer>> fun) {
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();
        long lStart = System.currentTimeMillis();
        MSResponse<Integer> msResponse = fun.apply(p);
        long lEnd = System.currentTimeMillis();
        log.warn("微服务方法{},{},耗时:{}毫秒;返回Code:{},msg:{}", strMethodName, p, lEnd-lStart, msResponse.getCode(), msResponse.getMsg());

        return new MSErrorCode(msResponse.getCode(), msResponse.getMsg());
    }

    /**
     *
     * @param supplier
     * @return
     */
    public static MSErrorCode customSave(Supplier<MSResponse<Integer>> supplier) {
        String strMethodName = Thread.currentThread().getStackTrace()[2].getFileName().replace(".java",".") + Thread.currentThread().getStackTrace()[2].getMethodName();
        long lStart = System.currentTimeMillis();
        MSResponse<Integer> msResponse = supplier.get();
        long lEnd = System.currentTimeMillis();
        log.warn("微服务方法{},耗时:{}毫秒;返回Code:{},msg:{}", strMethodName, lEnd-lStart, msResponse.getCode(), msResponse.getMsg());

        return new MSErrorCode(msResponse.getCode(), msResponse.getMsg());
    }

    private static MSServicePointService msServicePointService = SpringContextHolder.getBean(MSServicePointService.class);
    private static MSEngineerService msEngineerService = SpringContextHolder.getBean(MSEngineerService.class);

    public static Map<Long, String> getServicePointNamesByIds(List<Long> servicePointIds) {

        List<MDServicePointViewModel> mdServicePointViewModelList = msServicePointService.findBatchByIdsByCondition(servicePointIds, Arrays.asList("id","name"),null);
        return mdServicePointViewModelList != null && !mdServicePointViewModelList.isEmpty() ?mdServicePointViewModelList.stream().collect(Collectors.toMap(MDServicePointViewModel::getId, MDServicePointViewModel::getName)):Maps.newHashMap();
    }

    public static Map<Long, String> getEngineerNamesByIds(List<Long> engineerIds) {
        // add on 2019-10-28
        List<Engineer> engineerList = msEngineerService.findEngineersByIds(engineerIds, Arrays.asList("id", "name"));
        return engineerIds != null && !engineerIds.isEmpty() ? engineerList.stream().collect(Collectors.toMap(Engineer::getId, Engineer::getName)):Maps.newHashMap();
    }
}
