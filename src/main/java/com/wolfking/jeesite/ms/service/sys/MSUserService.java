package com.wolfking.jeesite.ms.service.sys;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.sys.SysUser;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sys.dao.UserDao;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.feign.sys.MSUserFeign;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Zhoucy
 * @date 2018/9/25 10:18
 **/
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MSUserService {

    @Autowired
    private MSUserFeign userFeign;

    @Autowired
    private MapperFacade mapper;

    @Resource
    private UserDao userDao;

    /**
     * 根据id查询用户信息
     */
    public User get(Long id) {
        User user = null;
        if (id != null) {
            MSResponse<SysUser> responseEntity = userFeign.get(id);
            if (MSResponse.isSuccess(responseEntity)) {
                user = mapper.map(responseEntity.getData(), User.class);
            }
        }
        return user != null ? user : new User();
    }

    /**
     * 根据id查询用户名称
     */
    public String getName(Long id) {
        String name = null;
        if (id != null) {
            MSResponse<String> responseEntity = userFeign.getName(id);
            if (MSResponse.isSuccess(responseEntity)) {
                name = responseEntity.getData();
            }
        }
        return StringUtils.toString(name);
    }

    /**
     * 根据id列表查询用户信息
     */
    public List<User> getListByUserIds(List<Long> userIds) {
        List<User> userList = null;
        if (userIds != null && !userIds.isEmpty()) {
            MSResponse<List<SysUser>> responseEntity = userFeign.getListByUserIds(userIds);
            if (MSResponse.isSuccess(responseEntity)) {
                userList = mapper.mapAsList(responseEntity.getData(), User.class);
            }
        }
        return userList != null ? userList : Lists.newArrayList();
    }

    /**
     * 根据id列表查询用户信息
     */
    public Map<Long, User> getMapByUserIds(List<Long> userIds) {
        Map<Long, User> userMap = null;
        if (userIds != null && !userIds.isEmpty()) {
            MSResponse<Map<Long, SysUser>> responseEntity = userFeign.getMapByUserIds(userIds);
            if (MSResponse.isSuccess(responseEntity)) {
                userMap = responseEntity.getData().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, i -> mapper.map(i.getValue(), User.class)));
            }
        }
        return userMap != null ? userMap : Maps.newHashMap();
    }

    /**
     * 根据id列表查询用户姓名
     */
    public Map<Long, String> getNamesByUserIds(List<Long> userIds) {
        Map<Long, String> nameMap = null;
        if (userIds != null && !userIds.isEmpty()) {
            MSResponse<Map<Long, String>> responseEntity = userFeign.getNamesByUserIds(userIds);
            if (MSResponse.isSuccess(responseEntity)) {
                nameMap = responseEntity.getData();
            }
        }
        return nameMap != null ? nameMap : Maps.newHashMap();
    }


    public List<User> getListByUserType(Integer userType) {
        List<User> userList = null;
        if (userType != null) {
            MSResponse<List<SysUser>> responseEntity = userFeign.getListByUserType(userType);
            if (MSResponse.isSuccess(responseEntity)) {
                userList = mapper.mapAsList(responseEntity.getData(), User.class);
            }
        }
        return userList != null ? userList : Lists.newArrayList();
    }

    /**
     * 根据用户类型查询用户信息
     */
    public Map<Long, User> getMapByUserType(Integer userType) {
        Map<Long, User> userMap = null;
        if (userType != null) {
            MSResponse<Map<Long, SysUser>> responseEntity = userFeign.getMapByUserType(userType);
            if (MSResponse.isSuccess(responseEntity)) {
                userMap = responseEntity.getData().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, i -> mapper.map(i.getValue(), User.class)));
            }
        }
        return userMap != null ? userMap : Maps.newHashMap();
    }

    /**
     * 根据用户类型查询用户名称
     */
    public Map<Long, String> getNameByUserType(Integer userType) {
        Map<Long, String> userMap = null;
        if (userType != null) {
            MSResponse<Map<Long, String>> responseEntity = userFeign.getNamesByUserType(userType);
            if (MSResponse.isSuccess(responseEntity)) {
                userMap = responseEntity.getData();
            }
        }
        return userMap != null ? userMap : Maps.newHashMap();
    }

    public void reloadAllToRedis() {
        MSResponse<Boolean> responseEntity = userFeign.reloadAllToRedis();
        if (!MSResponse.isSuccess(responseEntity)) {
            throw new RuntimeException(responseEntity.getMsg());
        }
    }

    public void reloadUserToRedis(User user) {
        if (user != null && user.getId() != null) {
            SysUser msUser = mapper.map(user, SysUser.class);
            MSResponse<Boolean> responseEntity = userFeign.reloadUserToRedis(msUser);
            if (!(MSResponse.isSuccess(responseEntity) && responseEntity.getData().equals(true))) {
                throw new RuntimeException(responseEntity.getMsg());
            }
        }
    }

    public void addUserToRedis(User user) {
        if (user != null && user.getId() != null) {
            SysUser msUser = mapper.map(user, SysUser.class);
            MSResponse<Boolean> responseEntity = userFeign.addUserToRedis(msUser);
            if (!(MSResponse.isSuccess(responseEntity) && responseEntity.getData().equals(true))) {
                throw new RuntimeException(responseEntity.getMsg());
            }
        }
    }

    @Transactional()
    public void refreshUserCacheByUserId(Long userId) {
        if (userId != null) {
            User user = userDao.getBaseInfo(userId);
            reloadUserToRedis(user);
        }
    }

    @Transactional()
    public void refreshUserCacheByEngineerId(Long engineerId) {
        if (engineerId != null) {
            User user = userDao.getBaseInfoByEngineerId(engineerId);
            reloadUserToRedis(user);
        }
    }

}
