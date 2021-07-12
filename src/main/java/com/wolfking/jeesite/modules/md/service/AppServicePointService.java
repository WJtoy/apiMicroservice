package com.wolfking.jeesite.modules.md.service;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDServicePoint;
import com.kkl.kklplus.entity.md.MDServicePointAddress;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.api.entity.common.RestAppException;
import com.wolfking.jeesite.modules.api.entity.md.*;
import com.wolfking.jeesite.modules.api.util.InsuranceFlagUtils;
import com.wolfking.jeesite.modules.api.util.RestEnum;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.md.dao.ServicePointDao;
import com.wolfking.jeesite.modules.md.entity.*;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.service.AreaService;
import com.wolfking.jeesite.modules.sys.utils.AreaUtils;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.b2bcenter.exception.AddressParseFailureException;
import com.wolfking.jeesite.ms.providermd.service.MSServicePointService;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.wolfking.jeesite.common.config.redis.RedisConstant.RedisDBType.REDIS_TEMP_DB;
import static com.wolfking.jeesite.common.config.redis.RedisConstant.VERCODE_KEY;


/**
 * 服务网点
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AppServicePointService extends LongIDBaseService {

    @Resource
    private ServicePointDao servicePointDao;
    @Autowired
    private ServicePointService servicePointService;

    @Autowired
    private ServicePointFinanceService servicePointFinanceService;

    @Autowired
    private ServicePointLogService servicePointLogService;

    @Autowired
    private MSServicePointService msServicePointService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    private MapperFacade mapper;

    private static final String LOCK_EDIT_SERVICE_POINT_KEY = "lock:servicepoint:%s";


    /***
     *  获取互助基金标志
     */
    public RestServicePointInsuranceFlag getInsuranceFlagByIdForAPI(Long servicePointId){
        RestServicePointInsuranceFlag restServicePointInsuranceFlag = new RestServicePointInsuranceFlag();
        NameValuePair<Integer, Integer> nameValuePair =  servicePointService.getInsuranceFlagByIdForAPI(servicePointId);
        restServicePointInsuranceFlag.setAppCloseInsuranceFlag(InsuranceFlagUtils.getInsurance(nameValuePair.getValue()));
        restServicePointInsuranceFlag.setInsuranceFlag(InsuranceFlagUtils.getInsurance(nameValuePair.getName()));
        return restServicePointInsuranceFlag;
    }

    /**
     * app关闭互助基金
     */
    public MSErrorCode closeMutualFund(Long servicePointId,Long userId){
        MDServicePoint mdServicePoint = new MDServicePoint();
        mdServicePoint.setId(servicePointId);
        mdServicePoint.setInsuranceFlag(0);     //关闭购买保险
        mdServicePoint.setUpdateDate(new Date());
        mdServicePoint.setUpdateById(userId);
        return servicePointService.updateInsuranceFlagForAPI(mdServicePoint);
    }

    /**
     * APP阅读保险条款
     */
    public void appReadInsuranceClause(ServicePoint servicePoint, Integer appInsuranceFlag, User user) {
        Long servicePointId = servicePoint.getId();
        String lockKey = String.format(LOCK_EDIT_SERVICE_POINT_KEY, servicePointId);
        boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, 1, 60);
        if (!locked) {
            throw new RestAppException("网点正在修改中，请稍候重试。");
        }
        try {
            //servicePointDao.appReadInsuranceClause(servicePoint.getId(), appInsuranceFlag, user.getId(), new Date()); //mark on 2020-1-14  web端去servicePoint
            servicePointService.appReadInsuranceClause(servicePoint.getId(), appInsuranceFlag, user.getId(), new Date()); // add on 2019-9-17
            servicePoint.setAppInsuranceFlag(appInsuranceFlag);
            servicePointLogService.saveServicePointLog(servicePoint.getId(), ServicePointLog.ServicePointLogType.EDIT_APP_INSURANCE_FLAG, "APP阅读保险条款",
                    ServicePointLogService.toServicePointJson(servicePoint), user);
            //redisUtils.zSetEX(RedisConstant.RedisDBType.REDIS_MD_DB, RedisConstant.MD_SERVICEPOINT_ALL, servicePoint, servicePointId, 0L);  //mark on 2020-1-17   web端去md_servicepoint
        } catch (Exception e) {
            LogUtils.saveLog("APP阅读保险条款", "AppServicePointService.appReadInsuranceClause", servicePoint.getServicePointNo(), e, user);
            throw new RuntimeException(e);
        } finally {
            if (lockKey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey);
            }
        }
    }

    /**
     * 保存网点地址信息
     */
    public void saveServicePointAddress(Long servicePointId, RestServicePointAddress addressInfo, User user) {
        if (addressInfo == null || StringUtils.isBlank(addressInfo.getAddress())
                || addressInfo.getLatitude() == null || addressInfo.getLatitude() <= 0
                || addressInfo.getLongitude() == null || addressInfo.getLongitude() <= 0) {
            throw new RestAppException("参数不全，请确认后重试。");
        }

        Long areaId;
        String subAddress;
        String[] result;
        try {
            //result = AreaUtils.parseAddress(addressInfo.getAddress().replace(" ", ""));   // mark on 2020-12-9
            result = AreaUtils.parseAddressFromMS(addressInfo.getAddress().replace(" ", ""));  // add on 2020-12-9
        } catch (Exception e) {
            throw new RestAppException("网点地址解析失败");
        }
        if (result != null && result.length > 2) {
            areaId = StringUtils.toLong(result[0]);
            subAddress = result[2];
        } else {
            throw new RestAppException("网点地址解析失败");
        }
        ServicePoint servicePoint = servicePointService.getFromCache(servicePointId);
        if (servicePoint == null) {
            throw new RestAppException("读取网点信息失败");
        }
        String lockKey = String.format(LOCK_EDIT_SERVICE_POINT_KEY, servicePointId);
        boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, 1, 60);
        if (!locked) {
            throw new RestAppException("网点正在修改中，请稍候重试。");
        }
        try {
            Area area = new Area(areaId);
            area.setName(AreaUtils.getCountyName(areaId));
            servicePoint.setArea(area);
            servicePoint.setAddress(addressInfo.getAddress());
            servicePoint.setSubAddress(subAddress);
            servicePoint.setLongitude(addressInfo.getLongitude());
            servicePoint.setLatitude(addressInfo.getLatitude());
            servicePoint.setUpdateBy(user);
            servicePoint.setUpdateDate(new Date());
            //servicePointDao.updateServicePointAddress(servicePoint);   //mark on 2020-1-14  web端去servicePoint
            servicePointService.updateServicePointAddress(servicePoint); // add on 2019-9-17
            servicePointLogService.saveServicePointLog(servicePoint.getId(), ServicePointLog.ServicePointLogType.EDIT_APP_SERVICEPOINT_ADDRESS, "APP保存网点地址",
                    ServicePointLogService.toServicePointJson(servicePoint), user);
            //redisUtils.zSetEX(RedisConstant.RedisDBType.REDIS_MD_DB, RedisConstant.MD_SERVICEPOINT_ALL, servicePoint, servicePointId, 0L); //mark on 2020-1-17   web端去md_servicepoint
        } catch (Exception e) {
            LogUtils.saveLog("APP保存网点地址", "AppServicePointService.saveServicePointAddress", servicePointId.toString(), e, user);
            throw new RestAppException("保存网点地址失败");
        } finally {
            if (lockKey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey);
            }
        }
    }

    /**
     * 保存网点账号信息
     */
    public void saveServicePointBankAccountInfo(Long servicePointId, Engineer engineer, RestServicePointBankAccountInfo accountInfo, User user) {
        if (accountInfo == null || StringUtils.isBlank(accountInfo.getBank())
                || StringUtils.isBlank(accountInfo.getBankNo())
                || StringUtils.isBlank(accountInfo.getBankOwner())
                || StringUtils.isBlank(accountInfo.getCode())) {
            throw new RestAppException("参数不全，请确认后重试。");
        }
        ServicePoint servicePoint = servicePointService.getFromCache(servicePointId);
        if (servicePoint == null || servicePoint.getFinance() == null) {
            throw new AddressParseFailureException("读取网点信息失败");
        }
        String verifyCodeCacheKey = String.format(VERCODE_KEY, RestEnum.VerifyCodeType.saveServicePointBankAccountInfo.ordinal(), engineer.getContactInfo());
        if (redisUtilsLocal.exists(REDIS_TEMP_DB, verifyCodeCacheKey)) {
            String verifyCode = (String) redisUtilsLocal.get(REDIS_TEMP_DB, verifyCodeCacheKey, String.class);
            if (!verifyCode.equals(accountInfo.getCode())) {
                throw new RestAppException("验证码错误");
            }
        } else {
            throw new RestAppException("验证码已过期");
        }
        String lockKey = String.format(LOCK_EDIT_SERVICE_POINT_KEY, servicePointId);
        boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, 1, 60);
        if (!locked) {
            throw new RestAppException("网点正在修改中，请稍候重试。");
        }
        try {
            Dict bankDict = MSDictUtils.getDictByValue(accountInfo.getBank(), Dict.DICT_TYPE_BANK_TYPE);
            if (bankDict != null) {
                servicePoint.setBank(bankDict);
                servicePoint.setBankNo(accountInfo.getBankNo());
                servicePoint.setBankOwner(accountInfo.getBankOwner());
                servicePoint.setUpdateBy(user);
                servicePoint.setUpdateDate(new Date());
                //servicePointDao.updateServicePointBankAccountInfo(servicePoint); //mark on 2020-1-14  web端去servicePoint
                servicePointService.updateServicePointBankAccountInfo(servicePoint); // add on 2019-9-17
                ServicePointFinance finance = servicePoint.getFinance();
                finance.setBank(bankDict);
                finance.setBranch(accountInfo.getBranch());
                finance.setBankNo(accountInfo.getBankNo());
                finance.setBankOwner(accountInfo.getBankOwner());
                servicePointDao.updateServicePointFIBankAccountInfo(finance);
                servicePointLogService.saveServicePointLog(servicePoint.getId(), ServicePointLog.ServicePointLogType.EDIT_APP_SERVICEPOINT_BANK_ACCTOUN_INFO, "APP保存网点银行账号信息",
                        ServicePointLogService.toServicePointJson(servicePoint), user);
                //redisUtils.zSetEX(RedisConstant.RedisDBType.REDIS_MD_DB, RedisConstant.MD_SERVICEPOINT_ALL, servicePoint, servicePointId, 0L); //mark on 2020-1-17   web端去md_servicepoint
                servicePointFinanceService.updateCache(finance);  //add on 2020-5-4  更新ServicePointFinance
            }
        } catch (Exception e) {
            LogUtils.saveLog("APP保存网点银行账号信息", "AppServicePointService.saveServicePointBankAccountInfo", servicePointId.toString(), e, user);
            throw new RestAppException("APP保存网点银行账号信息");
        } finally {
            if (lockKey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey);
            }
        }
    }

    /**
     * 保存网点收货地址信息
     */
    public void saveServicePointConsigneeAddress(Long servicePointId, RestServicePointConsigneeAddress address, User user) {
        if (address == null || StringUtils.isBlank(address.getUserName())
                || StringUtils.isBlank(address.getUserPhone())
                || StringUtils.isBlank(address.getAreaName())
                || StringUtils.isBlank(address.getAddress())) {
            throw new RestAppException("参数不全，请确认后重试。");
        }
        Long areaId;
        Long subAreaId = 0L;
        String detailAddress = "";
        String[] result;
        try {
            //result = AreaUtils.decodeAddressGaode((address.getAreaName() + address.getAddress()).replace(" ", ""));  // mark on 2020-12-9
            result = AreaUtils.decodeAddressGaodeFromMS((address.getAreaName() + address.getAddress()).replace(" ", ""));  // add on 2020-12-9
        } catch (Exception e) {
            throw new RestAppException("网点地址解析失败");
        }
        if (result != null && result.length > 0) {
            areaId = StringUtils.toLong(result[0]);
            if (result.length > 1) {
                subAreaId = StringUtils.toLong(result[1]);
                if (result.length > 3) {
                    detailAddress = result[3];
                }
            }
        } else {
            throw new RestAppException("网点地址解析失败");
        }
        ServicePoint servicePoint = servicePointService.getFromCache(servicePointId);
        if (servicePoint == null) {
            throw new RestAppException("读取网点信息失败");
        }
        String lockKey = String.format(LOCK_EDIT_SERVICE_POINT_KEY, servicePointId);
        boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, 1, 60);
        if (!locked) {
            throw new RestAppException("网点正在修改中，请稍候重试。");
        }
        try {
            MDServicePointAddress params = new MDServicePointAddress();
            params.setServicePointId(servicePointId);
            params.setAreaId(areaId);
            if(areaId != null){
                Area city = areaService.getFromCache(areaId);
                if (city != null) {
                    params.setCityId(city.getParentId());
                    Area province = areaService.getFromCache(city.getParentId());
                    if (province != null) {
                        params.setProvinceId(province.getParentId());
                    }
                }
            }
            params.setSubAreaId(subAreaId);
            params.setAddress(StringUtils.isNotBlank(detailAddress) ? detailAddress : address.getAddress());
            params.setUserName(address.getUserName());
            params.setContactInfo(address.getUserPhone());
            params.setCreateById(user.getId());
            params.setUpdateById(user.getId());
            params.setCreateDate(new Date());
            msServicePointService.saveAddress(params);
        } catch (Exception e) {
            LogUtils.saveLog("APP保存网点地址", "AppServicePointService.saveServicePointAddress", servicePointId.toString(), e, user);
            throw new RestAppException("保存网点地址失败");
        } finally {
            if (lockKey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey);
            }
        }
    }


    public RestResult<Object> getServicePointInfo(Long servicePointId) {
        ServicePoint servicePoint = servicePointService.getFromCache(servicePointId);
        RestServicePoint target = mapper.map(servicePoint, RestServicePoint.class);
        //切换为微服务
        RestDict type = mapper.map(MSDictUtils.getDictByValue(String.valueOf(servicePoint.getProperty() == 0 ? 1 : servicePoint.getProperty()), "ServicePointProperty"), RestDict.class);
        target.setType(type);
        target.setPhone1(servicePoint.getContactInfo1());
        target.setPhone2(servicePoint.getContactInfo2());
        target.setPrimaryName(servicePoint.getPrimary().getName());
        RestServicePointFinance finance = mapper.map(servicePoint.getFinance(), RestServicePointFinance.class);
        target.setFinance(finance);
        List<Area> areaList = servicePointService.getAreas(servicePointId);
        List<RestArea> restAreaList = mapper.mapAsList(areaList, RestArea.class);
        target.setAreaList(restAreaList);
        List<Product> productList = servicePointService.getProducts(servicePointId);
        List<RestProduct> restProductList = mapper.mapAsList(productList, RestProduct.class);
        target.setProductList(restProductList);
        MDServicePointAddress address = msServicePointService.getAddressByServicePointIdFromCache(servicePointId);
        if (address != null) {
            RestServicePointConsigneeAddress consigneeAddress = new RestServicePointConsigneeAddress();
            consigneeAddress.setUserName(address.getUserName());
            consigneeAddress.setUserPhone(address.getContactInfo());
            if (address.getAreaId() != null) {
                String areaFullName = AreaUtils.getCountyFullName(address.getAreaId());
                if (StringUtils.isNotBlank(areaFullName)) {
                    consigneeAddress.setAreaName(areaFullName);
                }
            }
            consigneeAddress.setAddress(address.getAddress());
            target.setConsigneeAddress(consigneeAddress);
        }
        return RestResultGenerator.success(target);
    }
}
