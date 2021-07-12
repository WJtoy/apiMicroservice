package com.wolfking.jeesite.modules.api.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther wj
 * @Date 2021/1/13 13:29
 */
public class EngineerTemperatureUtil {

    public static Integer isNormal(Integer healthOption,Double temperature) {
        Integer result =null;
        if (temperature<35.5 || temperature>37.2 ){
            if (healthOption== HealthOptionEnum.healthAbnormal.value){
                result = TemperatureEnum.abnormalHealthAndTemperature.value;
            }else {
                result = TemperatureEnum.abnormalTemperature.value;
            }
        }else {
            if (healthOption == HealthOptionEnum.healthNormal.value){
                result = TemperatureEnum.normal.value;
            }else if (healthOption == HealthOptionEnum.healthAbnormal.value){
                result = TemperatureEnum.abnormalHealth.value ;
            }else {
                result = TemperatureEnum.abnormal.value;
            }
        }
        return result;
    }



}
