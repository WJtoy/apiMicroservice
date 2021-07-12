/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sys.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wolfking.jeesite.common.config.Global;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.CustomerAccountProfile;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sys.dao.RoleDao;
import com.wolfking.jeesite.modules.sys.dao.UserDao;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Office;
import com.wolfking.jeesite.modules.sys.entity.Role;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.utils.ApiPropertiesUtils;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerAccountProfileService;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerService;
import com.wolfking.jeesite.ms.providersys.service.MSSysAreaService;
import com.wolfking.jeesite.ms.providersys.service.MSSysOfficeService;
import com.wolfking.jeesite.ms.utils.MSUserUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.util.Lists;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户工具类
 *
 * @author ThinkGem
 * @version 2013-12-05
 */
public class UserUtils {

    public static long globExpire = 240;// 4*60分钟

    private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
    private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
    private static MSCustomerService msCustomerService = SpringContextHolder.getBean(MSCustomerService.class);
    private static MSCustomerAccountProfileService msCustomerAccountProfileService = SpringContextHolder.getBean(MSCustomerAccountProfileService.class);
    private static ServicePointService servicePointService = SpringContextHolder.getBean(ServicePointService.class); // add on 2019-9-16
    private static RedisUtilsLocal redisUtilsLocal = SpringContextHolder.getBean(RedisUtilsLocal.class);
    private static MSSysAreaService msAreaService = SpringContextHolder.getBean(MSSysAreaService.class);
    private static MSSysOfficeService msSysOfficeService = SpringContextHolder.getBean(MSSysOfficeService.class);

    /**
     * 根据ID获取用户
     *
     * @param id
     * @return 取不到返回null
     */
    public static User get(Long id) {
        return get(id, null, null);
    }

    /**
     * 根据ID获取用户
     *
     * @param id
     * @return 取不到返回null
     */
    public static User get(Long id, String session) {
        return get(id, session, null);
    }

