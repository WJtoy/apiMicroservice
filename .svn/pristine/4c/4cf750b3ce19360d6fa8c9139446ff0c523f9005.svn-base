package com.wolfking.jeesite.modules.md.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.utils.AreaAdapter;
import com.wolfking.jeesite.modules.md.utils.AreaSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.ServicePointSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.ServicePointStationAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Map;

/**
 * 网点服务点
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonAdapter(ServicePointStationAdapter.class)
public class ServicePointStation extends LongIDDataEntity<ServicePointStation> {
    private String name = "";     // 服务点名称
    private String address = "";  // 除区/县的详细地址
    private Double longtitude;    // 经度
    private Double latitude;      // 纬度
    private Integer radius =0 ;       // 服务范围
    private Integer autoPlanFlag = 0; //自动派单开关,0-人工派单,1:自动派单 //2019-4-9
    @GsonIgnore
    private String orderBy;

    @JsonAdapter(ServicePointSimpleAdapter.class)
    private ServicePoint servicePoint;  //网点

    @GsonIgnore
    private String areas = "";          //保存时提交，使用Json格式
    //是否是第一次查询
    @GsonIgnore
    private Integer firstSearch = 1;

    @JsonAdapter(AreaSimpleAdapter.class)
    private Area area;   // 区域
}
