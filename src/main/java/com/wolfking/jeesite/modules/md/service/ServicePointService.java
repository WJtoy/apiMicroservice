package com.wolfking.jeesite.modules.md.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDServicePoint;
import com.kkl.kklplus.entity.md.MDServicePointProduct;
import com.kkl.kklplus.utils.QuarterUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.api.entity.fi.RestServicePointBalance;
import com.wolfking.jeesite.modules.api.entity.md.*;
import com.wolfking.jeesite.modules.api.util.*;
import com.wolfking.jeesite.modules.fi.dao.EngineerCurrencyDao;
import com.wolfking.jeesite.modules.fi.entity.EngineerCurrency;
import com.wolfking.jeesite.modules.md.dao.EngineerTemperatureDao;
import com.wolfking.jeesite.modules.md.dao.ServicePointDao;
import com.wolfking.jeesite.modules.md.entity.*;
import com.wolfking.jeesite.modules.md.entity.viewModel.EngineerTemperatureSearchModel;
import com.wolfking.jeesite.modules.md.utils.ProductUtils;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.service.AreaService;
import com.wolfking.jeesite.modules.sys.service.SystemService;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import com.wolfking.jeesite.ms.providermd.service.*;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 服务网点
 * Ryan Lu
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ServicePointService extends LongIDBaseService {

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    private AreaService areaService;

    @Autowired
    private MapperFacade mapper;

    @Resource
    private EngineerCurrencyDao engineerCurrencyDao;

    @Autowired
    private MSProductService msProductService;
    @Autowired
    private MSServicePointService msServicePointService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private MSEngineerService msEngineerService;

    @Autowired
    private EngineerService engineerService;

    @Autowired
    private MSEngineerAreaService msEngineerAreaService;

    @Autowired
    private MSServicePointAreaService msServicePointAreaService;

    @Autowired
    private MSServicePointProductService msServicePointProductService;

    @Autowired
    private MSServicePointPriceService msServicePointPriceService;
    @Autowired
    private ServicePointFinanceService servicePointFinanceService;
    @Resource
    private ServicePointDao dao;
    @Autowired
    private EngineerTemperatureDao engineerTemperatureDao;



    //region 网点

    public ServicePoint get(Long id) {
        return getWithExtendPropertyFromMaster(id);
    }

    public ServicePoint getWithExtendPropertyFromMaster(long id) {
        ServicePoint servicePoint = msServicePointService.getById(id);
        if (servicePoint != null && servicePoint.getProductCategoryIds() != null && !servicePoint.getProductCategoryIds().isEmpty()) {
            Map<Long, ProductCategory> productCategoryMap = ProductUtils.getAllProductCategoryMap();
            List<ProductCategory> productCategories = Lists.newArrayList();
            ProductCategory productCategory;
            for (Long pId : servicePoint.getProductCategoryIds()) {
                productCategory = productCategoryMap.get(pId);
                if (productCategory != null) {
                    productCategories.add(productCategory);
                }
            }
            servicePoint.setProductCategories(productCategories);
        }
        if (servicePoint != null && servicePoint.getCreateBy() != null && servicePoint.getCreateBy().getId() != null) {
            User user = UserUtils.get(servicePoint.getCreateBy().getId());
            servicePoint.setCreateBy(user);
        }
        if (servicePoint != null && servicePoint.getUpdateBy() != null && servicePoint.getUpdateBy().getId() != null) {
            User user = UserUtils.get(servicePoint.getUpdateBy().getId());
            servicePoint.setUpdateBy(user);
        }

        return getServicePointExtraProperties(servicePoint, true);
    }


    /**
     * 获取网点其他额外的属性
     *
     * @param servicePoint
     * @return
     */
    public ServicePoint getServicePointExtraProperties(ServicePoint servicePoint, boolean bFromMaster) {
        if (servicePoint == null) {
            return servicePoint;
        }
        // add on 2019-9-10
        // 获取finance
        ServicePointFinance servicePointFinance = null;
        if (bFromMaster) {
            servicePointFinance = dao.getFinanceFromMaster(servicePoint.getId());  //add on 2020-3-18
        } else {
            //servicePointFinance = dao.getFinanceNew(servicePoint.getId());  // mark on 2020-5-5
            servicePointFinance = servicePointFinanceService.getFromCache(servicePoint.getId()); // add on 2020-5-5 //从网点财务缓存中获取
        }
        if (servicePointFinance == null) {
            return null;
        }
        servicePoint.setFinance(servicePointFinance);
        // 获取主账号信息
        if (servicePoint.getPrimary() != null && servicePoint.getPrimary().getId() != null) {
            Engineer engineerFromMS = null;
            if (bFromMaster) {  // 从微服务DB中获取
                engineerFromMS = msEngineerService.getById(servicePoint.getPrimary().getId());
            } else {
                engineerFromMS = msEngineerService.getByIdFromCache(servicePoint.getPrimary().getId());
            }
            if (engineerFromMS != null) {
                servicePoint.setPrimary(engineerFromMS);
            }
            // add on 2019-10-21 end
        }
        // 获取区域信息
        if (servicePoint.getArea() != null && servicePoint.getArea().getId() != null) {
            Area area = areaService.getFromCache(servicePoint.getArea().getId());
            servicePoint.setArea(area);
        }

        //切换为微服务
        if (servicePoint.getFinance().getPaymentType() != null && Integer.parseInt(servicePoint.getFinance().getPaymentType().getValue()) > 0) {
            String paymentTypeLabel = MSDictUtils.getDictLabel(servicePoint.getFinance().getPaymentType().getValue(), "PaymentType", "");
            servicePoint.getFinance().getPaymentType().setLabel(paymentTypeLabel);
        }
        if (servicePoint.getFinance().getBank() != null && Integer.parseInt(servicePoint.getFinance().getBank().getValue()) > 0) {
            String bankName = MSDictUtils.getDictLabel(servicePoint.getFinance().getBank().getValue(), "banktype", "");
            servicePoint.getFinance().getBank().setLabel(bankName);
        }
        if (servicePoint.getLevel() != null && Integer.parseInt(servicePoint.getLevel().getValue()) > 0) {
            String levelName = MSDictUtils.getDictLabel(servicePoint.getLevel().getValue(), "ServicePointLevel", "");
            servicePoint.getLevel().setLabel(levelName);
        }
        if (servicePoint.getFinance() != null && servicePoint.getFinance().getUnit() != null && StringUtils.isNotBlank(servicePoint.getFinance().getUnit().getValue())) {
            String unitName = MSDictUtils.getDictLabel(servicePoint.getFinance().getUnit().getValue(), "unit", "");
            servicePoint.getFinance().getUnit().setLabel(unitName);
        }
        if (servicePoint.getFinance() != null && servicePoint.getFinance().getBankIssue() != null &&
                StringUtils.toInteger(servicePoint.getFinance().getBankIssue().getValue()) > 0) {
            String bankIssueName = MSDictUtils.getDictLabel(servicePoint.getFinance().getBankIssue().getValue(), "BankIssueType", "");
            servicePoint.getFinance().getBankIssue().setLabel(bankIssueName);
        }

        return servicePoint;
    }

    /**
     * 按id获得网点信息
     * 优先从缓存中取
     *
     * @param id
     * @return
     */
    public ServicePoint getFromCache(Long id) {
        ServicePoint servicePoint = msServicePointService.getCacheById(id);
        if (servicePoint != null && servicePoint.getProductCategoryIds() != null && !servicePoint.getProductCategoryIds().isEmpty()) {
            Map<Long, ProductCategory> productCategoryMap = ProductUtils.getAllProductCategoryMap();
            List<ProductCategory> productCategories = Lists.newArrayList();
            ProductCategory productCategory;
            for (Long pId : servicePoint.getProductCategoryIds()) {
                productCategory = productCategoryMap.get(pId);
                if (productCategory != null) {
                    productCategories.add(productCategory);
                }
            }
            servicePoint.setProductCategories(productCategories);
        }
        return getServicePointExtraProperties(servicePoint, false);
    }

    /**
     * 获得网点下所有安维人员清单
     *
     * @param id 网点id
     * @return
     */
    public List<Engineer> getEngineersFromCache(Long id) {
        List<Engineer> list = Lists.newArrayList();
        List<Engineer> engineerList = msEngineerService.findEngineerByServicePointIdFromCache(id);
        if (engineerList == null || engineerList.isEmpty()) {
            return engineerList;
        }
        engineerList = engineerList.stream().filter(engineer -> engineer.getDelFlag().equals(Engineer.DEL_FLAG_NORMAL)).collect(Collectors.toList());
        List<Long> engineerIds = engineerList.stream().map(Engineer::getId).collect(Collectors.toList());
        List<User> userList = systemService.findEngineerAccountList(engineerIds, null);
        Map<Long, User> userMap = userList != null && !userList.isEmpty() ? userList.stream().filter(r -> r.getEngineerId() != null).collect(Collectors.toMap(User::getEngineerId, Function.identity())) : Maps.newHashMap();

        engineerList.stream().forEach(engineer -> {
            User user = userMap.get(engineer.getId());
            if (user != null) {
                engineer.setAppLoged(user.getAppLoged());
                engineer.setAccountId(user.getId());
            }

            if (engineer.getLevel() != null && Integer.parseInt(engineer.getLevel().getValue()) > 0) {
                String levelName = MSDictUtils.getDictLabel(engineer.getLevel().getValue(), "ServicePointLevel", "");
                engineer.getLevel().setLabel(levelName);
            }
        });
        list.addAll(engineerList);

        if (list == null || list.size() == 0) {
            Engineer engineer = new Engineer();
            engineer.setServicePoint(new ServicePoint(id));
            list = findEngineerList(engineer);//切换为微服务
        }
        return list;
    }

    /**
     * 获得网点下某个安维人员信息
     *
     * @param servicePointId 网点id
     * @param engineerId     安维id
     * @return
     */
    public Engineer getEngineerFromCache(Long servicePointId, Long engineerId) {
        Engineer engineer = null;
        if (servicePointId == null || engineerId == null) {
            return engineer;
        }
        // add on 2019-10-31 begin
        engineer = msEngineerService.getEngineerFromCache(servicePointId, engineerId);
        if (engineer != null) {
            User user = systemService.getUserByEngineerId(engineer.getId());
            if (user != null) {
                engineer.setAppLoged(user.getAppLoged());
                engineer.setAccountId(user.getId());
            }

            if (engineer.getLevel() != null && Integer.parseInt(engineer.getLevel().getValue()) > 0) {
                String levelName = MSDictUtils.getDictLabel(engineer.getLevel().getValue(), "ServicePointLevel", "");
                engineer.getLevel().setLabel(levelName);
            }
        }

        return engineer;
        // add on 2019-10-31 end
    }

    /**
     * 按需读取网点价格
     *
     * @param servicePointId 网点id
     * @param products       NameValuePair<产品id,服务项目id>
     * @return
     */
    public List<ServicePrice> getPricesByProductsFromCache(Long servicePointId, List<NameValuePair<Long, Long>> products) {
        return msServicePointPriceService.findPricesListByCustomizePriceFlagFromCache(servicePointId, products);
    }

    /**
     * 按需读取网点价格
     *
     * @param servicePointId 网点id
     * @param products       NameValuePair<产品id,服务项目id>
     * @return
     */
    public Map<String, ServicePrice> getPriceMapByProductsFromCache(Long servicePointId, List<NameValuePair<Long, Long>> products) {
        List<ServicePrice> prices = msServicePointPriceService.findPricesListByCustomizePriceFlagFromCache(servicePointId, products);
        if (prices == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(prices)) {
            return Maps.newHashMapWithExpectedSize(0);
        }
        return prices.stream().collect(Collectors.toMap(
                e -> String.format("%d:%d", e.getProduct().getId(), e.getServiceType().getId()),
                e -> e
        ));
    }

    /**
     * 按需读取网点偏远区域价格
     *
     * @param servicePointId 网点id
     * @param products       NameValuePair<产品id,服务项目id>
     * @return
     */
    public Map<String, ServicePrice> getRemotePriceMapByProductsFromCache(Long servicePointId, List<NameValuePair<Long, Long>> products) {
        List<ServicePrice> prices = msServicePointPriceService.findPricesListByRemotePriceFlagFromCacheForSD(servicePointId, products);
        if (prices == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(prices)) {
            return Maps.newHashMapWithExpectedSize(0);
        }
        return prices.stream().collect(Collectors.toMap(
                e -> String.format("%d:%d", e.getProduct().getId(), e.getServiceType().getId()),
                e -> e
        ));
    }

    /**
     * 按需读取网点价格
     *
     * @param servicePointId 网点id
     * @param productId      产品id
     * @param serviceTypeId  服务项目id
     * @return
     */
    public ServicePrice getPriceByProductAndServiceTypeFromCache(long servicePointId, long productId, long serviceTypeId) {
        if (servicePointId <= 0 || productId <= 0 || serviceTypeId <= 0) {
            return null;
        }
        List<ServicePrice> prices = this.getPricesByProductsFromCache(servicePointId, Lists.newArrayList(new NameValuePair<Long, Long>(productId, serviceTypeId)));
        if (CollectionUtils.isEmpty(prices)) {
            return null;
        }
        return prices.get(0);
    }

    /**
     * 读取网点负责的区域列表
     *
     * @param id 网点id
     * @return
     */
    public List<Area> getAreas(Long id) {
        // servicePointArea微服务
        List<Long> areaIdList = msServicePointAreaService.findAreaIds(id);
        List<Area> areaList = areaService.findServicePointAreas(areaIdList);
        return areaList;
    }


    /**
     * 读取网点负责的产品id列表
     *
     * @param id
     * @return
     */
    public List<Integer> getProductIds(Long id) {
        MDServicePointProduct mdServicePointProduct = new MDServicePointProduct();
        mdServicePointProduct.setServicePointId(id);
        List<Long> productIdsFromMS = msServicePointProductService.findProductIds(mdServicePointProduct);
        List<Integer> productIds = !org.springframework.util.ObjectUtils.isEmpty(productIdsFromMS) ? productIdsFromMS.stream().map(x -> x.intValue()).collect(Collectors.toList()) : Lists.newArrayList();
        return productIds;
    }

    /**
     * 读取网点负责的产品id列表(后台)
     *
     * @param id
     * @return
     */
    public List<Product> getProducts(Long id) {
        //return dao.getProducts(id);  // mark on 2019-8-21
        // add on 2019-8-21 begin
        // 调用product微服务
        List<Product> productList = Lists.newArrayList();
        List<Integer> productIds = getProductIds(id);
        String productIdStr = productIds != null && !productIds.isEmpty() ? productIds.stream().map(r -> r.toString()).distinct().collect(Collectors.joining(",")) : "";

        Product product = new Product();
        product.setProductIds(productIdStr);
        //原sql要求输出字段：p.id,p.name,p.set_flag,p.sort,p.product_category_id as "productCategory.id"
        List<Product> products = msProductService.findListByConditions(product);
        if (products != null && !products.isEmpty()) {
            // 按sort排序
            productList = products.stream().sorted(Comparator.comparing(Product::getSort)).collect(Collectors.toList());
        }
        return productList;
        // add on 2019-8-21 end
    }

    //endregion 网点

    //region 安维人员管理

    public Engineer getEngineer(Long id) {
        Engineer engineer = engineerService.getEngineer(id);  // add on 2019-10-21
        return engineer;
    }

    /**
     * 为Engineer数据赋值 // add on 2019-9-16
     *
     * @param engineer
     * @return
     */
    public Engineer populateEngineer(Engineer engineer) {
        if (engineer != null) {
            // 调用微服务ServicePoint
            ServicePoint servicePoint = msServicePointService.getById(engineer.getServicePoint().getId());
            if (servicePoint != null) {
                engineer.getServicePoint().setServicePointNo(servicePoint.getServicePointNo());
                engineer.getServicePoint().setName(servicePoint.getName());
            }
        }
        // add on 2019-9-16 end
        //切换为微服务
        if (engineer != null && engineer.getLevel() != null && Integer.parseInt(engineer.getLevel().getValue()) > 0) {
            String levelName = MSDictUtils.getDictLabel(engineer.getLevel().getValue(), "ServicePointLevel", "");
            engineer.getLevel().setLabel(levelName);
        }
        return engineer;
    }

    /**
     * 读取安维负责的区域ID列表
     *
     * @param id 安维id
     * @return
     */
    public List<Long> getEngineerAreaIds(Long id) {
        return msEngineerAreaService.findEngineerAreaIds(id); //add on 2019-11-7 //EngineerArea微服务
    }

    //切换为微服务
    public List<Engineer> findEngineerList(Engineer engineer) {
        List<Engineer> engineerList = Lists.newArrayList();
        Page<Engineer> engineerPage = new Page<>();
        engineerPage.setPageSize(1000);
        engineer.setPage(engineerPage);
        Page<Engineer> engineerIdPage = msEngineerService.findEngineerList(engineerPage, engineer);
        //log.warn("总页数：{}",engineerIdPage.getTotalPage());
        //log.warn("记录：{}",engineerIdPage.getList());
        if (engineerIdPage.getList() != null && !engineerIdPage.getList().isEmpty()) {
            engineerList.addAll(engineerIdPage.getList());
        }

        for (int i = 2; i < engineerIdPage.getTotalPage() + 1; i++) {
            Page<Engineer> engineerPage1 = new Page<>();
            engineerPage1.setPageSize(1000);
            engineerPage1.setPageNo(i);
            engineer.setPage(engineerPage1);

            Page<Engineer> engineerIdPage1 = msEngineerService.findEngineerList(engineerPage1, engineer);
            //log.warn("记录数{}",engineerIdPage1.getList());
            if (engineerIdPage1.getList() != null && !engineerIdPage1.getList().isEmpty()) {
                engineerList.addAll(engineerIdPage1.getList());
            }
        }

        if (engineerList == null || engineerList.isEmpty()) {
            return engineerList;
        }
        List<Long> engineerIds = engineerList.stream().map(Engineer::getId).distinct().collect(Collectors.toList());
        List<User> userList = Lists.newArrayList();
        if (engineerIds != null && !engineerIds.isEmpty()) {
            if (engineerIds.size() > 200) {
                List<User> partUsers = Lists.newArrayList();
                Lists.partition(engineerIds, 200).forEach(partIds -> {
                    List<User> tempUserList = systemService.findEngineerAccountList(partIds, null);
                    if (tempUserList != null && !tempUserList.isEmpty()) {
                        partUsers.addAll(tempUserList);
                    }
                });
                if (partUsers != null && !partUsers.isEmpty()) {
                    userList.addAll(partUsers);
                }
            } else {
                userList = systemService.findEngineerAccountList(engineerIds, null);
            }
        }

        Map<Long, User> userMap = userList != null && !userList.isEmpty() ? userList.stream().filter(r -> r.getEngineerId() != null).collect(Collectors.toMap(User::getEngineerId, Function.identity())) : Maps.newHashMap();

        engineerList.stream().forEach(engineerEntity -> {
            User user = userMap.get(engineerEntity.getId());
            if (user != null) {
                engineerEntity.setAppLoged(user.getAppLoged());
                engineerEntity.setAccountId(user.getId());
            }

            if (engineerEntity.getLevel() != null && Integer.parseInt(engineerEntity.getLevel().getValue()) > 0) {
                String levelName = MSDictUtils.getDictLabel(engineerEntity.getLevel().getValue(), "ServicePointLevel", "");
                engineerEntity.getLevel().setLabel(levelName);
            }
        });
        return engineerList;
        // add on 2019-11-9 end

        //return list;
    }

    /**
     * 按帐号ID获得有APP权限的安维师傅的基本信息
     *
     * @param userId 帐号ID
     * @return
     */
    public Engineer getAppEngineer(Long userId, Long tokenTimeOut) {
        String key = String.format(RedisConstant.APP_SESSION, userId);
        Engineer engineer = null;
        Long engineerId = redisUtilsLocal.hGet(RedisConstant.RedisDBType.REDIS_NEW_APP_DB, key, "engineerId", Long.class);
        Long servicePointId = redisUtilsLocal.hGet(RedisConstant.RedisDBType.REDIS_NEW_APP_DB, key, "servicePointId", Long.class);
        if (engineerId != null && servicePointId != null) {
            engineer = getEngineerFromCache(servicePointId, engineerId);
        } else {
            engineer = getEngineerByUserId(userId); // add on 2019-10-22 //Engineer微服务
        }
        ServicePoint servicePoint = getFromCache(engineer.getServicePoint().getId()); // add on 2020-3-4
        if (servicePoint != null) {
            engineer.setServicePoint(servicePoint);
        }
        if (engineer != null) {
            //更新网点主帐号：app已登录
            if (engineer.getMasterFlag() == 1) {
                //ServicePoint sp = getFromCache(engineer.getServicePoint().getId());   //mark on 2020-3-5
                //sp.getPrimary().setAppLoged(1);    //mark on 2020-3-5
                //updateServicePointCache(sp);//mark on 2020-1-14  web端去servicePoint
            }
            redisUtilsLocal.hmSet(RedisConstant.RedisDBType.REDIS_NEW_APP_DB, key, "engineerId", engineer.getId(), tokenTimeOut);
            redisUtilsLocal.hmSet(RedisConstant.RedisDBType.REDIS_NEW_APP_DB, key, "servicePointId", engineer.getServicePoint().getId(), tokenTimeOut);
        }
        return engineer;
    }

    public Engineer getEngineerByUserId(Long userId) {
        // add on 2019-10-22
        // Engineer微服务
        User user = systemService.getUser(userId);
        if (user != null && user.getDelFlag().equals(User.DEL_FLAG_NORMAL)) {
            Long engineerId = user.getEngineerId();
            Engineer engineer = msEngineerService.getByIdFromCache(engineerId);
            if (engineer != null) {
                engineer.setAppLoged(user.getAppLoged());
                engineer.setAccountId(user.getId());
                if (engineer.getArea() != null && engineer.getArea().getId() != null) {
                    Area area = areaService.getFromCache(engineer.getArea().getId());
                    if (area != null) {
                        engineer.getArea().setName(area.getName());
                        engineer.getArea().setFullName(area.getFullName());
                    }
                }
                engineer = populateEngineer(engineer);
                return engineer;
            }
        }
        return null;
    }

    //endregion 网点价格

    //region 网点基础资料报表

    /**
     * 根据id(财务id与网点id相等)获取网点财务信息// add on 2019-9-30
     *
     * @param servicePointId
     * @return
     */
    public ServicePointFinance getFinance(Long servicePointId) {
        return dao.getFinanceNew(servicePointId);
    }

    //endregion 网点基础资料报表

    /**
     * 获取网点所有金额
     *
     * @param id
     * @return
     */
    public ServicePointFinance getAmounts(Long id) {
        return dao.getAmounts(id);
    }


    //region api functions

    /**
     * 获取用户信息
     *
     * @param loginUserInfo
     * @return
     */
    public RestResult<Object> getUserInfo(RestLoginUserInfo loginUserInfo, RestGetUserInfo getUserInfo) {
        RestEnum.UserInfoType userInfoType = RestEnum.UserInfoType.valueOf(RestEnum.UserInfoTypeString[getUserInfo.getType()]);
        RestEngineer userInfo = new RestEngineer();
        Engineer engineer = getEngineerFromCache(loginUserInfo.getServicePointId(), loginUserInfo.getEngineerId());
        userInfo.setName(engineer.getName());
        userInfo.setPhoto("");

        if (userInfoType == RestEnum.UserInfoType.All || userInfoType == userInfoType.Base) {
            userInfo.setOrderCount(engineer.getOrderCount());
            userInfo.setPlanCount(engineer.getPlanCount());
            userInfo.setBreakCount(engineer.getBreakCount());
            if (loginUserInfo.getPrimary()) {
                ServicePointFinance servicePointFinance = getAmounts(loginUserInfo.getServicePointId());
                userInfo.setBalance(servicePointFinance.getBalance());
            }
        }
        if (userInfoType == RestEnum.UserInfoType.All || userInfoType == userInfoType.Detail) {
            userInfo.setPhone(engineer.getContactInfo());
            userInfo.setAddress(engineer.getAddress());
            List<Long> areaIds = getEngineerAreaIds(loginUserInfo.getEngineerId());
            Area source;
            RestArea target;
            List<RestArea> restAreaList = Lists.newArrayList();
            for (Long areaId : areaIds) {
                source = areaService.getFromCache(areaId);
                target = mapper.map(source, RestArea.class);
                restAreaList.add(target);
            }
            userInfo.setAreaList(restAreaList);
            ServicePoint servicePoint = getFromCache(engineer.getServicePoint().getId());
            userInfo.setServicePointName(servicePoint.getName());
        }
        return RestResultGenerator.success(userInfo);
    }

    public RestResult<Object> getServicePointInfo(Long servicePointId) {
        ServicePoint servicePoint = getFromCache(servicePointId);
        RestServicePoint target = mapper.map(servicePoint, RestServicePoint.class);
        //切换为微服务
        RestDict type = mapper.map(MSDictUtils.getDictByValue(String.valueOf(servicePoint.getProperty() == 0 ? 1 : servicePoint.getProperty()), "ServicePointProperty"), RestDict.class);
        target.setType(type);
        target.setPhone1(servicePoint.getContactInfo1());
        target.setPhone2(servicePoint.getContactInfo2());
        //latitude
        //longitude
        //contractImage;
        //idCardImage;
        //otherImage1;
        //otherImage2;
        target.setPrimaryName(servicePoint.getPrimary().getName());
        RestServicePointFinance finance = mapper.map(servicePoint.getFinance(), RestServicePointFinance.class);
        target.setFinance(finance);
        //List<Area> areaList = dao.getAreas(servicePointId);  //旧方法  // mark on 2019-12-3
        List<Area> areaList = getAreas(servicePointId);        //新方法  // add on 2019-12-3
        List<RestArea> restAreaList = mapper.mapAsList(areaList, RestArea.class);
        target.setAreaList(restAreaList);
//        List<Product> productList = dao.getProducts(servicePointId);  //mark on 2019-8-21
        List<Product> productList = getProducts(servicePointId);        //add on 2019-8-21
        List<RestProduct> restProductList = mapper.mapAsList(productList, RestProduct.class);
        target.setProductList(restProductList);
        return RestResultGenerator.success(target);
    }

    /**
     * 获取服务网点下的师傅列表
     *
     * @param servicePointId
     * @return
     */
    public RestResult<Object> getEngineerList(Long servicePointId) {
        List<Engineer> engineerList = getEngineersFromCache(servicePointId);
        List<RestEngineer> restEngineerList = mapper.mapAsList(engineerList, RestEngineer.class);
        for (RestEngineer restEngineer : restEngineerList) {
            List<Long> areaIds = getEngineerAreaIds(Long.valueOf(restEngineer.getId()));
            Area source;
            RestArea target;
            List<RestArea> restAreaList = Lists.newArrayList();
            for (Long areaId : areaIds) {
                source = areaService.getFromCache(areaId);
                target = mapper.map(source, RestArea.class);
                restAreaList.add(target);
            }
            restEngineer.setAreaList(restAreaList);
        }
        return RestResultGenerator.success(restEngineerList);
    }

    /**
     * 获取可派单师傅列表
     *
     * @param getPlanEngineerList
     * @param servicePointId
     * @return
     */
    public RestResult<Object> getPlanEngineerList(RestGetPlanEngineerList getPlanEngineerList, Long servicePointId) {
        Long areaId = Long.valueOf(getPlanEngineerList.getAreaId());
        Integer currentEngineerId = null;
        if (getPlanEngineerList.getCurrentEngineerId() != null && getPlanEngineerList.getCurrentEngineerId().length() > 0) {
            currentEngineerId = Integer.valueOf(getPlanEngineerList.getCurrentEngineerId());
        }
        Engineer queryEntity = new Engineer();
        queryEntity.setArea(new Area(areaId));
        queryEntity.setServicePoint(new ServicePoint(servicePointId));
        queryEntity.setExceptId(currentEngineerId);
        queryEntity.setMasterFlag(null);
        queryEntity.setAppFlag(null);
        List<Engineer> engineerList = findEngineerList(queryEntity);//切换为微服务
        List<RestEngineer> restEngineerList = mapper.mapAsList(engineerList, RestEngineer.class);
        for (RestEngineer rEngineer : restEngineerList) {
            List<Long> areaIds = getEngineerAreaIds(Long.valueOf(rEngineer.getId()));
            RestArea target;
            List<RestArea> restAreaList = Lists.newArrayList();
            for (Long aId : areaIds) {
                target = mapper.map(areaService.getFromCache(aId), RestArea.class);
                restAreaList.add(target);
            }
            rEngineer.setAreaList(restAreaList);
        }
        return RestResultGenerator.success(restEngineerList);
    }

    /**
     * 获取网点余额信息
     *
     * @param servicePointId
     * @return
     */
    public RestResult<Object> getBalance(Long servicePointId) {
        RestServicePointBalance restServicePointBalance = null;
        ServicePointFinance servicePointFinance = dao.getFinanceForRestBalance(servicePointId);
        if (servicePointFinance != null) {
            restServicePointBalance = mapper.map(servicePointFinance, RestServicePointBalance.class);
        }
        EngineerCurrency firstEngineerCurrency = engineerCurrencyDao.getFirstCurrency(servicePointId);
        if (firstEngineerCurrency != null && firstEngineerCurrency.getCreateDate() != null) {
            restServicePointBalance.setMonthCount(DateUtils.getDateDiffMonth(firstEngineerCurrency.getCreateDate(), new Date()) + 1);
        } else {
            restServicePointBalance.setMonthCount(0);
        }
        return RestResultGenerator.success(restServicePointBalance);
    }

    //endregion api functions


    /**
     * 按需更改信息：订单相关统计数量只做增减,客评：平均运算，四舍五入
     *
     * @param maps
     */
    public void updateServicePointByMap(HashMap<String, Object> maps) {
        MSErrorCode msErrorCode = msServicePointService.updateServicePointByMap(maps);
        if (msErrorCode.getCode() > 0) {
            throw new RuntimeException("调用微服务按需更新网点信息失败.失败原因:" + msErrorCode.getMsg());
        }
    }

    /**
     * 是否启用网点保险扣除(微服务调用) // add on 2019-9-17
     *
     * @param id               网点id
     * @param appInsuranceFlag
     * @param updateBy
     * @param updateDate
     */
    public void appReadInsuranceClause(Long id, Integer appInsuranceFlag, Long updateBy, Date updateDate) {
        MSErrorCode msErrorCode = msServicePointService.appReadInsuranceClause(id, appInsuranceFlag, updateBy, updateDate);
        if (msErrorCode.getCode() > 0) {
            throw new RuntimeException("调用微服务是否启用网点保险扣除失败.失败原因:" + msErrorCode.getMsg());
        }
    }

    /**
     * 更新网点地址信息(微服务调用)// add on 2019-9-17
     *
     * @param servicePoint
     */
    public void updateServicePointAddress(ServicePoint servicePoint) {
        MSErrorCode msErrorCode = msServicePointService.updateServicePointAddress(servicePoint);
        if (msErrorCode.getCode() > 0) {
            throw new RuntimeException("调用微服务更新网点地址信息失败.失败原因:" + msErrorCode.getMsg());
        }
    }

    /**
     * 更新网点的银行账号信息(微服务调用)// add on 2019-9-17
     *
     * @param servicePoint
     */
    public void updateServicePointBankAccountInfo(ServicePoint servicePoint) {
        MSErrorCode msErrorCode = msServicePointService.updateServicePointBankAccountInfo(servicePoint);
        if (msErrorCode.getCode() > 0) {
            throw new RuntimeException("调用微服务更新网点的银行账号信息失败.失败原因:" + msErrorCode.getMsg());
        }
    }

    /**
     * 记录体温并更新微服务
     * @param loginUserInfo
     * @param restEngineerTemperature
     * @return
     */
    public RestResult<Object> saveEngineerTemperature(RestLoginUserInfo loginUserInfo,RestEngineerTemperature restEngineerTemperature){
        RestHealthStatus restHealthStatus = new RestHealthStatus();
        try {
                EngineerTemperature engineerTemperature = new EngineerTemperature();
                engineerTemperature.setHealthStatus(
                    EngineerTemperatureUtil.isNormal(restEngineerTemperature.getHealthOption(), restEngineerTemperature.getTemperature()));

                restHealthStatus.setHealthStatus(engineerTemperature.getHealthStatus());
                // 更新微服务体温
            if (engineerTemperature.getHealthStatus()==TemperatureEnum.normal.value) {
                Engineer engineer = new Engineer();
                engineer.setId(loginUserInfo.getEngineerId());
                engineer.setTemperature(restEngineerTemperature.getTemperature());
                engineer.setUpdateBy(new User(loginUserInfo.getUserId()));
                engineer.setUpdateDate(new Date());
                MSErrorCode msErrorCode = msEngineerService.updateTemperature(engineer);

                if (msErrorCode.getCode() > 0) {
                    return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
                }
            }
                engineerTemperature.setEngineerId(loginUserInfo.getEngineerId());
                engineerTemperature.setHealthOption(restEngineerTemperature.getHealthOption());
                engineerTemperature.setTemperature(restEngineerTemperature.getTemperature());
                engineerTemperature.setCreateDate(new Date());
                engineerTemperature.setCreateById(loginUserInfo.getUserId());
                engineerTemperature.setUpdateById(loginUserInfo.getUserId());
                engineerTemperature.setUpdateDate(engineerTemperature.getCreateDate());
                engineerTemperature.setQuarter(QuarterUtils.getQuarter(engineerTemperature.getCreateDate()));
                engineerTemperatureDao.insert(engineerTemperature);

            }catch (Exception e){
                log.info("更新体温异常出错{}",e.getMessage());
                return RestResultGenerator.exception("保存体温异常！");
            }
        return RestResultGenerator.success(restHealthStatus);
    }

    /**
     * 获取安维体温列表
     * @return
     */
    public RestResult<Object> getEngineerTemperatureList(Page<RestEngineerTemperatureInfo> page,EngineerTemperatureSearchModel engineerTemperatureSearchModel,Page<EngineerTemperatureSearchModel> searchPage) {

        List<EngineerTemperature> temperaturePage = engineerTemperatureDao.getEngineerTemperatureList(engineerTemperatureSearchModel);
        List<RestEngineerTemperatureInfo> list = Lists.newArrayList();
        RestEngineerTemperatureInfo restEngineerTemperatureInfo = null;
        Page<RestEngineerTemperatureInfo> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), searchPage.getCount());
        rtnPage.setOrderBy(searchPage.getOrderBy());
            for (EngineerTemperature engineerTemperature : temperaturePage) {

                restEngineerTemperatureInfo = mapper.map(engineerTemperature, RestEngineerTemperatureInfo.class);

                list.add(restEngineerTemperatureInfo);
            }
            rtnPage.setList(list);

        return RestResultGenerator.success(rtnPage);
    }

    /**
     * 获取是否开启互助基金
     * @param id
     * @return
     */
    public NameValuePair<Integer, Integer> getInsuranceFlagByIdForAPI(Long id){
        return msServicePointService.getInsuranceFlagByIdForAPI(id);
    }
    /**
     *  app手动关闭购买互助基金
     * @param mdServicePoint
     * @return
     */
    public MSErrorCode updateInsuranceFlagForAPI(MDServicePoint mdServicePoint){
        return msServicePointService.updateInsuranceFlagForAPI(mdServicePoint);
    }
}