    /**
     * 根据ID获取用户
     *
     * @param id
     * @param session   session
     * @param syncCache 同步缓存标记,判断缓存中key是否存在，不存在的重新读取并更新缓存
     * @return 取不到返回null
     */
    public static User get(Long id, String session, Boolean syncCache) {
        User user = (User) redisUtilsLocal.get(String.format(RedisConstant.SYS_USER_ID, id), User.class);
        if (user == null) {
            // user = userDao.get(id);  // mark on 2020-12-14
            /*
            // add on 2020-12-9 begin
            User newUser = userDao.getNew(id);
            if (newUser != null) {
                //add on 2020-12-12 begin
                Set<Long> officeIds = Sets.newHashSet();
                Optional.ofNullable(newUser.getCompany()).map(Office::getId).ifPresent(officeIds::add);
                Optional.ofNullable(newUser.getOffice()).map(Office::getId).ifPresent(officeIds::add);

                List<Office> officeList = msSysOfficeService.findSpecColumnListByIds(Lists.newArrayList(officeIds));
                Map<Long, Office> officeMap = Maps.newHashMap();
                Optional.ofNullable(officeList).ifPresent(r->r.stream().collect(Collectors.toMap(p->p.getId(),p->p)).putAll(officeMap));
                Optional.ofNullable(newUser).ifPresent(r->{
                    Long companyId = Optional.ofNullable(r.getCompany()).map(Office::getId).orElse(null);
                    Optional.ofNullable(officeMap.get(companyId)).ifPresent(r::setCompany);  // 主要是获取office中的code，name

                    Long officeId = Optional.ofNullable(r.getCompany()).map(Office::getId).orElse(null);
                    Optional.ofNullable(officeMap.get(officeId)).ifPresent(r::setOffice);  // 主要是获取office中的code，name
                });
                //add on 2020-12-12 end

                List<Long> AreaIdsList = Lists.newArrayList();
                Optional.ofNullable(newUser.getCompany()).map(Office::getArea).map(Area::getId).ifPresent(AreaIdsList::add);
                Optional.ofNullable(newUser.getOffice()).map(Office::getArea).map(Area::getId).ifPresent(AreaIdsList::add);

                List<Area> msAreaList = msAreaService.findSpecListByIds(AreaIdsList);
                if (ObjectUtils.isEmpty(msAreaList)) {
                    Map<Long,Area>  areaMap = msAreaList.stream().collect(Collectors.toMap(Area::getId, r->r, (v2,v1)->v1));
                    Optional.ofNullable(newUser.getCompany()).map(Office::getArea).ifPresent(r->{
                        Area area = areaMap.get(r.getId());
                        if (area != null) {
                            r.setName(area.getName());
                            r.setParent(new Area(r.getParentId()));
                            r.setParentIds(area.getParentIds());
                        }
                    });
                    Optional.ofNullable(newUser.getOffice()).map(Office::getArea).ifPresent(r->{
                        Area area = areaMap.get(r.getId());
                        if (area != null) {
                            r.setName(area.getName());
                            r.setParent(new Area(r.getParentId()));
                            r.setParentIds(area.getParentIds());
                        }
                    });
                }

                List<Area> localAreaList = Lists.newArrayList();
                Optional.ofNullable(newUser.getCompany()).map(Office::getArea).ifPresent(localAreaList::add);
                Optional.ofNullable(newUser.getOffice()).map(Office::getArea).ifPresent(localAreaList::add);

                //msAreaService.compareListArea(id.toString(), localAreaList, msAreaList, "UserUtils.get");  // mark on 2020-12-14
                */

            user = userDao.getNew(id);
            if (user != null) {
                Set<Long> officeIds = Sets.newHashSet();
                Optional.ofNullable(user.getCompany()).map(Office::getId).ifPresent(officeIds::add);
                Optional.ofNullable(user.getOffice()).map(Office::getId).ifPresent(officeIds::add);

                List<Office> officeList = msSysOfficeService.findSpecColumnListByIds(Lists.newArrayList(officeIds));
                Map<Long, Office> officeMap = ObjectUtils.isEmpty(officeList)?Maps.newHashMap(): officeList.stream().collect(Collectors.toMap(p->p.getId(),p->p));

                Optional.ofNullable(user).ifPresent(r -> {
                    Long companyId = Optional.ofNullable(r.getCompany()).map(Office::getId).orElse(null);
                    Optional.ofNullable(officeMap.get(companyId)).ifPresent(r::setCompany);  // 主要是获取office中的code，name

                    Long officeId = Optional.ofNullable(r.getOffice()).map(Office::getId).orElse(null);
                    Optional.ofNullable(officeMap.get(officeId)).ifPresent(r::setOffice);  // 主要是获取office中的code，name
                });

                List<Long> AreaIdsList = Lists.newArrayList();
                Optional.ofNullable(user.getCompany()).map(Office::getArea).map(Area::getId).ifPresent(AreaIdsList::add);
                Optional.ofNullable(user.getOffice()).map(Office::getArea).map(Area::getId).ifPresent(AreaIdsList::add);

                List<Area> msAreaList = msAreaService.findSpecListByIds(AreaIdsList);
                if (!ObjectUtils.isEmpty(msAreaList)) {
                    Map<Long, Area> areaMap = msAreaList.stream().collect(Collectors.toMap(Area::getId, r -> r, (v2, v1) -> v1));
                    Optional.ofNullable(user.getCompany()).map(Office::getArea).ifPresent(r -> {
                        Area area = areaMap.get(r.getId());
                        if (area != null) {
                            r.setName(area.getName());
                            r.setParent(new Area(r.getParentId()));
                            r.setParentIds(area.getParentIds());
                        }
                    });
                    Optional.ofNullable(user.getOffice()).map(Office::getArea).ifPresent(r -> {
                        Area area = areaMap.get(r.getId());
                        if (area != null) {
                            r.setName(area.getName());
                            r.setParent(new Area(r.getParentId()));
                            r.setParentIds(area.getParentIds());
                        }
                    });
                }
            }
            // add on 2020-12-9 end

            if (user == null) {
                return null;
            }
            if (user.getDelFlag().equals(User.DEL_FLAG_DELETE)) {
                return null;
            }
            user.setLoginDate(new Date());
//            long timeout = Long.valueOf(Global.getConfig("cache.timeout")) - RandomUtils.nextInt(0, 60 * 30);
            long timeout = ApiPropertiesUtils.getCache().getTimeout() - RandomUtils.nextInt(0, 60 * 30);
            loadUserInfo(user, session, timeout);
        } else if (syncCache != null && syncCache == true) {
//            long timeout = Long.valueOf(Global.getConfig("cache.timeout")) - RandomUtils.nextInt(0, 60 * 30);
            long timeout = ApiPropertiesUtils.getCache().getTimeout() - RandomUtils.nextInt(0, 60 * 30);
            loadUserInfo(user, session, timeout);
        }
        return user;
    }

