/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.api.entity.md.RestCheckUpdate;
import com.wolfking.jeesite.modules.api.entity.md.RestDict;
import com.wolfking.jeesite.modules.api.entity.md.RestGetOptionList;
import com.wolfking.jeesite.modules.api.util.RestEnum;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.service.sys.MSDictService;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典Service
 *
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class DictService extends LongIDBaseService {
    @Autowired
    private MapperFacade mapper;

    @Autowired
    private MSDictService msDictService;

    /**
     * 检查更新
     *
     * @param checkUpdate
     * @return
     */
    public RestResult<Object> checkUpdate(RestCheckUpdate checkUpdate) {
//        String typeString = "";
        StringBuilder typeString = new StringBuilder();
        if (checkUpdate.getPhoneType() == RestEnum.PhoneType.iPhone.ordinal()) {
//            typeString = "IOSAppVersion";
            typeString.append("IOSAppVersion");
        } else if (checkUpdate.getPhoneType() == RestEnum.PhoneType.AndroidPhone.ordinal()) {
//            typeString = "AndroidAppVersion";
            typeString.append("AndroidAppVersion");
        }
        if (StringUtils.isNotBlank(checkUpdate.getApp())) {
            typeString.append(checkUpdate.getApp().trim());
        }
        List<Dict> dictList = msDictService.findListByType(typeString.toString());
        if (dictList != null && dictList.size() > 0) {
            Dict dict = dictList.get(0);
//			dict.setValue((String) redisUtils.get(RedisConstant.RedisDBType.REDIS_SEQ_DB, typeString, String.class));
            if (dict != null) {
                //版本比较
                String receivedVersion[] = checkUpdate.getVersion().split("\\.");
                String dbVersion[] = dict.getValue().split("\\.");
                Boolean needUpdate = false;

                for (int i = 0; i < (dbVersion.length < receivedVersion.length ? dbVersion.length : receivedVersion.length); i++) {
                    if (Integer.parseInt(dbVersion[i]) > Integer.parseInt(receivedVersion[i])) {
                        needUpdate = true;
                        break;
                    }
                    if (Integer.parseInt(dbVersion[i]) < Integer.parseInt(receivedVersion[i])) {
                        break;
                    }
                }

                if (needUpdate) {
                    Dict appForceUpdateFlagDict = MSDictUtils.getDictByValue("1", Dict.DICT_TYPE_APP_FORCE_UPDATE_FLAG);
                    int appForceUpdateFlag = appForceUpdateFlagDict != null && appForceUpdateFlagDict.getIntValue() > 0 ? 1 : 0;
                    JsonObject jo = new JsonObject();
                    jo.addProperty("version", dict.getValue());
                    jo.addProperty("updateTime", DateUtils.formatDate(dict.getUpdateDate()));
                    jo.addProperty("updateLog", dict.getDescription());
                    jo.addProperty("updateURL", dict.getRemarks());
                    jo.addProperty("appForceUpdateFlag", appForceUpdateFlag);
                    return RestResultGenerator.success(jo);
                }
            }
        }
        return RestResultGenerator.success();
    }

    /**
     * 根据type获取数据字典列表
     * 切换为微服务
     *
     * @param getOptionList
     * @return
     */
    public RestResult<Object> getDictListByType(RestGetOptionList getOptionList) {
        String typeString = "";
        if (getOptionList.getType() == 0) {
            //切换为微服务
            typeString = "completed_type";
        } else if (getOptionList.getType() == 1) {
            //切换为微服务
            typeString = "PendingType";
        }
        //切换为微服务
        else if (getOptionList.getType() == 2) {
            typeString = "order_abnormal_reason";
        }
        //切换为微服务
        else if (getOptionList.getType() == 3) {
            typeString = "material_apply_type";
        }
        //切换为微服务
        else if (getOptionList.getType() == 4) {
            typeString = "express_type";
        } else if (getOptionList.getType() == 5) {
            typeString = Dict.DICT_TYPE_BANK_TYPE;
        }
        List<Dict> dictList = Lists.newArrayList();
        if (getOptionList.getType() != 1) {
            dictList = MSDictUtils.getDictList(typeString);
        } else {
            dictList = MSDictUtils.getDictExceptList(typeString, "7"); //APP端不显示待跟进
        }

        List<RestDict> restDictList = Lists.newArrayList();
        if (dictList != null && dictList.size() > 0) {
            restDictList = mapper.mapAsList(dictList, RestDict.class);
        }
        return RestResultGenerator.success(restDictList);
    }
}
