package com.wolfking.jeesite.modules.api.entity.md.mapper;

import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import com.wolfking.jeesite.modules.api.entity.md.RestEngineer;
import com.wolfking.jeesite.modules.api.entity.md.RestEngineerTemperatureInfo;
import com.wolfking.jeesite.modules.api.util.HealthOptionEnum;
import com.wolfking.jeesite.modules.api.util.TemperatureEnum;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.EngineerTemperature;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther wj
 * @Date 2021/1/14 9:40
 */
@Component
public class RestEngineerTemperatureMapper extends CustomMapper<RestEngineerTemperatureInfo, EngineerTemperature> {
    @Override
    public void mapAtoB(RestEngineerTemperatureInfo a, EngineerTemperature b, MappingContext context) {

    }

    @Override
    public void mapBtoA(EngineerTemperature b, RestEngineerTemperatureInfo a, MappingContext context) {
        AppDict appDict = new AppDict();
        AppDict appDict1 = new AppDict();
        a.setId(b.getId());
        if (b.getHealthOption()==HealthOptionEnum.healthNormal.value){
            appDict.setValue(String.valueOf(HealthOptionEnum.healthNormal.value));
            appDict.setLabel(String.valueOf(HealthOptionEnum.healthNormal.label));
        }else {
            appDict.setValue(String.valueOf(HealthOptionEnum.healthAbnormal.value));
            appDict.setLabel(String.valueOf(HealthOptionEnum.healthAbnormal.label));
        }
        a.setHealthOption(appDict);
        a.setTemperature(b.getTemperature());
        a.setCreateDate(b.getCreateDate());
              if (b.getHealthStatus().equals(TemperatureEnum.normal.value)) {
                  appDict1.setValue(String.valueOf(TemperatureEnum.normal.value));
                  appDict1.setLabel(TemperatureEnum.normal.healthStatus);
            }else if (b.getHealthStatus().equals( TemperatureEnum.abnormal.value)){
                  appDict1.setValue(String.valueOf(TemperatureEnum.abnormal.value));
                  appDict1.setLabel(TemperatureEnum.abnormal.healthStatus);
            }else if (b.getHealthStatus().equals(TemperatureEnum.abnormalTemperature.value)){
                  appDict1.setValue(String.valueOf(TemperatureEnum.abnormalTemperature.value));
                  appDict1.setLabel(TemperatureEnum.abnormalTemperature.healthStatus);
            }else if (b.getHealthStatus().equals(TemperatureEnum.abnormalHealth.value)){
                  appDict1.setValue(String.valueOf(TemperatureEnum.abnormalHealth.value));
                  appDict1.setLabel(TemperatureEnum.abnormalHealth.healthStatus);
            }else {
                  appDict1.setValue(String.valueOf(TemperatureEnum.abnormalHealthAndTemperature.value));
                  appDict1.setLabel(TemperatureEnum.abnormalHealthAndTemperature.healthStatus);
              }
        a.setHealthStatus(appDict1);
        }

}