    /**
     * 根据ID获取用户帐号信息(不读取相关客户，安维信息)
     *
     * @param id
     * @return 取不到返回null
     */
    public static User getAcount(Long id) {
        User user = (User) redisUtilsLocal.get(String.format(RedisConstant.SYS_USER_ID, id), User.class);
        if (user == null) {
            user = userDao.getBaseInfo(id);
            if (user == null) {
                return null;
            }
            if (user.getDelFlag() == User.DEL_FLAG_DELETE) {
                return null;
            }
        }
        return user;
    }

    /**
     * 根据登录名获取用户
     * 先取缓存，再取数据库
     *
     * @param loginName
     * @return 取不到返回null
     */
    /*
    // 没有地方调用，代码注释  //mark on 2020-12-14
    public static User getByLoginName(String loginName) {
        User user = null;
        Long id = (Long) redisUtilsLocal.get(String.format(RedisConstant.SYS_USER_LOGINNAME, loginName), Long.class);
        if (id != null) {
            user = (User) redisUtilsLocal.get(String.format(RedisConstant.SYS_USER_ID, id), User.class);
        }
        if (user == null) {
            user = userDao.getByLoginName(new User(null, loginName));

            // add on 2020-12-9 begin
            User newUser = userDao.getByLoginName(new User(null, loginName));
            if (newUser != null) {
                List<Long> AreaIdsList = Lists.newArrayList();
                Optional.ofNullable(newUser.getCompany()).map(Office::getArea).map(Area::getId).ifPresent(AreaIdsList::add);
                Optional.ofNullable(newUser.getOffice()).map(Office::getArea).map(Area::getId).ifPresent(AreaIdsList::add);

                List<Area> msAreaList = msAreaService.findSpecListByIds(AreaIdsList);
                if (ObjectUtils.isEmpty(msAreaList)) {
                    Map<Long,Area>  areaMap = msAreaList.stream().collect(Collectors.toMap(Area::getId, r->r, (v2,v1)->v1));
                    Optional.ofNullable(newUser.getCompany()).map(Office::getArea).ifPresent(r->{
                        Area area = areaMap.get(r.getId());
                        if (area != null) {
                            r.setName(area.getName());
                            r.setParent(new Area(r.getParentId()));
                            r.setParentIds(area.getParentIds());
                        }
                    });
                    Optional.ofNullable(newUser.getOffice()).map(Office::getArea).ifPresent(r->{
                        Area area = areaMap.get(r.getId());
                        if (area != null) {
                            r.setName(area.getName());
                            r.setParent(new Area(r.getParentId()));
                            r.setParentIds(area.getParentIds());
                        }
                    });
                }

                List<Area> localAreaList = Lists.newArrayList();
                Optional.ofNullable(newUser.getCompany()).map(Office::getArea).ifPresent(localAreaList::add);
                Optional.ofNullable(newUser.getOffice()).map(Office::getArea).ifPresent(localAreaList::add);

                msAreaService.compareListArea("loginName=" + loginName , localAreaList, msAreaList, "UserUtils.getByLoginName");
            }
            // add on 2020-12-9 end

            if (user == null) {
                return null;
            }
        }
        return user;
    }
    */

    /**
     * 根据登录名获取APP用户
     *
     * @param loginName
     * @return
     */
    public static User getAppUserByLoginName(String loginName) {
        User newUser = userDao.getAppUserByLoginNameNew(new User(null, loginName));
        if (newUser != null) {
            //add on 2020-12-12 begin
            Set<Long> officeIds = Sets.newHashSet();
            Optional.ofNullable(newUser.getCompany()).map(Office::getId).ifPresent(officeIds::add);
            Optional.ofNullable(newUser.getOffice()).map(Office::getId).ifPresent(officeIds::add);

            List<Office> officeList = msSysOfficeService.findSpecColumnListByIds(Lists.newArrayList(officeIds));
            Map<Long, Office> officeMap = ObjectUtils.isEmpty(officeList)?Maps.newHashMap(): officeList.stream().collect(Collectors.toMap(p->p.getId(),p->p));

            Optional.ofNullable(newUser).ifPresent(r->{
                Long companyId = Optional.ofNullable(r.getCompany()).map(Office::getId).orElse(null);
                Optional.ofNullable(officeMap.get(companyId)).ifPresent(r::setCompany);  // 主要是获取office中的code，name

                Long officeId = Optional.ofNullable(r.getOffice()).map(Office::getId).orElse(null);
                Optional.ofNullable(officeMap.get(officeId)).ifPresent(r::setOffice);  // 主要是获取office中的code，name
            });
            //add on 2020-12-12 end

            List<Long> AreaIdsList = Lists.newArrayList();
            Optional.ofNullable(newUser.getCompany()).map(Office::getArea).map(Area::getId).ifPresent(AreaIdsList::add);
            Optional.ofNullable(newUser.getOffice()).map(Office::getArea).map(Area::getId).ifPresent(AreaIdsList::add);

            List<Area> msAreaList = msAreaService.findSpecListByIds(AreaIdsList);
            if (!ObjectUtils.isEmpty(msAreaList)) {
                Map<Long,Area>  areaMap = msAreaList.stream().collect(Collectors.toMap(Area::getId, r->r, (v2,v1)->v1));
                Optional.ofNullable(newUser.getCompany()).map(Office::getArea).ifPresent(r->{
                    Area area = areaMap.get(r.getId());
                    if (area != null) {
                        r.setName(area.getName());
                        r.setParent(new Area(r.getParentId()));
                        r.setParentIds(area.getParentIds());
                    }
                });
                Optional.ofNullable(newUser.getOffice()).map(Office::getArea).ifPresent(r->{
                    Area area = areaMap.get(r.getId());
                    if (area != null) {
                        r.setName(area.getName());
                        r.setParent(new Area(r.getParentId()));
                        r.setParentIds(area.getParentIds());
                    }
                });
            }
        }
        return newUser;
    }

    /**
     * 装载用户信息
     * 根据用户类型装载区域，客户等到缓存
     *
     * @param user    用户实例
     * @param session session id,登录时传入值不为null
     * @param timeout 超时时间
     */
    public static void loadUserInfo(User user, String session, Long timeout) {
        if (user == null || user.getId() == null) {
            return;
        }
        Long mytimeout = timeout;
        if (mytimeout == null) {
//            mytimeout = Long.valueOf(Global.getConfig("cache.timeout")) - RandomUtils.nextInt(0, 60 * 30);
            mytimeout = ApiPropertiesUtils.getCache().getTimeout() - RandomUtils.nextInt(0, 60 * 30);
        }
        //厂商帐号
        if (user.isCustomer() && user.getCustomerAccountProfile().getId() != null && user.getCustomerAccountProfile().getId() > 0 && (user.getCustomerAccountProfile().getCustomer() == null || user.getCustomerAccountProfile().getCustomer().getId() == null)) {
            CustomerAccountProfile customerAccountProfile = msCustomerAccountProfileService.getById(user.getCustomerAccountProfile().getId());   // add on 2019-7-29 调用微服务
            if (customerAccountProfile != null
                    && customerAccountProfile.getCustomer() != null
                    && customerAccountProfile.getCustomer().getId() != null) {
                Customer customer = msCustomerService.get(customerAccountProfile.getCustomer().getId());
                if (customer != null) {
                    customerAccountProfile.setCustomer(customer);
                }
            }
            // add on 2019-6-29 end
            User sales = MSUserUtils.get(customerAccountProfile.getCustomer().getSales().getId());
            if (sales != null) {
                customerAccountProfile.getCustomer().getSales().setName(sales.getName());
                customerAccountProfile.getCustomer().getSales().setMobile(sales.getMobile());
                customerAccountProfile.getCustomer().getSales().setQq(sales.getQq());
            }
            user.setCustomerAccountProfile(customerAccountProfile);
        }
        //roles
        if (user.getRoleList() == null || user.getRoleList().size() == 0) {
            List<Role> roles = roleDao.getUserRoles(user.getId());
            user.setRoleList(roles);
        }
        if (user.isEngineer() && (user.getCompany() == null || user.getCompany().getId() == null)) {
            Engineer e = servicePointService.getEngineer(user.getEngineerId());
            if (e != null && e.getServicePoint() != null) {
                user.setCompany(new Office(e.getServicePoint().getId(), e.getServicePoint().getName()));
            }
        }
        if (session != null) {
            //user session <-> id
            String key = String.format(RedisConstant.SHIRO_USER_SESSION, user.getId());
            redisUtilsLocal.set(RedisConstant.RedisDBType.REDIS_CONSTANTS_DB, key, session, globExpire * RandomUtils.nextInt(60, 120));
            //redisUtils.set(RedisConstant.RedisDBType.REDIS_CONSTANTS_DB, key, session, 60 * RedisValueCache.globExpire);
            //user:name:*
            redisUtilsLocal.set(String.format(RedisConstant.SYS_USER_LOGINNAME, user.getLoginName()), user.getId(), mytimeout);//存储用户id
            redisUtilsLocal.set(String.format(RedisConstant.SYS_USER_ID, user.getId()), user, mytimeout);
        }
    }

    public static Long getAppUserByPhone(String phone) {
        Long user = userDao.getIdByMobile(phone);
        return user;
    }


}
